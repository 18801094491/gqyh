package com.zcdy.dsc.modules.rdp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author 在信汇通
 * @Description: RDP数字返回VO
 * @Date: 2020-12-11 15:52
 * @version: 1.0
 */
@Getter
@Setter
@ApiModel(value="NumDataVo",description="RDP数字返回VO")
public class NumDataVo {
    /** 数 */
    @Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private Integer num ;

    /** 数 */
    @Excel(name = "名字", width = 15)
    @ApiModelProperty(value = "名字")
    private String name ;


}
