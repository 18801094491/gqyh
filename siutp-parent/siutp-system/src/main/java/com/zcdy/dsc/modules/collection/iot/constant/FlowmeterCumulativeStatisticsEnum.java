package com.zcdy.dsc.modules.collection.iot.constant;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BiConsumer;

import com.zcdy.dsc.modules.collection.iot.vo.FlowmeterCumulativeStatisticsVo;

/**
 * @author tangchao
 * @since 2020-5-6 18:13:38
 */

public enum FlowmeterCumulativeStatisticsEnum {
    //流量计瞬时流量
    A16A20("A16A20","流量计瞬时流量", (byte)3,FlowmeterCumulativeStatisticsVo::setInstantaneousFlow),
    //正累计流量
    A16A21("A16A21","正累计流量", (byte)0, FlowmeterCumulativeStatisticsVo::setPositiveTotalFlow),
    A16A22("A16A22","流量计流速", (byte)3, FlowmeterCumulativeStatisticsVo::setFlowRate),
    A16A23("A16A23","负累计流量", (byte)0, FlowmeterCumulativeStatisticsVo::setNegativeTotalFlow),
    A16A24("A16A24","累计流量", (byte)0, FlowmeterCumulativeStatisticsVo::setTotalFlow),
    A16A25("A16A25","清水池液位", (byte)0, FlowmeterCumulativeStatisticsVo::setWaterLevel);

    private FlowmeterCumulativeStatisticsEnum(String type, String title, byte scale, BiConsumer<FlowmeterCumulativeStatisticsVo, String> stringConsumer){
        this.type = type;
        this.title = title;
        this.scale = scale;
        this.stringConsumer = stringConsumer;
    }

    public void setValue(FlowmeterCumulativeStatisticsVo f, String s){
        this.stringConsumer.accept(f, new BigDecimal(s).setScale(this.scale, RoundingMode.HALF_UP).toString());
    }

    private String type;
    private String title;
    private byte scale;
    private BiConsumer<FlowmeterCumulativeStatisticsVo, String> stringConsumer;



}
