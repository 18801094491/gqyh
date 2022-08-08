package com.zcdy.dsc.modules.operation.work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @description: 工单录入-子项
 * @author: songguang.jiao
 * @date: 2020-07-16
 */
@Data
@TableName("work_job_record_item")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "work_job_record_item", description = "工单录入-子项")
public class WorkJobRecordItem {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 录入单id
     */
    @ApiModelProperty(value = "录入单id")
    private java.lang.String recordId;
    /**
     * 数据项名称
     */
    @ApiModelProperty(value = "数据项名称")
    private java.lang.String itemName;
    /**
     * 数据项值
     */
    @ApiModelProperty(value = "数据项值")
    private java.lang.String itemValue;
    /**
     * 选项排序
     */
    @ApiModelProperty(value = "选项排序")
    private java.lang.String itemOrder;

    @TableField(exist = false)
    private java.lang.String itemId;

}
