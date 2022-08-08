package com.zcdy.dsc.modules.operation.equipment.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述: 备品备件管理信息表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-03
 * 版本号: V1.0
 */
@Data
@ApiModel(value="OptSpaGetVo", description="备品备件管理信息表")
public class SparepartVo {
    /**id*/
    @ApiModelProperty(value = "id")
    private String id;
	/**备件名称*/
    @ApiModelProperty(value = "备件名称")
	private String sparepartName;
	/**备件规格*/
    @ApiModelProperty(value = "备件规格")
	private String sparepartSpecs;
	/**批次号*/
	@ApiModelProperty(value = "批次号")
	private String batchSn;
	/**仓库id*/
	@ApiModelProperty(value = "仓库id")
	private String storeId;
	/**仓库名称*/
	@ApiModelProperty(value = "仓库名称")
	private String storeName;
	/**库存报警值*/
    @ApiModelProperty(value = "库存报警值")
	private Integer warnAmount;
	/**库存量*/
	@ApiModelProperty(value = "库存量")
	private Integer amount;
	/**供应商Id*/
	@ApiModelProperty(value = "供应商Id")
	private String supplierId;
	/**供应商*/
	@ApiModelProperty(value = "供应商")
	private String supplierName;
	/**是否超过预警值*/
	@ApiModelProperty(value = "是否超过预警值， 0正常  1 报警  2 未入库")
	private String state;
	/**型号*/
	@ApiModelProperty(value = "型号")
	private String sparepartModel;
	
}
