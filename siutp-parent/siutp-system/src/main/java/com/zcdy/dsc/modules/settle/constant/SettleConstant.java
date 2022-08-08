package com.zcdy.dsc.modules.settle.constant;

/**
 * 结算常量
 * @author tangchao
 * @since 2020-5-11
 */
public interface SettleConstant {
    /**
     * 未结算
     */
    String STATUS_UNSETTLEMENT = "00";
    /**
     * 手工结算
     */
    String STATUS_MANUAL_SETTLEMENT = "01";
    /**
     * 自动结算
     */
    String STATUS_AUTO_SETTLEMENT = "02";
    /**
     * 初始化表底
     */
    String STATUS_INIT_SETTLEMENT = "03";
    /**
     * 周期水表
     */
    String EQUIP_IS_CYCLE = "0";
    /**
     * 非周期水表
     */
    String EQUIP_NOT_CYCLE = "1";
    /**
     * 自动结算人
     */
    String AUTO_SETTLE_PERSON = "zcdy";
}
