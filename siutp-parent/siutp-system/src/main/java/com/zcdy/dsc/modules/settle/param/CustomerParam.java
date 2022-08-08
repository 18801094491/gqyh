package com.zcdy.dsc.modules.settle.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 描述: 新增客户信息入参 @author： songguang.jiao 创建时间： 2020年1月2日 下午4:16:08 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value = "CustomerAddParam", description = "新增客户信息入参")
public class CustomerParam {
    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private String id;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    /**
     * 客户类型
     */
    @ApiModelProperty(value = "客户类型")
    private String customerType;
    /**
     * 客户地址
     */
    @ApiModelProperty(value = "客户地址")
    private java.lang.String customerAddress;
    /**
     * 水价模式
     */
    @ApiModelProperty(value = "value")
    private String priceMode;
    /**
     * 付款模式
     */
    @ApiModelProperty(value = "付款模式")
    private String payMode;
    /**
     * 水价(元/吨)
     */
    @ApiModelProperty(value = "水价(元/吨)")
    private String price;
    /**
     * 客户联系人姓名
     */
    @ApiModelProperty(value = "联系人姓名")
    private String contacterName;
    /**
     * 客户联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String contacterPhone;

    /**
     * 客户税号
     */
    @ApiModelProperty(value = "客户税号")
    private String customeDuty;

    /**
     * 客户银行账号
     */
    @ApiModelProperty(value = "客户银行账号")
    private String customeBankAccout;
    
    /**
     * 客户状态
     */
    @ApiModelProperty(value="客户状态")
    private String customerStatus;

}
