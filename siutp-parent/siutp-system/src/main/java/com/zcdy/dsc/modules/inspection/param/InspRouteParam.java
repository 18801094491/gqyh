package com.zcdy.dsc.modules.inspection.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zcdy.dsc.common.api.param.AbstractPageParam;
import com.zcdy.dsc.modules.inspection.entity.InspRoute;
import com.zcdy.dsc.modules.inspection.entity.InspRoutePoint;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description: 巡检路线
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@ApiModel(value="work_list_inspection_route", description="巡检路线参数列表")
public class InspRouteParam extends AbstractPageParam {
    /**id*/
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**所属区域*/
    @ApiModelProperty(value = "所属区域")
    private java.lang.String areaId;
    /**线路名称*/
    @ApiModelProperty(value = "线路名称")
    private java.lang.String name;
    /**线路编号*/
    @ApiModelProperty(value = "线路编号")
    private java.lang.String code;
    /**删除标识0-正常,1-已删除*/
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
    private java.lang.Integer delFlag;
}
