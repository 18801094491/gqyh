package com.zcdy.dsc.modules.settle.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author tangchao
 * @date 2020/5/13
 */
@Data
@ApiModel("结算管理")
public class SettlementStatisticsDetailVo {
    @ApiModelProperty("总应缴水费(元)")
    private String totalCost;
    @ApiModelProperty("总用水")
    private String totalFlow;
    @ApiModelProperty("明细")
    private List<SettlementStatisticsVo> list;
}
