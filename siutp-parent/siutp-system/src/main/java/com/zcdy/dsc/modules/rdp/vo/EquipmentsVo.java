package com.zcdy.dsc.modules.rdp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

/**
 * @author 在信汇通
 * @Description: RDP水质旭日图Vo
 * @Date: 2020-12-11 15:52
 * @version: 1.0
 */
@Getter
@Setter
@ApiModel(value="SunriseChartVo",description="RDP设备Vo")
@Data
public class EquipmentsVo {
    /** 设备id */
    @Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
    private String id;
    /** 类型 */
    @Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
    private Integer type;
    /** 设备分类 */
    @Excel(name = "设备分类", width = 15)
    @ApiModelProperty(value = "设备分类")
    private String equipmentClass;
    /** 设备名字 */
    @Excel(name = "设备名字", width = 15)
    @ApiModelProperty(value = "设备名字")
    private String name;
    /** 设备参数列表 */
    @Excel(name = "设备参数列表", width = 15)
    @ApiModelProperty(value = "设备参数列表")
    private List<EquipmentInfo> equipmentInfoList;
}
