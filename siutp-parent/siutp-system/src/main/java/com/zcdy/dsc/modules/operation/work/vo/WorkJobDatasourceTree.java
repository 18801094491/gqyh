package com.zcdy.dsc.modules.operation.work.vo;

import com.zcdy.dsc.modules.operation.work.entity.WorkJobDatasourceItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 工单数据项树
 *
 * @author songguang.jiao
 * @date 2020/6/2810:35
 */
@Getter
@Setter
@EqualsAndHashCode
@ApiModel("WorkJobDatasourceTree")
public class WorkJobDatasourceTree {

    /**
     * id
     */
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 树key
     */
    @ApiModelProperty(value = "树key")
    private java.lang.String key;

    /**
     * 生成树用
     */
    @ApiModelProperty(value = "value")
    private java.lang.String value;
    /**
     * 工单模板id
     */
    @ApiModelProperty(value = "工单模板id")
    private java.lang.String tplId;
    /**
     * 数据项名称
     */
    @ApiModelProperty(value = "数据项名称")
    private java.lang.String title;
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
    private java.lang.Short isLeaf;
    /**
     * 数据项类别 0-根数据项,1-子数据项
     */
    @ApiModelProperty(value = "数据项类别 0-根数据项,1-子数据项")
    private java.lang.String dataCategory;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private java.lang.String dataUnit;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private java.lang.String dataOrder;

    /**
     * 数据项子项
     */
    @ApiModelProperty("数据项子项")
    private List<WorkJobDatasourceItem> items;


    /**
     * 子节点
     */
    @ApiModelProperty("子节点")
    private List<WorkJobDatasourceTree> children;
}
