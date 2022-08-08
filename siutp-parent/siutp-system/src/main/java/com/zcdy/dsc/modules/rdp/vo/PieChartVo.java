package com.zcdy.dsc.modules.rdp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author 在信汇通
 * @Description: RDP水质饼图Vo
 * @Date: 2021-07-29 17:52
 * @version: 1.0
 */
@Getter
@Setter
@ApiModel(value="PieChartVo",description="RDP水质饼图Vo")
public class PieChartVo {
    /** 名字 */
    @Excel(name = "名字", width = 15)
    @ApiModelProperty(value = "名字")
    private String name;
    /** 数据 */
    @Excel(name = "数据", width = 15)
    @ApiModelProperty(value = "数据")
    private Double value;
}
