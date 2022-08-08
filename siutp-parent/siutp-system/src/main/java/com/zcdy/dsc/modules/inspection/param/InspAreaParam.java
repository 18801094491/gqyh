package com.zcdy.dsc.modules.inspection.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zcdy.dsc.common.api.param.AbstractPageParam;
import com.zcdy.dsc.modules.inspection.entity.InspArea;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 巡检区域
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@ApiModel(value="work_list_inspection_area", description="巡检区域参数列表")
public class InspAreaParam extends AbstractPageParam {
    /**id*/
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**区域名称*/
    @ApiModelProperty(value = "区域名称")
    private java.lang.String name;
    /**区域编号*/
    @ApiModelProperty(value = "区域编号")
    private java.lang.String code;
    /**删除标识0-正常,1-已删除*/
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
    private java.lang.Integer delFlag;
}
