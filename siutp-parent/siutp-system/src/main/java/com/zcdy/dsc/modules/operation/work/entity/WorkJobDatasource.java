package com.zcdy.dsc.modules.operation.work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @description: 工单数据项
 * @author: 智能无人硬核心项目组
 * @date: 2020-06-24
 * @version: V1.0
 */
@Data
@TableName("work_job_datasource")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "work_job_datasource", description = "工单数据项")
public class WorkJobDatasource {

    /**
     * 主键
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 工单模板id
     */
    @ApiModelProperty(value = "工单模板id")
    private java.lang.String tplId;
    /**
     * 数据项名称
     */
    @ApiModelProperty(value = "数据项名称")
    private java.lang.String dataName;
    /**
     * 父id
     */
    @ApiModelProperty(value = "父id")
    private java.lang.String parentId;
    /**
     * 是否需要录入  1-是,0-否
     */
    @ApiModelProperty(value = "是否需要录入  1-是,0-否")
    private java.lang.String needEnter;
    /**
     * 数据类型 0-文本,1-多行文本,2-多选,3-单选,4-日期
     */
    @ApiModelProperty(value = "数据类型 0-文本,1-多行文本,2-多选,3-单选,4-日期")
    private java.lang.String dataType;
    /**
     * 是否必填 0-否,1-是
     */
    @ApiModelProperty(value = "是否必填 0-否,1-是")
    private java.lang.String needRequired;
    /**
     * 是否必填 0-否,1-是
     */
    @ApiModelProperty(value = "是否叶子节点 0-否,1-是")
    private Short isLeaf;
    /**
     * 数据项类别 0-根数据项,1-子数据项
     */
    @ApiModelProperty(value = "数据项类别 0-根数据项,1-子数据项")
    private java.lang.String dataCategory;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private java.lang.String dataOrder;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private java.lang.String dataUnit;
    /**
     * 创建人，派单人
     */
    @ApiModelProperty(value = "创建人，派单人")
    private java.lang.String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
    /**
     * 删除标识0-正常,1-已删除
     */
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
    private java.lang.Integer delFlag;
}
