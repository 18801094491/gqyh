package com.zcdy.dsc.modules.settle.vo;

import com.zcdy.dsc.common.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tangchao
 * @date 2020/5/9
 */
@Data
@ApiModel("结算管理查询参数")
public class SettlementStatisticsQueryParam {
    /**
     * 设备Id
     */
    @ApiModelProperty("设备ID")
    private String equipmentId;
    /**
     * 客户名模糊查询
     */
    @ApiModelProperty("客户名称模糊查询")
    private String likeCustomerName;

    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private java.lang.String equipmentSn;

    /**
     * 所属标段
     */
    @ApiModelProperty(value = "所属标段")
    private java.lang.String equipmentSection;

    /**
     * 放置位置
     */
    @ApiModelProperty(value = "放置位置")
    private java.lang.String equipmentLocation;

    /**
     * 设备类型 1-非周期 0-周期
     */
    @ApiModelProperty(value = "设备类型 1-非周期 0-周期")
    private java.lang.String equipmentType;
    /**
     * 当前时间
     */
    @ApiModelProperty(value = "当前时间", hidden = true)
    private java.lang.String nowTime = DateUtil.currentDateTimeStr();

    @ApiModelProperty(value = "查询日期 yyy-MM-dd")
    private String queryDate;

    @ApiModelProperty(value = "客户id")
    private String customerId;

}
