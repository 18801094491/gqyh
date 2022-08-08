package com.zcdy.dsc.modules.rdp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author 在信汇通
 * @Description: RDP水文旭日图数据返回vo
 * @Date: 2020-12-11 15:52
 * @version: 1.0
 */
@ApiModel(value="HydrologysVo",description="RDP水文旭日图数据返回vo")
@Data
public class HydrologyVo {
    /** 水文设备编号 */
    @Excel(name = "水文设备编号", width = 15)
    @ApiModelProperty(value = "水文设备编号")
    private String no;
    /** 水文设备参数 */
    @Excel(name = "水文设备参数", width = 15)
    @ApiModelProperty(value = "水文设备参数")
    private String value;
}
