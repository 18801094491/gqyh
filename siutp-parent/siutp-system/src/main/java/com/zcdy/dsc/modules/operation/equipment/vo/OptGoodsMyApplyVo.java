package com.zcdy.dsc.modules.operation.equipment.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Classname: siutp-parent
 * 创建时间： 2020/2/6 16:04
 * @author：  * 描述: 查看自己申请情况  返回vo类
 */
@Data
@ApiModel(value="OptGoodsMyApplyVo", description="查看自己申请情况")
public class OptGoodsMyApplyVo {
    /**id*/
    @ApiModelProperty(value = "id")
    private String id;
    /**借用人*/
    @ApiModelProperty(value = "createBy")
    private String createBy;
    /**申请时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "申请时间")
    private java.util.Date createTime;
    /**期望领用时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "期望领用时间")
    private java.util.Date wantedTime;
    /**预计归还时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "预计归还时间")
    private java.util.Date expectbackTime;
    /**借用工具*/
    @ApiModelProperty(value = "借用工具")
    private String useTool;
    /**状态*/
    @ApiModelProperty(value = "状态")
    private String verifyStatus;
    /**状态*/
    @ApiModelProperty(value = "状态的value值")
    private String verifyStatusValue;
    /**申请事由*/
    @ApiModelProperty(value = "申请事由")
    private String reason;
}
