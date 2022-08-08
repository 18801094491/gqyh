package com.zcdy.dsc.modules.operation.work.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 上报问题vo类
 *
 * @author songguang.jiao
 * @date 2020/6/417:19
 */
@Getter
@Setter
@ApiModel(value = "ProblemReportVo")
public class ProblemReportVo {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 问题编号
     */
    @ApiModelProperty(value = "问题编号")
    private java.lang.String problemSn;
    /**
     * 问题名称
     */
    @ApiModelProperty(value = "问题名称")
    private java.lang.String problemName;
    /**
     * 问题类型
     */
    @ApiModelProperty(value = "问题类型code")
    private java.lang.String problemType;
    /**
     * 问题类型
     */
    @ApiModelProperty(value = "问题类型")
    private java.lang.String problemTypeName;

    /**
     * 问题描述
     */
    @ApiModelProperty(value = "问题描述")
    private String problemDescription;
    /**
     * 问题设备信息
     */
    @ApiModelProperty(value = "问题设备信息")
    private java.lang.String equipmentId;
    /**
     * 问题设备信息
     */
    @ApiModelProperty(value = "问题设备信息")
    private java.lang.String equipmentName;
    /**
     * 问题状态
     */
    @ApiModelProperty(value = "问题状态code")
    private java.lang.String problemStatus;
    /**
     * 问题状态
     */
    @ApiModelProperty(value = "问题状态")
    private java.lang.String problemStatusName;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createUser;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
}
