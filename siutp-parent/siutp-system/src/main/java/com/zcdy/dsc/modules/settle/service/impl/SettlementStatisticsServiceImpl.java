package com.zcdy.dsc.modules.settle.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.exception.BusinessException;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.constant.ScaleConstant;
import com.zcdy.dsc.modules.operation.equipment.entity.MeterFlow;
import com.zcdy.dsc.modules.operation.equipment.service.MeterFlowService;
import com.zcdy.dsc.modules.settle.constant.SettleConstant;
import com.zcdy.dsc.modules.settle.entity.SettleBatch;
import com.zcdy.dsc.modules.settle.entity.SettleChecklist;
import com.zcdy.dsc.modules.settle.mapper.SettlementStatisticsMapper;
import com.zcdy.dsc.modules.settle.service.SettleBatchService;
import com.zcdy.dsc.modules.settle.service.SettlementStatisticsService;
import com.zcdy.dsc.modules.settle.vo.SettleBatchOptEquipInfoVo;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsQueryParam;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsVo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author tangchao
 * @date 2020-5-8 15:06:00
 */

@Service
public class SettlementStatisticsServiceImpl implements SettlementStatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementStatisticsServiceImpl.class);

    @Resource
    private SettlementStatisticsMapper settlementStatisticsMapper;

    @Resource
    private SettleBatchService settleBatchService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SettleChecklistServiceImpl settleChecklistService;

    @Resource
    private MeterFlowService meterFlowService;

    @Override
    public List<SettlementStatisticsVo> getSettleByCustomerId(String customerId) {
        if (StringUtils.isBlank(customerId)) {
            throw new BusinessException("客户ID不能为空!");
        }
        List<SettlementStatisticsVo> result = new ArrayList<>(getDynamicDataForNow(customerId));
        //获取所有绑定的水表目前的值
        List<SettlementStatisticsVo> settleByCustomerId = this.settlementStatisticsMapper.getSettleByCustomerId(customerId);
        if(settleByCustomerId != null){
            for(SettlementStatisticsVo s : settleByCustomerId){
                SettleBatch previousSettleBatch = this.settleBatchService.getPreviousSettleBatch(s.getEquipmentId(), s.getCurrentFlowTimeStr());
                BigDecimal previousCurrentFlow = new BigDecimal(previousSettleBatch.getCurrentFlow());
                BigDecimal currentFlowDecimal = s.getCurrentFlowDecimal();
                BigDecimal usedFlow = currentFlowDecimal.subtract(previousCurrentFlow);
                BigDecimal unitPrice = new BigDecimal(s.getCurrentWaterPrice());
                s.setCurrentUsedFlow(usedFlow.setScale(ScaleConstant.LL_SCALE_SHOW, RoundingMode.HALF_UP).toEngineeringString());
                s.setCurrentCost(unitPrice.multiply(usedFlow).setScale(ScaleConstant.YL_SCALE, RoundingMode.HALF_UP).toEngineeringString());
                s.setPreviousFlowDate(previousSettleBatch.getCurrentFlowDate());
                s.setPreviousFlowTime(previousSettleBatch.getCurrentFlowTime());
                s.setPreviousFlow(previousSettleBatch.getCurrentFlow());
                s.setPreviousId(String.valueOf(previousSettleBatch.getId()));
            }
            result.addAll(settleByCustomerId);
        }
        return result;
    }

    @Override
    public List<SettlementStatisticsVo> getDynamicDataForNow(String customerId) {
        List<SettlementStatisticsVo> result = new ArrayList<>();
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        //获取客户所有绑定的周期水表的变量
        Map<String, String> param = new HashMap<>(2);
        param.put("customerId", customerId);
        param.put("nowTime", nowTime);
        param.put("nowDate", nowDate);
        List<SettleBatchOptEquipInfoVo> varList = this.settlementStatisticsMapper.getWaterVarByCustomerId(param);
        if (varList == null) {
            varList = new ArrayList<>(0);
        }
        int signCount = this.settlementStatisticsMapper.getSignCount(customerId);
        varList.forEach(item -> {
            String varName = item.getVariableName();
            String valueEntityStr = this.stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstantV2.REDISDATAKEY, varName));
            if (StringUtils.isBlank(valueEntityStr)) {
                return;
            }
            ValueEntity valueEntity = JSON.parseObject(valueEntityStr, ValueEntity.class);
            SettlementStatisticsVo statisticsVo = getSettlementStatisticsVo(nowTime, signCount, item, valueEntity.getValue());
            result.add(statisticsVo);
        });
        return result;
    }

    /**
     * 通过时间 选择时间之前的数据
     *
     * @param customerId 客户id
     * @param date   yyyy-MM-dd
     * @return 结算数据
     */
    @Override
    public List<SettlementStatisticsVo> getSettleByCustomerId(String customerId, String date) {
        List<SettlementStatisticsVo> result = new ArrayList<>();
        Map<String, String> param = new HashMap<>(2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date inputDate;
        try {
            inputDate = sdf.parse(date);
        } catch (ParseException e) {
            LOGGER.error(String.format("%s 日期格式不正确: %s", date, "yyyy-MM-dd"));
            throw new BusinessException(String.format("%s 日期格式不正确: %s", date, "yyyy-MM-dd"));
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(inputDate);
        cal1.set(Calendar.HOUR_OF_DAY, 23);
        cal1.set(Calendar.MINUTE, 59);
        cal1.set(Calendar.SECOND, 59);
        final Date dateObj = cal1.getTime();
        String dateTime = dateObj.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        param.put("customerId", customerId);
        param.put("nowTime", dateTime);
        List<SettleBatchOptEquipInfoVo> varList = this.settlementStatisticsMapper.getWaterVarByCustomerId(param);
        if (varList == null) {
            varList = new ArrayList<>(0);
        }
        int signCount = this.settlementStatisticsMapper.getSignCount(customerId);
        varList.forEach(item -> {
            String equipmentId = item.getEquipmentId();
            MeterFlow meterFlow = this.meterFlowService.getMeterByDateAndEquipmentId(equipmentId, date);
            if (meterFlow == null || StringUtils.isBlank(meterFlow.getPositiveFlow())) {
                LOGGER.error(String.format("%s 设备没有水量信息: %s", equipmentId, date));
                throw new BusinessException(String.format("%s 设备没有水量信息: %s , 请联系管理员.", equipmentId, date));
            }
            SettlementStatisticsVo settlementStatisticsVo = getSettlementStatisticsVo(dateTime, signCount, item, meterFlow.getPositiveFlow());
            if(settlementStatisticsVo.getPreviousFlowTime().compareTo(dateObj) > 0){
                LOGGER.error(String.format("%s 日期不能早于最后结算的日期: %s", dateTime,
                        settlementStatisticsVo.getPreviousFlowTime().toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
                throw new BusinessException(String.format("%s 日期不能早于最后结算的日期: %s , 请联系管理员.", dateTime,
                        settlementStatisticsVo.getPreviousFlowTime().toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            }
            result.add(settlementStatisticsVo);
        });
        List<SettlementStatisticsVo> settleByCustomerId = this.settlementStatisticsMapper.getSettleByCustomerId(customerId);
        result.addAll(settleByCustomerId);
        return result;
    }

    /**
     * 计算结算周期数据
     *
     * @param dateTime  抄表时间日期
     * @param signCount 签约次数
     * @param item      设备信息
     * @param flow      抄表日期时候的表底数
     * @return 结算周期
     */
    private SettlementStatisticsVo getSettlementStatisticsVo(String dateTime, int signCount, SettleBatchOptEquipInfoVo item, String flow) {
        SettlementStatisticsVo statisticsVo = new SettlementStatisticsVo();
        BeanUtils.copyProperties(item, statisticsVo);
        statisticsVo.setSignCount(String.valueOf(signCount));
        //上次结算数据
        SettleBatch previousSettleBatch = this.settleBatchService.getPreviousSettleBatch(item.getEquipmentId());
        statisticsVo.setPreviousId(String.valueOf(previousSettleBatch.getId()));
        statisticsVo.setPreviousFlowDate(previousSettleBatch.getCurrentFlowDate());
        statisticsVo.setPreviousFlowTime(previousSettleBatch.getCurrentFlowTime());
        statisticsVo.setPreviousFlow(previousSettleBatch.getCurrentFlow());
        statisticsVo.setCurrentFlowDateStr(dateTime);
        statisticsVo.setCurrentFlowTimeStr(dateTime);
        BigDecimal currentFlowDecimal = new BigDecimal(flow).setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP);
        BigDecimal previousFlowDecimal = previousSettleBatch.getCurrentFlowDecimal();
        BigDecimal currentUsedFlow = currentFlowDecimal.subtract(previousFlowDecimal);
        BigDecimal unitPrice = this.getUnitPrice(item.getEquipmentId(), dateTime);
        if (unitPrice == null) {
            unitPrice = BigDecimal.ZERO;
        }
        statisticsVo.setCurrentFlow(currentFlowDecimal.toEngineeringString());
        statisticsVo.setCurrentUsedFlow(currentUsedFlow.toEngineeringString());
        statisticsVo.setCurrentWaterPrice(unitPrice.setScale(ScaleConstant.PRICE, RoundingMode.HALF_UP).toEngineeringString());
        statisticsVo.setCurrentCost(currentUsedFlow.multiply(unitPrice).setScale(ScaleConstant.PRICE, RoundingMode.HALF_UP).toEngineeringString());
        return statisticsVo;
    }

    @Override
    public IPage<SettlementStatisticsVo> pageSettle(Page<SettlementStatisticsVo> page, SettlementStatisticsQueryParam param) {
        return this.settlementStatisticsMapper.pageSettle(page, param);
    }


    @Override
    public List<SettlementStatisticsVo> getSettleByQueryParam(SettlementStatisticsQueryParam queryParam) {
        List<String> customerIds;
        if(StringUtils.isBlank(queryParam.getCustomerId())){
            customerIds = this.getCustomerIds(queryParam);
        }else{
            customerIds = Collections.singletonList(queryParam.getCustomerId());
        }

        if (customerIds == null || customerIds.size() == 0) {
            return new ArrayList<>(0);
        }
        List<SettlementStatisticsVo> result = new ArrayList<>();
        String queryDate = queryParam.getQueryDate();
        if(StringUtils.isBlank(queryDate)){
            queryDate = DateUtil.currentDateStr();
        }
        LocalDate queryDateObj = LocalDate.parse(queryDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if(LocalDate.now(ZoneId.systemDefault()).compareTo(queryDateObj) < 0){
            throw new BusinessException("日期晚于当前时间: " + queryDate);
        }
        //如果为当前时间, 则从redis 取实时数据.
        boolean isNow =  LocalDate.now(ZoneId.systemDefault()).compareTo(queryDateObj) == 0;

        for (String customerId : customerIds){
            if(isNow){
                result.addAll(this.getSettleByCustomerId(customerId));
            }else{
                result.addAll(this.getSettleByCustomerId(customerId, queryDate));
            }
        }
        return result;
    }

    @Override
    public List<String> getCustomerIds(SettlementStatisticsQueryParam queryParam) {
        return this.settlementStatisticsMapper.getCustomerIds(queryParam);
    }


    /**
     * 通过水表id, 和日期 获取水表的单价
     * 返回值可能为null, 在水价表中找不到数据. 注意处理NullPointException
     *
     * @param equipmentId 获取水表单价
     * @param time        时间 yyyy-MM-dd HH:mm:dd
     * @return 单价
     */
    @Override
    public BigDecimal getUnitPrice(String equipmentId, String time) {
        String date = "";
        try {
            LocalDate parse = LocalDate.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            date = parse.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            LOGGER.error("非时间格式." + e);
            return null;
        }
        Map<String, String> param = new HashMap<>(2);
        param.put("equipmentId", equipmentId);
        param.put("date", date);
        param.put("time", time);
        return this.settlementStatisticsMapper.getUnitPrice(param);
    }

    @Override
    public void manualSettle(List<SettlementStatisticsVo> settlementList) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (sysUser == null) {
            throw new BusinessException("用户没有登录.");
        }
        String settleBy = sysUser.getUsername();
        this.settle(settlementList, SettleConstant.STATUS_MANUAL_SETTLEMENT, settleBy);
    }

    @Override
    public void autoSettle(List<SettlementStatisticsVo> settlementList) {
        this.settle(settlementList, SettleConstant.STATUS_AUTO_SETTLEMENT, SettleConstant.AUTO_SETTLE_PERSON);
    }

    /**
     * 手工结算全部
     *
     * @param settlementList 结算批次列表
     */

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void settle(List<SettlementStatisticsVo> settlementList, String status, String settleBy) {
        if (settlementList == null || settlementList.size() == 0) {
            throw new BusinessException("没有可结算数据.");
        }
        BigDecimal totalUsedFlow = BigDecimal.ZERO.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP);
        BigDecimal totalCost = BigDecimal.ZERO.setScale(ScaleConstant.PRICE, RoundingMode.HALF_UP);
        for (SettlementStatisticsVo item : settlementList) {
            totalUsedFlow = totalUsedFlow.add(new BigDecimal(item.getCurrentUsedFlow()));
            totalCost = totalCost.add(new BigDecimal(item.getCurrentCost()));
        }

        Date nowDate = new Date();
        SettleChecklist settleChecklist = new SettleChecklist();
        settleChecklist.setSettleBy(settleBy);
        settleChecklist.setSettleDate(nowDate);
        settleChecklist.setSettleTime(nowDate);
        settleChecklist.setTotalCost(totalCost);
        settleChecklist.setTotalUsedflow(totalUsedFlow.toEngineeringString());
        settleChecklistService.save(settleChecklist);
        Integer checklistId = settleChecklist.getId();
        for (SettlementStatisticsVo item : settlementList) {
            //新增
            if (item.getId() == null) {
                SettleBatch settleBatch = this.settleBatchService.getById(item.getPreviousId());
                settleBatch.setPreviousId(settleBatch.getId());
                settleBatch.setChecklistId(checklistId);
                settleBatch.setId(null);
                settleBatch.setCurrentPositiveFlow(new BigDecimal(item.getCurrentFlow()).setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toEngineeringString());
                settleBatch.setCurrentNavigatFlow(BigDecimal.ZERO.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toEngineeringString());
                settleBatch.setCurrentFlow(new BigDecimal(item.getCurrentFlow()).setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toEngineeringString());
                settleBatch.setCurrentFlowDate(item.getCurrentFlowDate());
                settleBatch.setCurrentFlowTime(item.getCurrentFlowTime());
                settleBatch.setStatus(status);
                settleBatch.setDistrictId(item.getEquipmentDistrictId());
                settleBatch.setContractId(item.getContractId());
                settleBatch.setCustomerId(item.getCustomerId());
                BigDecimal currentUsedFlow = new BigDecimal(item.getCurrentUsedFlow()).setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP);
                settleBatch.setUsedFlow(currentUsedFlow.toEngineeringString());
                BigDecimal unitPrice = new BigDecimal(item.getCurrentWaterPrice()).setScale(ScaleConstant.PRICE, RoundingMode.HALF_UP);
                settleBatch.setUnitPrice(unitPrice);
                BigDecimal transationCost = new BigDecimal(item.getCurrentCost());
                BigDecimal computeCost = currentUsedFlow.multiply(unitPrice).setScale(ScaleConstant.PRICE, RoundingMode.HALF_UP);
                if (computeCost.compareTo(transationCost) != 0) {
                    throw new BusinessException("净用水 * 单价 != 花费");
                }
                settleBatch.setTotalCost(computeCost);
                settleBatch.setCreateBy(settleBy);
                settleBatch.setCreateTime(nowDate);
                settleBatch.setUpdateBy(settleBy);
                settleBatch.setUpdateTime(nowDate);
                this.settleBatchService.save(settleBatch);
            } else {
                UpdateWrapper<SettleBatch> updateWrapper = new UpdateWrapper<>();
                updateWrapper.lambda().eq(SettleBatch::getId, item.getId());
                SettleBatch settleBatch = new SettleBatch();
                settleBatch.setPreviousId(Integer.parseInt(item.getPreviousId()));
                settleBatch.setStatus(status);
                settleBatch.setChecklistId(checklistId);
                settleBatch.setCurrentFlow(item.getCurrentFlow());
                settleBatch.setUsedFlow(item.getCurrentUsedFlow());
                BigDecimal unitPrice = new BigDecimal(item.getCurrentWaterPrice()).setScale(ScaleConstant.PRICE, RoundingMode.HALF_UP);
                settleBatch.setUnitPrice(unitPrice);
                BigDecimal currentUsedFlow = new BigDecimal(item.getCurrentUsedFlow()).setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP);
                BigDecimal transationCost = new BigDecimal(item.getCurrentCost());
                BigDecimal computeCost = currentUsedFlow.multiply(unitPrice);
                if (computeCost.compareTo(transationCost) != 0) {
                    throw new BusinessException("净用水 * 单价 != 花费");
                }
                settleBatch.setDistrictId(item.getEquipmentDistrictId());
                settleBatch.setContractId(item.getContractId());
                settleBatch.setCustomerId(item.getCustomerId());
                settleBatch.setTotalCost(computeCost);
                settleBatch.setUpdateBy(settleBy);
                settleBatch.setUpdateTime(nowDate);
                this.settleBatchService.update(settleBatch, updateWrapper);
            }
        }

    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void execute(String custoemrId) {
        List<SettlementStatisticsVo> dynamicDataForNow = this.getDynamicDataForNow(custoemrId);
        this.autoSettle(dynamicDataForNow);
    }

    @Override
    public void execute(JobExecutionContext context) {
        List<String> customerIds = this.getCustomerIds(new SettlementStatisticsQueryParam());
        if (customerIds == null || customerIds.size() == 0) {
            LOGGER.warn(String.format("%s 自动周期结算: 没有可以结算的客户.", DateUtil.currentDateTimeStr()));
            return;
        }
        customerIds.forEach(this::execute);
    }

}
