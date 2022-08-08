package com.zcdy.dsc.modules.operation.upkeep.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 设备养护通知vo
 *
 * @author songguang.jiao
 * @date 2020/6/815:39
 */
@Getter
@Setter
@ApiModel("KeepAdviseVo")
public class KeepAdviseVo {
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private java.lang.String equipmentId;
    /**
     * 保养记录id
     */
    @ApiModelProperty(value = "保养记录id")
    private java.lang.String id;
    /**
     * 设备编码
     */
    @ApiModelProperty(value = "设备编号")
    private java.lang.String equipmentSn;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private java.lang.String equipmentName;
    /**
     * 所属标段
     */
    @ApiModelProperty(value = "所属标段")
    private java.lang.String equipmentSectionName;
    /**
     * 所在位置
     */
    @ApiModelProperty(value = "所在位置")
    private java.lang.String equipmentLocation;
    /**
     * 计划日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "计划日期")
    private java.util.Date planDate;
}
