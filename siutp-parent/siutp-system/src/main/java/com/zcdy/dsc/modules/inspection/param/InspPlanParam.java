package com.zcdy.dsc.modules.inspection.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zcdy.dsc.common.api.param.AbstractPageParam;
import com.zcdy.dsc.modules.inspection.entity.InspPlan;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 巡检计划
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@ApiModel(value="work_list_inspection_plan", description="巡检计划参数列表")
public class InspPlanParam extends AbstractPageParam {
    /**id*/
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**名称*/
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
    /**区域id*/
    @ApiModelProperty(value = "区域id")
    private java.lang.String areaId;
    /**路线id*/
    @ApiModelProperty(value = "路线id")
    private java.lang.String routeId;
    /**编号*/
    @ApiModelProperty(value = "编号")
    private java.lang.String code;
    /**删除标识0-正常,1-已删除*/
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
    private java.lang.Integer delFlag;
}
