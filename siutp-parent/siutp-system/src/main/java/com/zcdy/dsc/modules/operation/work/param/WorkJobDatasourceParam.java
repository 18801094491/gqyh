package com.zcdy.dsc.modules.operation.work.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author songguang.jiao
 * @date 2020/6/2415:35
 */
@Getter
@Setter
@ApiModel("WorkJobDatasourceParam")
public class WorkJobDatasourceParam {
    /**
     * 主键
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 工单模板id
     */
    @NotBlank(message = "工单模板不能为空")
    @ApiModelProperty(value = "工单模板id")
    private java.lang.String tplId;
    /**
     * 数据项名称
     */
    @NotBlank(message = "数据项名称不能为空")
    @ApiModelProperty(value = "数据项名称")
    private java.lang.String dataName;
    /**
     * 父id
     */
    @ApiModelProperty(value = "父id")
    private java.lang.String parentId;
    /**
     * 数据项类别 0-根数据项,1-子数据项
     */
    @NotBlank(message = "数据项类别不能为空")
    @ApiModelProperty(value = "数据项类别 0-根数据项,1-子数据项")
    private java.lang.String dataCategory;
    /**
     * 是否需要录入  1-是,0-否
     */
    @NotBlank(message = "是否需要录入不能为空")
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
     * 子选项列表
     */
    @ApiModelProperty(value = "子选项列表")
    @Valid
    private List<DatasourceItem> items;

    /**
     * 子选项数据
     */
    @Getter
    @Setter
    @ApiModel("DatasourceItem")
    public static final class DatasourceItem {

        @ApiModelProperty("选项id")
        private String itemId;

        /**
         * 选项名称
         */
        @ApiModelProperty(value = "选项名称")
        private java.lang.String itemName;
        /**
         * 选项排序
         */
        @ApiModelProperty(value = "选项排序")
        private java.lang.String itemOrder;

    }
}
