package com.zcdy.dsc.modules.settle.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 描述: 用户报装信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-11
 * 版本号: V1.0
 */
@Data
@ApiModel(value="SettleUseralotVo", description="用户报装信息")
public class SettleUseralotVo {

	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**客户id*/
	@ApiModelProperty(value = "客户id")
	private String customerId;
	/**客户名称*/
	@Excel(name = "客户名称", width = 15)
	@ApiModelProperty(value = "客户名称")
	private String customerName;
	/**报装时间*/
	@Excel(name = "报装时间", width = 20, format = "yyyy-MM-dd HH:mm")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @ApiModelProperty(value = "报装时间")
	private java.util.Date lotTime;
	/**报装内容*/
	@Excel(name = "报装内容", width = 15)
    @ApiModelProperty(value = "报装内容")
	private String lotContent;
	/**任务分配人员id*/
	@ApiModelProperty(value = "任务分配人员id")
	private String taskId;
	/**任务分配人员*/
	@Excel(name = "任务分配人员", width = 15)
    @ApiModelProperty(value = "任务分配人员")
	private String taskName;
	/**实施人联系电话*/
	@Excel(name = "实施人联系电话", width = 15)
    @ApiModelProperty(value = "实施人联系电话")
	private String telephone;
	/**安装状态*/
    @ApiModelProperty(value = "安装状态")
	private String installState;
	/**安装状态，value*/
	@Excel(name = "安装状态", width = 15)
	@ApiModelProperty(value = "安装状态-value")
	private String installStateValue;
	/**安装时间*/
	@Excel(name = "安装时间", width = 20, format = "yyyy-MM-dd HH:mm")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @ApiModelProperty(value = "安装时间")
	private java.util.Date installTime;
	/**用途*/
	@Excel(name = "用途", width = 15)
    @ApiModelProperty(value = "用途")
	private String purpose;
	/**审核验收意见*/
	@Excel(name = "审核验收意见", width = 15)
    @ApiModelProperty(value = "审核验收意见")
	private String accepidea;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;

}
