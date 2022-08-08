package com.zcdy.dsc.modules.operation.work.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 点位下拉选
 *
 * @author songguang.jiao
 * @date 2020/7/2 10:17
 */
@Getter
@Setter
@ApiModel("WorkPointDropdown")
public class WorkPointDropdown {

    /**
     * dataId
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 设备id
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty("设备id")
    private String equipmentId;
    /**
     * 巡检设备名称
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty("巡检设备名称")
    private String equipmentName;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private java.lang.String longitude;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private java.lang.String latitude;
}
