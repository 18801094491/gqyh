package com.zcdy.dsc.modules.operation.equipment.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Classname: siutp-parent
 * 创建时间： 2020/2/6 16:04
 * @author：  * 描述:
 */
@Data
@ApiModel(value="OptGoodsApplyAddVo", description="货物申领申请信息")
public class OptGoodsApplyParamVo {
    /**id*/
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**期望领用时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @ApiModelProperty(value = "期望领用时间")
    private java.util.Date wantedTime;
    /**预计归还时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @ApiModelProperty(value = "预计归还时间")
    private java.util.Date expectbackTime;
    /**紧急程度*/
    @ApiModelProperty(value = "紧急程度")
    private java.lang.String emergentLevel;
    /**申请事由*/
    @ApiModelProperty(value = "申请事由")
    private java.lang.String reason;
    /**申请工具集合*/
    @ApiModelProperty(value = "申请工具集合")
    private List<OptApplyItemVo> applyItem;

}
