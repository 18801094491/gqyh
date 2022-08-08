package com.zcdy.dsc.modules.settle.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author : songguang.jiao
 */
@Getter
@Setter
public class DistrictWaterCustomerInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 区域名称
     */
    private String districtName;
    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户类型编码
     */
    private String customerType;

    /**
     * 客户类型名称
     */
    private String customerTypeName;

    /**
     * 用水用途
     */
    private String contractUse;

    /**
     * 当前水价
     */
    private BigDecimal price;

    /**
     * 设备id
     */
    private String equipmentId;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 设备编码
     */
    private String equipmentSn;

    /**
     * 上个月流量值
     */
    private String lastMonthFlow;

    /**
     * 当天流量值 redis读取
     */
    private String currentDayFlow;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 合同编号
     */
    private String contractSn;

    /**
     * 合同失效日期
     */
    private String endDate;

    /**
     * 合同有效期剩余时间
     */
    private Integer remainDay;
    /**
     * 是否警告 1 是  0 不
     */
    private Integer isWarning;
    /**
     * 正向累计流量变量名称
     */
    private String variableName;
}
