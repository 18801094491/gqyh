package com.zcdy.dsc.modules.operation.equipment.vo.store;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 运维工具入库参数
 * @author：  songguang.jiao
 * 创建时间： 2020年2月6日 下午4:52:26 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="OptToolBillVo",description="运维工具入库参数")
public class ToolBillParamVo {
	
	/**工具id*/
    @ApiModelProperty(value = "工具id")
	private java.lang.String toolId;
	/**工具名称*/
    @NotEmpty(message="工具名称不能为空")
    @NotNull(message="工具名称不能为空")
    @ApiModelProperty(value = "工具名称")
	private java.lang.String toolName;
	/**工具规格*/
    @NotEmpty(message="工具规格不能为空")
    @NotNull(message="工具规格不能为空")
    @ApiModelProperty(value = "工具规格")
	private java.lang.String toolModel;
	/**供货商*/
    @NotEmpty(message="供货商不能为空")
    @NotNull(message="供货商不能为空")
    @ApiModelProperty(value = "供货商")
	private java.lang.String supplierId;
    /**仓库id*/
    @NotEmpty(message="仓库id不能为空")
    @NotNull(message="仓库id不能为空")
    @ApiModelProperty(value = "仓库id")
    private java.lang.String storeId;
    //生产厂家
    @ApiModelProperty(value = "生产厂家")
    private String toolFactory;
	/**批次号*/
    @ApiModelProperty(value = "批次号")
	private java.lang.String batchSn;
	/**数量*/
    @NotNull(message="数量不能为空")
    @Min(value=1,message="数量必须为正整数")
    @ApiModelProperty(value = "数量")
	private java.lang.Integer amount;
	/**生产日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "生产日期")
	private java.util.Date productDate;
	/**有效期止*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "有效期止")
	private java.util.Date endDate;
	
}
