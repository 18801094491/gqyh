package com.zcdy.dsc.modules.rdp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;
import java.util.Map;

/**
 * @author 在信汇通
 * @Description: RDP表格数据
 * @Date: 2020-12-11 15:52
 * @version: 1.0
 */
@Getter
@Setter
@ApiModel(value="TableDataVo",description="RDP表格数据")
public class ResponseDataVo {
    /**表格数据*/
    @Excel(name = "表格数据", width = 15)
    @ApiModelProperty(value = "表格数据")
    private List<Map<String,Object>> data;
    /**表格数据条数*/
    @Excel(name = "表格数据条数", width = 15)
    @ApiModelProperty(value = "表格数据条数")
    private Integer totals;

    public void setData(List<Map<String,Object>> data){
        this.data = data;
        if (data != null){
            this.totals = data.size();
        }else {
            this.totals = 0;
        }
    }

    public Integer getTotals() {
        if (data != null){
            return this.data.size();
        }else {
            return 0;
        }
    }
}
