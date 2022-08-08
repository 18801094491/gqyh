package com.zcdy.dsc.modules.rdp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author 在信汇通
 * @Description: RDP表格表头数据
 * @Date: 2020-12-11 15:52
 * @version: 1.0
 */
@Getter
@Setter
@ApiModel(value="ColumnDataVo",description="RDP表格表头数据")
public class ColumnDataVo {
    @Excel(name = "表头字段", width = 15)
    @ApiModelProperty(value = "表头字段")
    private String key;
    @Excel(name = "表头名字", width = 15)
    @ApiModelProperty(value = "表头名字")
    private String text;
}
