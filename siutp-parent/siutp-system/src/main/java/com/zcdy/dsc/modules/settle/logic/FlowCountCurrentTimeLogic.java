package com.zcdy.dsc.modules.settle.logic;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.CaseFormat;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.common.util.NumberFormatUtil;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.constant.ScaleConstant;
import com.zcdy.dsc.constant.UnitConstant;
import com.zcdy.dsc.modules.collection.gis.vo.MeterAndFlowCountVo;
import com.zcdy.dsc.modules.datacenter.statistic.service.ModuleVarService;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentVariableVO;
import com.zcdy.dsc.modules.operation.equipment.constant.EquipIdConstant;
import com.zcdy.dsc.modules.operation.equipment.entity.MeterFlow;
import com.zcdy.dsc.modules.operation.equipment.service.MeterFlowService;
import com.zcdy.dsc.modules.settle.constant.FlowModuleConstant;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 描述: 今日用水量统计
 *
 * @author songguang.jiao
 * 创建时间:  2020年5月7日 下午2:52:00
 * 版本: V1.0
 */
@Service("flowCountCurrentTimeLogic")
public class FlowCountCurrentTimeLogic {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private MeterFlowService meterFlowService;

    @Resource
    private ModuleVarService moduleVarService;

    /**
     * 描述: 查询当日实时的流量数据
     * 流量计模块统计(7892dbd36ccb11ea9fded05099cd3eff),每日水表统计(2e1b78e76a8811ea9fded05099cd3eff),执行河东水厂日流量统计 模块(d61d3cba7e2711ea9fded05099cd3eff)
     * @author: songguang.jiao
     * 创建时间:  2020年5月7日 下午3:03:58
     * 版本: V1.0
     */
    public List<MeterAndFlowCountVo> getTodayData() {
        List<MeterAndFlowCountVo> allCurrentData = new ArrayList<MeterAndFlowCountVo>();
        //河东水厂
        Map<String, MeterFlow> hdData = this.calculateHd("d61d3cba7e2711ea9fded05099cd3eff");
        BigDecimal flowHd = BigDecimal.ZERO;
        MeterFlow hd = hdData.get(EquipIdConstant.HDSC);
        if (hd != null) {
            flowHd = flowHd.add(new BigDecimal(hd.getNetFlowDay())).setScale(ScaleConstant.LL_SCALE_SHOW,
                    RoundingMode.HALF_UP);
        }
        MeterAndFlowCountVo meterHeDong = new MeterAndFlowCountVo();
        meterHeDong.setFlowName(FlowModuleConstant.FLOW_HD_NAME);
        meterHeDong.setFlowCode(FlowModuleConstant.FLOW_HD);
        meterHeDong.setFlowCount(flowHd.toString() + UnitConstant.FLOWUNIT);
        meterHeDong.setFlowCountFormat(NumberFormatUtil.formatData(flowHd) + UnitConstant.FLOWUNIT);
        allCurrentData.add(meterHeDong);

        //行政区 7标+2标流量
        Map<String, MeterFlow> flowData = this.calculate("7892dbd36ccb11ea9fded05099cd3eff");
        BigDecimal flowXzq = BigDecimal.ZERO;
        MeterFlow gll201 = flowData.get(EquipIdConstant.GLL_2_01);
        if (gll201 != null) {
            flowXzq = flowXzq.add(new BigDecimal(gll201.getNetFlowDay())).setScale(ScaleConstant.LL_SCALE_SHOW,
                    RoundingMode.HALF_UP);
        }
        MeterFlow gll701 = flowData.get(EquipIdConstant.GLL_7_01);
        if (gll701 != null) {
            flowXzq = flowXzq.add(new BigDecimal(gll701.getNetFlowDay())).setScale(ScaleConstant.LL_SCALE_SHOW,
                    RoundingMode.HALF_UP);
        }
        MeterAndFlowCountVo meterXzq = new MeterAndFlowCountVo();
        meterXzq.setFlowName(FlowModuleConstant.FLOW_XZQ_NAME);
        meterXzq.setFlowCode(FlowModuleConstant.FLOW_XZQ);
        meterXzq.setFlowCount(flowXzq.toString() + UnitConstant.FLOWUNIT);
        meterXzq.setFlowCountFormat(NumberFormatUtil.formatData(flowXzq) + UnitConstant.FLOWUNIT);
        allCurrentData.add(meterXzq);

        //行政区入楼
        Map<String, MeterFlow> meterData = this.calculate("2e1b78e76a8811ea9fded05099cd3eff");
        List<String> rulou = EquipIdConstant.METER_RULOU;
        BigDecimal flowRulou = BigDecimal.ZERO;
        for (String string : rulou) {
            MeterFlow meterFlow = meterData.get(string);
            if (meterFlow != null) {
                flowRulou = flowRulou.add(new BigDecimal(meterFlow.getNetFlowDay())).setScale(ScaleConstant.LL_SCALE_SHOW,
                        RoundingMode.HALF_UP);
            }
        }
        MeterAndFlowCountVo meterRuLou = new MeterAndFlowCountVo();
        meterRuLou.setFlowName(FlowModuleConstant.FLOW_XZRL_NAME);
        meterRuLou.setFlowCode(FlowModuleConstant.FLOW_XZRL);
        meterRuLou.setFlowCount(flowRulou.toString() + UnitConstant.FLOWUNIT);
        meterRuLou.setFlowCountFormat(NumberFormatUtil.formatData(flowRulou) + UnitConstant.FLOWUNIT);
        allCurrentData.add(meterRuLou);

        //行政区绿化  =行政区-行政区入楼
        MeterAndFlowCountVo meterLvHua = new MeterAndFlowCountVo();
        meterLvHua.setFlowName(FlowModuleConstant.FLOW_XZLH_NAME);
        meterLvHua.setFlowCode(FlowModuleConstant.FLOW_XZLH);
        BigDecimal flowLvhua = flowXzq.subtract(flowRulou).setScale(ScaleConstant.LL_SCALE_SHOW, RoundingMode.HALF_UP);
        meterLvHua.setFlowCount(flowLvhua.toString() + UnitConstant.FLOWUNIT);
        meterLvHua.setFlowCountFormat(NumberFormatUtil.formatData(flowLvhua) + UnitConstant.FLOWUNIT);
        allCurrentData.add(meterLvHua);

        //镜河补水
        Map<String, MeterFlow> jhData = this.calculateHd("d61d3cba7e2711ea9fded05099cd3eff");
        BigDecimal flowFzg = BigDecimal.ZERO;
        MeterFlow fzg = jhData.get(EquipIdConstant.FZG_WSB05);
        if (fzg != null) {
            flowFzg = flowFzg.add(new BigDecimal(fzg.getNetFlowDay())).setScale(ScaleConstant.LL_SCALE_SHOW, RoundingMode.HALF_UP);
        }
        MeterAndFlowCountVo meterFzg = new MeterAndFlowCountVo();
        meterFzg.setFlowName(FlowModuleConstant.FLOW_FZG_NAME);
        meterFzg.setFlowCode(FlowModuleConstant.FLOW_FZG);
        meterFzg.setFlowCount(flowFzg.toString() + UnitConstant.FLOWUNIT);
        meterFzg.setFlowCountFormat(NumberFormatUtil.formatData(flowFzg) + UnitConstant.FLOWUNIT);
        allCurrentData.add(meterFzg);

        //华电北燃
        Map<String, MeterFlow> hdbrData = this.calculateHd("d61d3cba7e2711ea9fded05099cd3eff");
        BigDecimal flowHdbr = BigDecimal.ZERO;
        MeterFlow hdbr = hdbrData.get(EquipIdConstant.HBDR_WSB01);
        if (hdbr != null) {
            flowHdbr = flowHdbr.add(new BigDecimal(hdbr.getNetFlowDay())).setScale(ScaleConstant.LL_SCALE_SHOW, RoundingMode.HALF_UP);
        }
        MeterAndFlowCountVo meterHdbr = new MeterAndFlowCountVo();
        meterHdbr.setFlowName(FlowModuleConstant.FLOW_HDBR_NAME);
        meterHdbr.setFlowCode(FlowModuleConstant.FLOW_HDBR);
        meterHdbr.setFlowCount(flowHdbr.toString() + UnitConstant.FLOWUNIT);
        meterHdbr.setFlowCountFormat(NumberFormatUtil.formatData(flowHdbr) + UnitConstant.FLOWUNIT);
        allCurrentData.add(meterHdbr);
        return allCurrentData;
    }


