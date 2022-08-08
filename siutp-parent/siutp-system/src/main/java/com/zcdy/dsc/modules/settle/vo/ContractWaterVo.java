package com.zcdy.dsc.modules.settle.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : songguang.jiao
 */
@Getter
@Setter
public class ContractWaterVo {
    /**
     * 绑定关系id
     */
    private String id;

    /**
     * 水表编码
     */
    private String equipmentSn;

    /**
     * 水表名称
     */
    private String equipmentName;

    /**
     * 规则ID
     */
    private String ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

}
