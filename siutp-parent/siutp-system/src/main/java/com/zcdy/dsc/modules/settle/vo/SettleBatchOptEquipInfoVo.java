package com.zcdy.dsc.modules.settle.vo;

import lombok.Data;

/**
 * @author tangchao
 * @date 2020-5-8 15:04:55
 */
@Data
public class SettleBatchOptEquipInfoVo {
    /**
     * 设备名称
     */
    private String equipmentName;
    /**
     * 水表信息
     */
    private String equipmentInfo;
    /**
     * 设备id
     */
    private String equipmentId;
    /**
     * 合同id
     */
    private String contractId;
    /**
     * 生效日期
     */
    private String contractValidateDate;
    /**
     * 失效日期
     */
    private String contractInvalidateDate;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 客户id
     */
    private String customerId;
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
     * 区域id
     */
    private String equipmentDistrictId;
    private String districtId;
    /**
     * 区域名称
     */
    private String equipmentDistrictName;
    private String districtName;
    /**
     * 水表类型 0-周期性 1-非周期
     */
    private String equipmentType;
    /**
     * 变量id
     */
    private String variableId;
    /**
     * 变量名称
     */
    private String variableName;
    /**
     * 变量显示名
     */
    private String variableTitle;
    /**
     * 变量类型
     */
    private String variableType;
}
