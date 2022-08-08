package com.zcdy.dsc.modules.operation.equipment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Classname: siutp-parent
 * 创建时间： 2020/2/6 16:15
 * @author：  * 描述:
 */
@Getter
@Setter
@ApiModel(value="OptApplyItemVo", description="货物申请项详情")
public class OptApplyItemVo {
    /**id*/
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**仓库id*/
    @ApiModelProperty(value = "仓库id")
    private java.lang.String storeId;
    /**仓库名称*/
    @ApiModelProperty(value = "仓库名称--回显用")
    private java.lang.String storeName;
    /**运维工具id*/
    @ApiModelProperty(value = "运维工具id")
    private java.lang.String dataId;
    /**运维工具名称*/
    @ApiModelProperty(value = "工具规格")
    private java.lang.String toolModel;
    /**运维工具名称*/
    @ApiModelProperty(value = "工具规格code")
    private java.lang.String toolModelCode;
    /**运维工具名称*/
    @ApiModelProperty(value = "工具名称")
    private java.lang.String dataName;
    /**运维工具名称code*/
    @ApiModelProperty(value = "工具名称Code")
    private java.lang.String dataNameCode;
    /**amount*/
    @ApiModelProperty(value = "申请数量")
    private java.lang.String amount;

}
