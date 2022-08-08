package com.zcdy.dsc.modules.settle.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.exception.BusinessException;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.constant.ScaleConstant;
import com.zcdy.dsc.modules.configcentre.constant.SystemParamConstant;
import com.zcdy.dsc.modules.configcentre.entity.SystemConfig;
import com.zcdy.dsc.modules.configcentre.service.SystemParamService;
import com.zcdy.dsc.modules.operation.equipment.entity.IotEquipmentPrice;
import com.zcdy.dsc.modules.settle.constant.SettleConstant;
import com.zcdy.dsc.modules.settle.entity.SettleBatch;
import com.zcdy.dsc.modules.settle.mapper.SettleBatchMapper;
import com.zcdy.dsc.modules.settle.mapper.SettlementStatisticsMapper;
import com.zcdy.dsc.modules.settle.param.CustometInfoParam;
import com.zcdy.dsc.modules.settle.service.SettleBatchService;
import com.zcdy.dsc.modules.settle.vo.CustomerInfoData.CustomerCount;
import com.zcdy.dsc.modules.settle.vo.CustomerInfoData.CustomerMonthInfo;
import com.zcdy.dsc.modules.settle.vo.SettleBatchOptEquipInfoVo;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsVo;

/**
 * @author tangchao
 * @version V1.0
 * @Description: 结算批次
 * @date 2020-5-8 14:42:34
 */
