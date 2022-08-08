package com.zcdy.dsc.modules.rdp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

/**
 * @author 在信汇通
 * @Description: RDP表格
 * @Date: 2020-12-11 15:52
 * @version: 1.0
 */
@Getter
@Setter
@ApiModel(value="TableDataVo",description="RDP表格")
public class TableDataVo {
    /**表格表头*/
    @Excel(name = "表格表头", width = 15)
    @ApiModelProperty(value = "表格表头")
    private List<ColumnDataVo> columnData;
    /**表格数据*/
    @Excel(name = "表格数据", width = 15)
    @ApiModelProperty(value = "表格数据")
    private ResponseDataVo responseData;
}
