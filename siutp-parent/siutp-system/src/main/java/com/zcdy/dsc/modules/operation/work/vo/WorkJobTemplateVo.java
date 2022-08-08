package com.zcdy.dsc.modules.operation.work.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author songguang.jiao
 * @date 2020/6/2411:39
 */
@Getter
@Setter
@ApiModel("WorkJobTemplateVo")
public class WorkJobTemplateVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private java.lang.String tplName;
    /**
     * 模板类型
     */
    @ApiModelProperty(value = "模板类型")
    private java.lang.String tplType;
    /**
     * 模板类型
     */
    @ApiModelProperty(value = "模板类型名称")
    private java.lang.String tplTypeName;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
    /**
     * 模板状态 1-启用,0-停用
     */
    @ApiModelProperty(value = "模板状态 1-启用,0-停用")
    private java.lang.String tplStatus;
    /**
     * 模板状态 1-启用,0-停用
     */
    @ApiModelProperty(value = "模板状态 1-启用,0-停用")
    private java.lang.String tplStatusName;
    /**
     * 创建人，派单人
     */
    @ApiModelProperty(value = "创建人，派单人")
    private java.lang.String createUser;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
}
