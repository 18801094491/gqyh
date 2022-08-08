package com.zcdy.dsc.modules.datacenter.statistic.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author 在信汇通
 * @Description: 水文数据Vo类
 * @Date: 2020-12-11 15:52
 * @version: 1.0
 */
@ApiModel(value="HydrologysVo",description="水文数据Vo类")
@Data
public class HydrologysVo {
    /** 水文设备编号 */
    @Excel(name = "水文设备编号", width = 15)
    @ApiModelProperty(value = "水文设备编号")
    private String no;
    /** 水文设备类型 */
    @Excel(name = "水文设备类型", width = 15)
    @ApiModelProperty(value = "水文设备类型")
    private Integer type;
    /** 水文设备-检测液位 */
    @Excel(name = "水文设备-检测液位", width = 15)
    @ApiModelProperty(value = "水文设备-检测液位")
    private String detectionOfLiquidLevel;
    /** 水文设备-流速 */
    @Excel(name = "水文设备-流速", width = 15)
    @ApiModelProperty(value = "水文设备-流速")
    private String currentSpeed;
    /** 水文设备-雨量 */
    @Excel(name = "水文设备-雨量", width = 15)
    @ApiModelProperty(value = "水文设备-雨量")
    private String rainfall;
    /** 水文设备-底泥厚度 */
    @Excel(name = "水文设备-底泥厚度", width = 15)
    @ApiModelProperty(value = "水文设备-底泥厚度")
    private String sedimentThickness;
}
