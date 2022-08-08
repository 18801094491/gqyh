package com.zcdy.dsc.modules.settle.vo;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 合同列表信息
 * @author： songguang.jiao
 * 创建时间： 2020年1月13日 上午11:45:46
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value = "ContractVo", description = "合同列表信息")
public class ContractVo {

	/** id */
	@ApiModelProperty(value = "id")
	private java.lang.String id;
	/** 客户信息 */
	@ApiModelProperty(value = "客户信息id")
	private java.lang.String customerId;
	/** 客户信息名称 */
	@ApiModelProperty(value = "客户信息名称")
	private java.lang.String constomerName;
	/** 合同编号 */
	@ApiModelProperty(value = "合同编号")
	private java.lang.String contractSn;
	/** 合同名称 */
	@ApiModelProperty(value = "合同名称")
	private java.lang.String contractName;
	/** 生效日期 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@ApiModelProperty(value = "生效日期")
	private java.util.Date startDate;
	/** 结束日期 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@ApiModelProperty(value = "结束日期")
	private java.util.Date endDate;
	/** 用途 */
	@ApiModelProperty(value = "用途")
	private java.lang.String contractUse;
	/** 签订日期 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@ApiModelProperty(value = "签订日期")
	private java.util.Date signDate;
	/** 合同附件地址，通过上传得到 */
	@ApiModelProperty(value = "合同附件地址，通过上传得到")
	private java.lang.String fileUrl;
	/** 备注信息 */
	@ApiModelProperty(value = "备注信息")
	private java.lang.String remarks;
	/**收入分类*/
    @ApiModelProperty(value = "收入分类code")
	private java.lang.String type;
	/**收入分类*/
    @ApiModelProperty(value = "收入分类名称")
	private java.lang.String typeName;
    /**倒计时(天)*/
    @ApiModelProperty(value = "倒计时(天)")
    private String countdownDays;
    /**是否告警(1-是,0-否)*/
    @ApiModelProperty(value = "是否告警(1-是,0-否)")
    private String alarmStatus;
}
