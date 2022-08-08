package com.zcdy.dsc.modules.settle.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 客户信息列表信息
 *
 * @author： songguang.jiao
 * 创建时间：  2020年1月2日 下午6:16:39
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value = "CustomerVo", description = "客户信息列表信息")
public class CustomerVo {

    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 客户编码
     */
    @Excel(name = "客户编码", width = 15, orderNum = "10")
    @ApiModelProperty(value = "客户编码")
    private java.lang.String customerSn;
    /**
     * 客户名称
     */
    @Excel(name = "客户名称", width = 15, orderNum = "20")
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    /**
     * 客户地址
     */
    @Excel(name = "客户地址", width = 15, orderNum = "30")
    @ApiModelProperty(value = "客户地址")
    private java.lang.String customerAddress;

    /**
     * 客户状态
     */
    @Excel(name = "客户状态", width = 15, orderNum = "40")
    @ApiModelProperty(value = "客户状态")
    private String customerStatusName;
    /**
     * 客户类型
     */
    @Excel(name = "客户类型", width = 15, orderNum = "50")
    @ApiModelProperty(value = "客户类型")
    private String customerTypeName;
    /**
     * 付款模式
     */
    @Excel(name = "付款模式", width = 15, orderNum = "60")
    @ApiModelProperty(value = "付款模式")
    private String payMode;
    /**
     * 客户税号
     */
    @Excel(name = "客户税号", width = 15, orderNum = "70")
    @ApiModelProperty(value = "客户税号")
    private String customeDuty;
    /**
     * 客户银行账号
     */
    @Excel(name = "客户银行账户", width = 15, orderNum = "80")
    @ApiModelProperty(value = "客户银行账号")
    private String customeBankAccout;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建日期", width = 20, orderNum = "90", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**
     * 客户类型
     */
    @ApiModelProperty(value = "客户类型")
    private String customerType;

    /**
     * 水价模式
     */
    @ApiModelProperty(value = "水价模式")
    private String priceMode;

    /**
     * 水价(元/吨)
     */
    @ApiModelProperty(value = "水价(元/吨)")
    private String price;


    /**
     * 客户状态
     */

    @ApiModelProperty(value = "客户状态")
    private String customerStatus;


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
     * 绑定状态1-未绑定,0-已绑定
     */
    @ApiModelProperty(value = "绑定状态1-未绑定,0-已绑定")
    private String bindStatus;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间，“空值”查询判断条件，非显示字段，只在生成sql时作为判断条件使用")
    private String createTimeEmptyStr;
}
