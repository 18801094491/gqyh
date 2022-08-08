package com.zcdy.dsc.modules.settle.entity;

import lombok.Data;

/**
 * @author tangchao
 * @date 2020/5/12
 */
@Data
public class SettleChecklistPage {
    private String id;
    private String customerName;
    private String customerTypeName;
    private String settleDate;
    private String totalCost;
    private String realName;
}
