package com.zcdy.dsc.modules.operation.equipment.vo.store;

import org.jeecgframework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 导出工具列表
 * @author：  songguang.jiao
 * 创建时间： 2020年2月7日 上午9:59:39 
 * 版本号: V1.0
 */
@Getter
@Setter
public class ToolExportVo {
	/**工具名称*/
	@Excel(name = "工具名称", width = 15)
    @ApiModelProperty(value = "工具名称")
	private java.lang.String toolName;
	/**工具*/
	@Excel(name = "工具规格", width = 15)
    @ApiModelProperty(value = "工具规格")
	private java.lang.String toolModel;
    /**供货商名称*/
	@Excel(name = "供货商名称", width = 15)
    @ApiModelProperty(value = "供货商名称")
	private java.lang.String supplierName;
    /**所在仓库*/
	@Excel(name = "所在仓库", width = 15)
    @ApiModelProperty(value = "所在仓库")
	private java.lang.String storeName;
    //生产厂家
	@Excel(name = "生产厂家", width = 15)
    @ApiModelProperty(value = "生产厂家")
    private String toolFactory;
    //总量
	@Excel(name = "总量", width = 15)
    @ApiModelProperty(value = "总量")
    private String total;
    //损坏报废数量
	@Excel(name = "损坏报废数量", width = 15)
    @ApiModelProperty(value = "损坏报废数量")
    private String damageNum;
    //外借数量
	@Excel(name = "外借数量", width = 15)
    @ApiModelProperty(value = "外借数量")
    private String borrowedNum;
    //可用量
	@Excel(name = "可用量", width = 15)
    @ApiModelProperty(value = "可用量")
    private String usedNum;
	
}
