package com.zcdy.dsc.modules.map.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 河流分页查询参数
 * @author:  在信汇通
 * 创建时间:  2020年12月10日 上午9:28:54
 * 版本: V1.0
 */
@Getter
@Setter
@ApiModel(value="MapRiverParam",description="河流分页查询参数")
public class MapRiverParam extends AbstractPageParam {

    /**河流名称*/
    @ApiModelProperty(value = "河流名称")
    private String riverName;
}
