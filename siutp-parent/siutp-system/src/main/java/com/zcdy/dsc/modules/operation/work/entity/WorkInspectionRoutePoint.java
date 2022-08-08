package com.zcdy.dsc.modules.operation.work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @description: 巡检路线关联巡检点
 * @author: songguang.jiao
 * @date: 2020-07-01
 */
@Data
@TableName("work_inspection_route_point")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "work_inspection_route_point", description = "巡检路线关联巡检点")
public class WorkInspectionRoutePoint {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 巡检点id
     */
    @ApiModelProperty(value = "巡检点id")
    private java.lang.String pointId;
    /**
     * 巡检路线id
     */
    @ApiModelProperty(value = "巡检路线id")
    private java.lang.String routeId;
}
