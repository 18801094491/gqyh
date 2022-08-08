package com.zcdy.dsc.modules.settle.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zcdy.dsc.constant.ScaleConstant;
import com.zcdy.dsc.modules.operation.equipment.constant.EquipIdConstant;
import com.zcdy.dsc.modules.operation.equipment.entity.MeterFlow;
import com.zcdy.dsc.modules.operation.equipment.service.MeterFlowService;

/**
 * 昨日用水量统计计算 保留1位小数,无单位
 * 
 * @author songguang.jiao
 * @date 2020/05/11 09:44:39
 */
@Service("flowCountYesterDayLogic")
public class FlowCountYesterDayLogic {

    @Resource
    private MeterFlowService meterFlowService;

    /**
     * 默认查询不到数据时显示为 --
     */
    private String initFlow = "--";

    /**
     * 描述: 昨日行政区用水 @author: songguang.jiao 创建时间: 2020年4月30日 下午3:48:21 版本: V1.0
     */
    public String getFlowXzq() {
        // 查询昨天流量计之和,7标跟2标
        String flowXzq = initFlow;
        BigDecimal flowCountYes = BigDecimal.ZERO;
        QueryWrapper<MeterFlow> queryYesterday = new QueryWrapper<>();
        queryYesterday.lambda().in(MeterFlow::getEquipmentId, EquipIdConstant.GLL_2_01, EquipIdConstant.GLL_7_01)
            .eq(MeterFlow::getStaticsDate, LocalDate.now().minusDays(1));
        List<MeterFlow> yesterdayFlows = meterFlowService.list(queryYesterday);
        if (yesterdayFlows.size() < 1) {
            return flowXzq;
        }
        for (MeterFlow meterFlow : yesterdayFlows) {
            flowCountYes = flowCountYes.add(new BigDecimal(meterFlow.getNetFlowDay()));
        }
        flowXzq = flowCountYes.setScale(ScaleConstant.LL_SCALE_SHOW, RoundingMode.HALF_UP).toString();
        return flowXzq;
    }

    /**
     * 描述: 昨日行政区入楼 @author: songguang.jiao 创建时间: 2020年4月30日 下午4:10:34 版本: V1.0
     */
    public String getFlowXzrl() {
        String flowXzrl = initFlow;
        String yestodayRulouValue =
            meterFlowService.queryMeterRsLh(EquipIdConstant.METER_RULOU, LocalDate.now().minusDays(1));
        if (!StringUtils.isEmpty(yestodayRulouValue)) {
            flowXzrl = new BigDecimal(yestodayRulouValue).setScale(ScaleConstant.LL_SCALE_SHOW, RoundingMode.HALF_UP)
                .toString();
        }
        return flowXzrl;
    }

    /**
     * 描述: 昨日行政区绿化 @author: songguang.jiao 创建时间: 2020年4月30日 下午4:46:00 版本: V1.0
     */
    public String getFlowXzlh() {
        String flowXzlh = initFlow;
        if (!this.getFlowXzq().equals(initFlow) && !this.getFlowXzrl().equals(initFlow)) {
            flowXzlh = new BigDecimal(this.getFlowXzq()).subtract(new BigDecimal(this.getFlowXzrl())).toString();
        }
        return flowXzlh;
    }

    /**
     * 描述: 昨日河东水厂 @author: songguang.jiao 创建时间: 2020年4月30日 下午4:47:57 版本: V1.0
     */
    public String getFlowHd() {
        String flowHd = initFlow;
        MeterFlow meterFlow = meterFlowService
            .getOne(Wrappers.lambdaQuery(new MeterFlow()).eq(MeterFlow::getEquipmentId, EquipIdConstant.HDSC)
                .eq(MeterFlow::getStaticsDate, LocalDate.now().minusDays(1)));
        if (null != meterFlow) {
            flowHd = new BigDecimal(meterFlow.getNetFlowDay())
                .setScale(ScaleConstant.LL_SCALE_SHOW, RoundingMode.HALF_UP).toString();
        }
        return flowHd;
    }

    /**
     * 描述: 昨日镜河补水 @author: songguang.jiao 创建时间: 2020年4月30日 下午4:49:00 版本: V1.0
     */
    public String getFlowFzg() {
        String flowFzg = initFlow;
        MeterFlow meterFlow = meterFlowService
            .getOne(Wrappers.lambdaQuery(new MeterFlow()).eq(MeterFlow::getEquipmentId, EquipIdConstant.FZG_WSB05)
                .eq(MeterFlow::getStaticsDate, LocalDate.now().minusDays(1)));
        if (meterFlow != null) {
            flowFzg = new BigDecimal(meterFlow.getNetFlowDay())
                .setScale(ScaleConstant.LL_SCALE_SHOW, RoundingMode.HALF_UP).toString();
        }
        return flowFzg;
    }

    /**
     * 描述: 昨日华电北燃 @author: songguang.jiao 创建时间: 2020年4月30日 下午4:49:00 版本: V1.0
     */
    public String getFlowHdbr() {
        String flowHdbr = initFlow;
        MeterFlow meterFlow = meterFlowService
            .getOne(Wrappers.lambdaQuery(new MeterFlow()).eq(MeterFlow::getEquipmentId, EquipIdConstant.HBDR_WSB01)
                .eq(MeterFlow::getStaticsDate, LocalDate.now().minusDays(1)));
        if (meterFlow != null) {
            flowHdbr = new BigDecimal(meterFlow.getNetFlowDay())
                .setScale(ScaleConstant.LL_SCALE_SHOW, RoundingMode.HALF_UP).toString();
        }
        return flowHdbr;
    }

}
