package com.zcdy.dsc.modules.operation.work.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 描述: 第三方维修团队详情
 *
 * @author： songguang.jiao
 * 创建时间：  2020年1月16日 上午9:45:05
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value = "ThirdTeamVo", description = "第三方维修团队详情")
public class ThirdTeamVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 团队编号
     */
    @ApiModelProperty(value = "团队编号")
    private java.lang.String teamSn;
    /**
     * 团队名称
     */
    @Excel(name = "团队名称", width = 15)
    @ApiModelProperty(value = "团队名称")
    private java.lang.String teamName;
    /**
     * 协议起始日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "协议起始日期")
    private java.util.Date agreeStart;
    /**
     * 协议截至日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "协议截至日期")
    private java.util.Date agreeEnd;
    /**
     * 联系人姓名
     */
    @ApiModelProperty(value = "联系人姓名")
    private java.lang.String contactName;
    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private java.lang.String contactPhone;
    /**
     * 联系人职位
     */
    @ApiModelProperty(value = "联系人职位")
    private java.lang.String contactPosition;
    /**
     * 联系人职位code
     */
    @ApiModelProperty(value = "联系人职位code")
    private java.lang.String contactPositionCode;
    /**
     * 团队评级
     */
    @ApiModelProperty(value = "团队评级")
    private java.lang.String teamRating;
    /**
     * 团队评级code
     */
    @ApiModelProperty(value = "团队评级code")
    private java.lang.String teamRatingCode;
    /**
     * 资质附件路径
     */
    @ApiModelProperty(value = "资质附件路径")
    private java.lang.String fileUrl;
    /**
     * 资质附件名称
     */
    @ApiModelProperty(value = "资质附件名称")
    private java.lang.String fileName;

}