    /**
     * 描述: 计算今日水表流量
     * @author: songguang.jiao
     * 创建时间:  2020年5月7日 上午11:34:59
     * 版本: V1.0
     */
    private Map<String, MeterFlow> calculate(String moduleId) {
        // 根据统计项目id和统计变量查询变量信息和对应资产信息
        List<EquipmentVariableVO> datas = this.moduleVarService.queryAllEquipAndVarByModuleId(moduleId);
        Map<String, EquipmentVariableVO> config = new HashMap<>(datas.size());
        Map<String, MeterFlow> meterMap = new HashMap<>(datas.size());
        Class<MeterFlow> clazz = MeterFlow.class;
        for (EquipmentVariableVO vo : datas) {
            config.put(vo.getVariableName(), vo);
            MeterFlow one = meterMap.get(vo.getEquipmentId());
            if (null == one) {
                one = new MeterFlow();
                meterMap.put(vo.getEquipmentId(), one);
            }
            one.setEquipmentId(vo.getEquipmentId());
            String column = vo.getTableColumn();
            String fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column);
            Field field = null;
            String valueEntityStr = this.stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstantV2.REDISDATAKEY, vo.getVariableName()));
            if (StringUtils.isEmpty(valueEntityStr)) {
                continue;
            }
            ValueEntity entity = JSON.parseObject(valueEntityStr, ValueEntity.class);
            String value = entity.getValue();
            BigDecimal valueBg = new BigDecimal(value);
            int scale = vo.getScale() > 0 ? vo.getScale() : ScaleConstant.LL_SCALE_STORE;
            try {
                field = clazz.getDeclaredField(fieldName);
                if (null == field) {
                    continue;
                }
                field.setAccessible(true);
                field.set(one, valueBg.setScale(scale, RoundingMode.HALF_UP).toString());
                meterMap.put(vo.getEquipmentId(), one);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        Collection<MeterFlow> meters = meterMap.values();
        List<MeterFlow> needSaves = new ArrayList<MeterFlow>();
        Date now = new Date();
        //查询昨日统计时间
        Map<String, MeterFlow> mapYesterDayData = meterFlowService.queryByDate(LocalDate.now().minusDays(1).toString());
        for (MeterFlow one : meters) {
            String equipmentId = one.getEquipmentId();
            //如果采集的正负累计流量为空的话,就默认采集值为0
            if (StringUtils.isEmpty(one.getPositiveFlow())) {
                one.setPositiveFlow("0");
            }
            if (StringUtils.isEmpty(one.getNavigateFlow())) {
                one.setNavigateFlow("0");
            }
            BigDecimal posBg = new BigDecimal(one.getPositiveFlow());
            BigDecimal nvgBg = new BigDecimal(one.getNavigateFlow());
            BigDecimal netBg = posBg.add(nvgBg);
            // 查询数据库前一天的用户用水数据
            MeterFlow todayMeterFlow = new MeterFlow();
            todayMeterFlow.setNavigateFlow(nvgBg.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
            todayMeterFlow.setPositiveFlow(posBg.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
            todayMeterFlow.setEquipmentId(equipmentId);
            todayMeterFlow.setStaticsDate(now);
            todayMeterFlow.setNetFlow(netBg.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
            todayMeterFlow.setPositiveFlowDay("0");
            todayMeterFlow.setNavigateFlowDay("0");
            todayMeterFlow.setNetFlowDay("0");
            todayMeterFlow.setStaticsDate(now);
            todayMeterFlow.setStaticsTime(now);

            BigDecimal positiveFlowDay = BigDecimal.ZERO;
            BigDecimal navigateFlowDay = BigDecimal.ZERO;
            BigDecimal netFlowDay = BigDecimal.ZERO;

            //获取昨天的数据
            MeterFlow meter = mapYesterDayData.get(equipmentId);
            //如果能取到值就说明该水表有历史记录值
            if (null != meter) {
                // 昨日正向累计流量
                String forValueOld = meter.getPositiveFlow();
                BigDecimal yestodayPosBg = new BigDecimal(forValueOld);
                // 昨日正向累计流量
                String revValueOld = meter.getNavigateFlow();
                BigDecimal yestodayNvgBg = new BigDecimal(revValueOld);
                //正向累计量和0做比较，如果不大于0则认为没取到数据，则正向累计量需要用前一天的数据
                //负向累计量和0做比较，如果不大于0则认为没取到数据，则负向累计量需要用前一天的数据
                int aa = posBg.compareTo(BigDecimal.ZERO);
                int bb = nvgBg.compareTo(BigDecimal.ZERO);
                if (aa == 0 && bb == 0) {
                    posBg = yestodayPosBg;
                    nvgBg = yestodayNvgBg;
                }
                netBg = posBg.add(nvgBg);
                positiveFlowDay = posBg.subtract(yestodayPosBg);
                navigateFlowDay = nvgBg.subtract(yestodayNvgBg);
                netFlowDay = positiveFlowDay.add(navigateFlowDay);
                //负向累计
                todayMeterFlow.setNavigateFlow(nvgBg.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
                //正向累计
                todayMeterFlow.setPositiveFlow(posBg.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
                //净用累计
                todayMeterFlow.setNetFlow(netBg.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
                //正向当天
                todayMeterFlow.setPositiveFlowDay(positiveFlowDay.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
                //负向当天
                todayMeterFlow.setNavigateFlowDay(navigateFlowDay.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
                //净用当天
                todayMeterFlow.setNetFlowDay(netFlowDay.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
            }
            needSaves.add(todayMeterFlow);
        }

        //转成map
        Map<String, MeterFlow> mapData = new HashMap<>(needSaves.size());
        for (MeterFlow meterFlow : needSaves) {
            mapData.put(meterFlow.getEquipmentId(), meterFlow);
        }
        return mapData;
    }

    /**
     * 描述: 只产生河东水厂数据  河东水厂跟水表变量取值不同
     * @author: songguang.jiao
     * 创建时间:  2020年4月14日 下午5:08:35
     * 版本: V1.0
     */
    private Map<String, MeterFlow> calculateHd(String moduleId) {
        // 查询所有水表和水表变量信息
        // 根据统计项目id和统计变量查询变量信息和对应资产信息
        List<EquipmentVariableVO> datas = this.moduleVarService.queryAllEquipAndVarByModuleId(moduleId);
        Map<String, EquipmentVariableVO> config = new HashMap<>(datas.size());
        Map<String, MeterFlow> meterMap = new HashMap<>(datas.size());
        Class<MeterFlow> clazz = MeterFlow.class;
        for (EquipmentVariableVO vo : datas) {
            //不是河东水厂的暂时不统计
          //  if (EquipIdConstant.HDSC.equals(vo.getEquipmentId())) {
                config.put(vo.getVariableName(), vo);
                MeterFlow one = meterMap.get(vo.getEquipmentId());
                if (null == one) {
                    one = new MeterFlow();
                    meterMap.put(vo.getEquipmentId(), one);
                }
                one.setEquipmentId(vo.getEquipmentId());
                String column = vo.getTableColumn();
                String fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column);
                Field field = null;
                String valueEntityStr = this.stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstantV2.REDISDATAKEY, vo.getVariableName()));
                if (StringUtils.isEmpty(valueEntityStr)) {
                    continue;
                }
                ValueEntity entity = JSON.parseObject(valueEntityStr, ValueEntity.class);
                String value = entity.getValue();
                BigDecimal valueBg = new BigDecimal(value);
                int scale = vo.getScale() > 0 ? vo.getScale() : ScaleConstant.LL_SCALE_STORE;
                try {
                    field = clazz.getDeclaredField(fieldName);
                    if (null == field) {
                        continue;
                    }
                    field.setAccessible(true);
                    field.set(one, valueBg.setScale(scale, RoundingMode.HALF_UP).toString());
                    meterMap.put(vo.getEquipmentId(), one);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
           // } else {
          //      continue;
           // }
        }

        Collection<MeterFlow> meters = meterMap.values();
        List<MeterFlow> needSaves = new ArrayList<MeterFlow>();
        Date now = new Date();
        for (MeterFlow one : meters) {
            String equipmentId = one.getEquipmentId();
            //如果没有河东再生水累计流量就设置为空
            if (StringUtils.isEmpty(one.getNetFlow())) {
                one.setNetFlow("0");
            }
            BigDecimal netBg = new BigDecimal(one.getNetFlow());
            // 查询数据库前一天的用户用水数据
            MeterFlow todayMeterFlow = new MeterFlow();
            todayMeterFlow.setNavigateFlow("0");
            todayMeterFlow.setPositiveFlow("0");
            todayMeterFlow.setEquipmentId(equipmentId);
            todayMeterFlow.setStaticsDate(now);
            todayMeterFlow.setNetFlow(netBg.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
            todayMeterFlow.setPositiveFlowDay("0");
            todayMeterFlow.setNavigateFlowDay("0");
            todayMeterFlow.setNetFlowDay("0");
            todayMeterFlow.setStaticsDate(now);
            todayMeterFlow.setStaticsTime(now);

            BigDecimal netFlowDay = BigDecimal.ZERO;
            //获取昨天的数据
            String preDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            QueryWrapper<MeterFlow> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(MeterFlow::getEquipmentId, equipmentId).eq(MeterFlow::getStaticsDate, preDate);
            MeterFlow meter = meterFlowService.getOne(queryWrapper);
            //如果能取到值就说明该水表有历史记录值
            if (null != meter) {
                netFlowDay = new BigDecimal(todayMeterFlow.getNetFlow()).subtract(new BigDecimal(meter.getNetFlow()));
                //净用当天
                todayMeterFlow.setNetFlowDay(netFlowDay.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toString());
            }
            needSaves.add(todayMeterFlow);
        }
        //转成map
        Map<String, MeterFlow> mapData = new HashMap<>(needSaves.size());
        for (MeterFlow meterFlow : needSaves) {
            mapData.put(meterFlow.getEquipmentId(), meterFlow);
        }
        return mapData;
    }
}