@Service
public class SettleBatchServiceImpl extends ServiceImpl<SettleBatchMapper, SettleBatch> implements SettleBatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettleBatchServiceImpl.class);

    @Resource
    private SettlementStatisticsMapper settlementStatisticsMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private SystemParamService systemParamService;
    private final String pattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * 调度器运行接口
     *
     * @param date 时间
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void executeAll(String date) {
        SystemConfig sysParam = this.systemParamService.getConfigByKey(SystemParamConstant.CUSTOMER_METER_FLOW_VARTYPE);
        if (null == sysParam) {
            throw new BusinessException("系统参数配置不正确，请正确配置参数：" + SystemParamConstant.CUSTOMER_METER_FLOW_VARTYPE);
        }
        // 获取需要抄表的记录
        String dateTimeStr = DateUtil.currentDateTimeStr();
        List<IotEquipmentPrice> iotPrices =
                this.settlementStatisticsMapper.getEndWithTodayRule(date, dateTimeStr, sysParam.getConfigValue());
        List<SettleBatch> settleBatches = executeAllSettleBatch(iotPrices);
        if (settleBatches == null || settleBatches.size() == 0) {
            LOGGER.warn("没有有效的批次记录!!");
            return;
        }
        this.saveBatch(settleBatches);
    }

    /**
     * 在一下情况生成所有结算批次 1. 水价即将变更(明天),自动抄表记录阶段价格最后日期表底 2. 周期性水表到月自动抄表, 目前逻辑按照自然月
     *
     * @author tangchao
     * @since 2020年5月8日10:31:57
     */
    public List<SettleBatch> executeAllSettleBatch(List<IotEquipmentPrice> iotPrices) {
        if (iotPrices == null || iotPrices.size() == 0) {
            LOGGER.warn("com.zcdy.dsc.modules.settle.service.impl.SettleBatchServiceImpl 没有要结算的数据.");
            return null;
        }
        final Date now = new Date();
        List<SettleBatch> batch = new ArrayList<>();
        iotPrices.forEach(item -> {
            SettleBatch settleBatch = executeSettleBatch(item, now);
            if (settleBatch == null) {
                return;
            }
            batch.add(settleBatch);
        });
        return batch;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void executeEquipmentNow(String optId, String status) {
        String date = DateUtil.currentDateStr();
        String time = LocalDateTime.now().plusSeconds(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, String> param = new HashMap<>(2);
        param.put("equipmentId", optId);
        param.put("date", date);
        param.put("time", time);
        BigDecimal unitPrice = this.settlementStatisticsMapper.getUnitPrice(param);
        if (unitPrice == null) {
            throw new BusinessException("水价不存在.");
        }
        String waterVar = this.baseMapper.getWaterVar(optId);
        if (StringUtils.isBlank(waterVar)) {
            throw new BusinessException("该设备没有绑定变量.");
        }
        String nowTime = LocalDateTime.now().plusMinutes(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        SettleBatch settleBatch =
                this.executeSettleBatch(waterVar, optId, unitPrice.toEngineeringString(), nowTime, status);
        this.baseMapper.insert(settleBatch);
    }

    public SettleBatch executeSettleBatch(String varName, String optId, String price, Date date) {
        return this.executeSettleBatch(varName, optId, price, date, SettleConstant.STATUS_UNSETTLEMENT);
    }

    public SettleBatch executeSettleBatch(String varName, String optId, String price, Date date, String status) {
        String dateStr = date.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(pattern));
        return executeSettleBatch(varName, optId, price, dateStr, status);
    }

    /**
     * 生成单一水表结算批次数据
     *
     * @param date    时间
     * @param varName 需要在redis统计的变量名
     * @param optId   设备id
     * @param status  状态
     * @return 批次数据
     * @author tangchao
     * @see com.zcdy.dsc.modules.settle.constant.SettleConstant status
     * @since 2020-5-8 10:47:02
     */
    public SettleBatch executeSettleBatch(String varName, String optId, String price, String date, String status) {
        if (StringUtils.isBlank(varName) || StringUtils.isBlank(optId)) {
            return null;
        }
        // 通过变量名从redis读取当前流量
        String valueEntityStr =
                this.stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstantV2.REDISDATAKEY, varName));
        String entityValue = null;
        if (StringUtils.isBlank(valueEntityStr)) {
            LOGGER.warn("redis key:{} 值不存在或为空, 查找上次抄表记录.", String.format(RedisKeyConstantV2.REDISDATAKEY, varName));
            SettleBatch previousSettleBatch = getPreviousSettleBatch(optId);
            if(previousSettleBatch == null){
                entityValue = "0.00";
            }else{
                entityValue = previousSettleBatch.getCurrentFlow();
            }
        }else{
            entityValue = JSON.parseObject(valueEntityStr, ValueEntity.class).getValue();
        }

        SettleBatch settleBatch = new SettleBatch();
        settleBatch.setStatus(status);
        // 获取设备合同客户区域等信息
        SettleBatchOptEquipInfoVo optEquipInfo = this.getOptEquipInfo(optId, date);
        if (StringUtils.isBlank(optEquipInfo.getContractId())) {
            throw new BusinessException("合同信息不存在: " + date);
        }
        BeanUtils.copyProperties(optEquipInfo, settleBatch);
        BigDecimal currentPositiveFlow =
                new BigDecimal(entityValue).setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP);
        settleBatch.setCurrentPositiveFlow(currentPositiveFlow.toEngineeringString());
        settleBatch.setCurrentNavigatFlow(
                BigDecimal.ZERO.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toEngineeringString());
        settleBatch.setCurrentFlow(settleBatch.getCurrentPositiveFlowDecimal()
                .add(settleBatch.getCurrentNavigatFlowDecimal()).toEngineeringString());
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            settleBatch.setCurrentFlowDate(sdf.parse(date));
            settleBatch.setCurrentFlowTime(sdf.parse(date));
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
        // 查找上一批次
        SettleBatch previousSettleBatch = getPreviousSettleBatch(settleBatch.getEquipmentId());
        settleBatch.setPreviousId(previousSettleBatch.getId());

        BigDecimal previousFlowDecimal = previousSettleBatch.getCurrentFlowDecimal();

        // 净用水
        settleBatch.setUsedFlow(currentPositiveFlow.subtract(previousFlowDecimal).toEngineeringString());
        settleBatch.setUnitPrice(new BigDecimal(Optional.of(price).get()));
        settleBatch.setTotalCost(settleBatch.getUsedFlowDecimal().multiply(settleBatch.getUnitPrice()));
        //如果是出事话数据, 将用水量归零,
        if (SettleConstant.STATUS_INIT_SETTLEMENT.equals(status)) {
            settleBatch.setUsedFlow("0");
            settleBatch.setTotalCost(BigDecimal.ZERO);
        }
        return settleBatch;
    }

    /**
     * 生成单一水表结算批次数据
     *
     * @return null 变量名为空 redis不存在key SettleBatch 生成结算数据
     * @author tangchao
     * @since 2020-5-8 10:47:02
     */
    public SettleBatch executeSettleBatch(IotEquipmentPrice iotEquipmentPrice, Date now) {
        if (iotEquipmentPrice == null) {
            return null;
        }
        return this.executeSettleBatch(iotEquipmentPrice.getVariableName(), iotEquipmentPrice.getOptId(),
                iotEquipmentPrice.getPrice(), now);
    }

    /**
     * 根据设备id, 和给定时间查找, 距离这个时间之前最近的结算批次
     *
     * @param equipmentId 资产水表id
     * @param time        日期
     * @return null 参数不正确 如果有上一条, 返回上一次批次 如果没有, 说明为第一条数据, 返回初始化空数据
     * @author tangchao
     */
    @Override
    public SettleBatch getPreviousSettleBatch(String equipmentId, String time) {
        if (StringUtils.isBlank(equipmentId)) {
            return null;
        }
        SettleBatch settleBatch;
        QueryWrapper<SettleBatch> query = new QueryWrapper<>();
        query.lambda().eq(SettleBatch::getEquipmentId, equipmentId).lt(SettleBatch::getCurrentFlowTime, time)
                .orderByDesc(SettleBatch::getCurrentFlowTime);
        IPage<SettleBatch> page = this.baseMapper.selectPage(new Page<>(1, 1), query);
        if (page.getRecords().size() > 0) {
            settleBatch = page.getRecords().get(0);
        } else {
            settleBatch = SettleBatch.initEmptyInstant();
        }
        return settleBatch;
    }

    /**
     * 查找水表最后的一条批次数据
     *
     * @param equipmentId 设备id
     * @return null 参数不正确 如果有上一条, 返回上一次批次 如果没有, 说明为第一条数据, 返回初始化空数据
     * @see com.zcdy.dsc.modules.settle.service.impl.SettleBatchServiceImpl#getPreviousSettleBatch(String, String)
     */
    @Override
    public SettleBatch getPreviousSettleBatch(String equipmentId) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
        return this.getPreviousSettleBatch(equipmentId, now);
    }

    /**
     * 获取设备某个时刻的信息
     *
     * @param optEquipmentId 设备id
     * @param date           时间
     * @return 设备信息
     */
    @Override
    public SettleBatchOptEquipInfoVo getOptEquipInfo(String optEquipmentId, String date) {
        Map<String, String> param = new HashMap<>(2);
        param.put("equipmentId", optEquipmentId);
        param.put("now", date);
        return this.baseMapper.getOptEquipInfo(param);
    }

    @Override
    public List<CustomerMonthInfo> queryMonthInfo(CustometInfoParam param) {
        //按照月份分组
        SimpleDateFormat smFormat = new SimpleDateFormat("yyyy-MM");
        List<SettlementStatisticsVo> listData = baseMapper.listData(param);
        Map<String, List<SettlementStatisticsVo>> dataMap = new LinkedHashMap<>(listData.size());
        for (SettlementStatisticsVo statisticsVo : listData) {
            if (statisticsVo.getSettleDate() != null) {
                String format = smFormat.format(statisticsVo.getSettleDate());
                List<SettlementStatisticsVo> list = dataMap.computeIfAbsent(format, k -> new ArrayList<>());
                list.add(statisticsVo);
            }
        }
        // 转list
        return dataMap.entrySet().stream()
                .map(e -> new CustomerMonthInfo(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    @Override
    public List<CustomerCount> customerCount(CustometInfoParam param) {
        return baseMapper.customerCount(param);
    }

    @Override
    public IPage<SettlementStatisticsVo> queryHistory(Page<SettlementStatisticsVo> page, CustometInfoParam param) {
        return baseMapper.historyData(page, param);
    }

    @Override
    public List<SettlementStatisticsVo> exportHistory(CustometInfoParam param) {
        return baseMapper.historyData(param);
    }



}
