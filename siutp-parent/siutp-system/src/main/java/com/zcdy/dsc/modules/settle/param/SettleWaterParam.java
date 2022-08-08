 package com.zcdy.dsc.modules.settle.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 水表管理分页查询
 * @author songguang.jiao
 * @date 2020/05/18 11:20:25
 */
@Getter
@Setter
public class SettleWaterParam extends AbstractPageParam{

    /**
     * 水表名称
     */
    @ApiModelProperty(value="水表名称")
    private String waterName;
    
    /**
     * 水表编号
     */
    @ApiModelProperty(value="水表编号")
    private String waterSn;
    
    /**
     * 所属标段
     */
    @ApiModelProperty(value="所属标段")
    private String equimentSection;
    
    /**
     * 客户名称
     */
    @ApiModelProperty(value="客户名称")
    private String customerName;
    
    /**
     * 放置位置
     */
    @ApiModelProperty(value="放置位置")
    private String equimentLocation;
    
    
    
}
