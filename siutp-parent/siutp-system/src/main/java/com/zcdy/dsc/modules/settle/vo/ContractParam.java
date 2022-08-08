package com.zcdy.dsc.modules.settle.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 合同新增修改参数
 * @author：  songguang.jiao
 * 创建时间：  2020年1月13日 下午12:58:24
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="ContractParam",description="合同新增修改参数")
public class ContractParam {

	/**id*/
	@ApiModelProperty(value = "id")
	private java.lang.String id;
	/**客户信息*/
	@ApiModelProperty(value = "客户信息")
	@NotBlank(message="客户信息id不能为空")
	private java.lang.String customerId;
	/**合同编号*/
	@ApiModelProperty(value = "合同编号")
	@NotBlank(message="合同编号不能为空")
	private java.lang.String contractSn;
	/**合同名称*/
	@ApiModelProperty(value = "合同名称")
	private java.lang.String contractName;
	/**生效日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "生效日期")
	@NotNull(message="生效日期不能为空")
	private java.util.Date startDate;
	/**结束日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "结束日期")
	@NotNull(message="结束日期不能为空")
	private java.util.Date endDate;
	/**用途*/
	@ApiModelProperty(value = "用途")
	private java.lang.String contractUse;
	/**签订日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "签订日期")
	@NotNull(message="签订日期不能为空")
	private java.util.Date signDate;
	/**合同附件地址，通过上传得到*/
	@ApiModelProperty(value = "合同附件地址，通过上传得到")
	private java.lang.String fileUrl;
	/**备注信息*/
	@ApiModelProperty(value = "备注信息")
	private java.lang.String remarks;

    @ApiModelProperty(value = "收入分类")
    @NotBlank(message="收入分类不能为空")
 	private java.lang.String type;
	
}
