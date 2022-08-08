package com.zcdy.dsc.modules.settle.vo;

import lombok.Data;

/**
 * @author tangchao
 * @date 2020/5/25
 */
@Data
public class AnnualStatisticalRowVo {
    private String customerId;
    private String customerName;
    private String statisticYear;
    private String statisticMonth;
    private String monthTotalFlow;
    private String monthTotalCost;
}
