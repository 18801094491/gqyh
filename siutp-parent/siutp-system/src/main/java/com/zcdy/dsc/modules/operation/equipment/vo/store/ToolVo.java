package com.zcdy.dsc.modules.operation.equipment.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 工具列表对象
 * @author：  songguang.jiao
 * 创建时间：  2020年1月9日 下午1:02:58
 * 版本号: V1.0
 */
@Setter
@Getter
@ApiModel(value="ToolVo",description="工具列表对象")
public class ToolVo {

	/**工具id*/
    @ApiModelProperty(value = "工具id")
	private java.lang.String id;
	/**工具名称*/
    @ApiModelProperty(value = "工具名称")
	private java.lang.String toolName;
	/**工具*/
    @ApiModelProperty(value = "工具规格")
	private java.lang.String toolModel;
    /**供货商名称*/
    @ApiModelProperty(value = "供货商名称")
	private java.lang.String supplierName;
    /**所在仓库ID*/
    @ApiModelProperty(value = "所在仓库")
	private java.lang.String storeName;
    //工具编号
    @ApiModelProperty(value = "工具编号")
    private String toolSn;
    //生产厂家
    @ApiModelProperty(value = "生产厂家")
    private String toolFactory;
    //总量
    @ApiModelProperty(value = "总量")
    private String total;
    //损坏报废数量
    @ApiModelProperty(value = "损坏报废数量")
    private String damageNum;
    //外借数量
    @ApiModelProperty(value = "外借数量")
    private String borrowedNum;
    //可用量
    @ApiModelProperty(value = "可用量")
    private String usedNum;
}
