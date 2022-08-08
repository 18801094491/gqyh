package com.zcdy.dsc.modules.rdp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;
/**
 * @author 在信汇通
 * @Description: RDP水质旭日图最底层Vo
 * @Date: 2020-12-11 15:52
 * @version: 1.0
 */
@Getter
@Setter
@ApiModel(value="SunriseChartChildrenVo",description="RDP水质旭日图最底层Vo")
public class SunriseChartChildrenVo {
    /** 旭日图名字 */
    @Excel(name = "旭日图名字", width = 15)
    @ApiModelProperty(value = "旭日图名字")
    private String name;
    /** 旭日图数据 */
    @Excel(name = "旭日图数据", width = 15)
    @ApiModelProperty(value = "旭日图数据")
    private Integer value = 1;
}
