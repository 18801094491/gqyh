package com.zcdy.dsc.modules.operation.work.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 工单巡检点入参
 *
 * @author songguang.jiao
 * @date 2020/7/9/0009  9:35:47
 */
@Getter
@Setter
@ApiModel("WorkPointParam")
public class WorkPointParam {

    /**
     * 工单id
     */
    @ApiModelProperty("工单id")
    @NotBlank(message = "工单id不能为空")
    private String workJobId;

    /**
     * 巡检点名称
     */
    @ApiModelProperty(value = "巡检点名称")
    @NotBlank(message = "巡检点名称不能为空")
    private java.lang.String name;
    /**
     * 注意事项
     */
    @ApiModelProperty(value = "注意事项")
    private java.lang.String attention;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
    /**
     * 数据模板
     */
    @ApiModelProperty(value = "数据模板")
    @NotBlank(message = "数据模板不能为空")
    private java.lang.String tplId;
    /**
     * 类别
     */
    @ApiModelProperty(value = "类别")
    @NotBlank(message = "数据类别不能为空")
    private java.lang.String category;
    /**
     * 对应数据id(告警id,设备id,问题id,点位为空)
     */
    @ApiModelProperty(value = "对应数据id(告警id,设备id,问题id,点位为空)")
    private java.lang.String dataId;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    @NotBlank(message = "经度不能为空")
    private java.lang.String longitude;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    @NotBlank(message = "纬度不能为空")
    private java.lang.String latitude;
}
