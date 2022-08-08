package com.zcdy.dsc.modules.operation.equipment.vo.store;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zcdy.dsc.modules.operation.equipment.vo.check.storebill.CheckAmount;
import com.zcdy.dsc.modules.operation.equipment.vo.check.storebill.CheckStoreId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 备品备件出入库参数
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-03
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="SparepartParamVo", description="备品备件出入库参数")
public class SparepartBillParamVo {
	/**备品备件Id*/
	@ApiModelProperty(value = "备品备件Id")
	private String sparepartId;
	/**备品备件*/
	@ApiModelProperty(value = "备品备件")
	private String sparepartName;
    /**备件型号*/
    @ApiModelProperty(value = "备件型号")
    private String sparepartModel;
	/**备件规格*/
    @ApiModelProperty(value = "备件规格")
	private String sparepartSpecs;
    /**批次号*/
    @ApiModelProperty(value = "批次号")
    private java.lang.String batchSn;
	/**供应商id*/
    @ApiModelProperty(value = "供应商id")
	private String supplierId;
    /**仓库id*/
    @NotNull(message="未指定仓库",groups={CheckStoreId.class})
    @NotBlank(message = "未指定仓库",groups={CheckStoreId.class})
    @ApiModelProperty(value = "仓库id")
    private java.lang.String storeId;
    
    /**数量*/
	@NotNull(message="数量不能为空",groups={CheckAmount.class})
	@Min(value = 1, message = "最小数量为1",groups={CheckAmount.class})
//	@Pattern(regexp="^[0-9]*[1-9][0-9]*$",groups={CheckAmount.class},message="请输入正整数")
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
