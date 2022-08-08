package com.zcdy.dsc.modules.operation.equipment.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Classname: siutp-parent
 * 创建时间： 2020/2/6 16:04
 * @author：  * 描述:
 */
@Data
@ApiModel(value="OptGoodsApplyVo", description="OptGoodsApplyVo")
public class OptGoodsApplyVo {
    /**id*/
    @ApiModelProperty(value = "id")
    private String id;
    /**姓名*/
    @ApiModelProperty(value = "申请人")
    private java.lang.String createBy;
    /**申请时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "申请时间")
    private java.util.Date createTime;
    /**借用工具*/
    @ApiModelProperty(value = "借用工具")
    private java.lang.String useTool;
    /**紧急程度*/
    @ApiModelProperty(value = "紧急程度")
    private java.lang.String emergenText;
    /**紧急程度*/
    @ApiModelProperty(value = "紧急程度的value值")
    private java.lang.String emergentValue;
    /**期望领用时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "期望领用时间")
    private java.util.Date wantedTime;
    /**状态*/
    @ApiModelProperty(value = "状态")
    private java.lang.String verifyStatus;
    /**状态*/
    @ApiModelProperty(value = "状态的value值")
    private java.lang.String verifyStatusValue;
    /**申请事由*/
    @ApiModelProperty(value = "申请事由")
    private java.lang.String reason;
}
