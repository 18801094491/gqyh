package com.zcdy.dsc.modules.settle.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户信息集合
 * 
 * @author songguang.jiao
 * @date 2020/05/12 10:30:49
 */
@Getter
@Setter
@ApiModel(value = "CustomerInfoData")
public class CustomerInfoData {
    
    /**
     * 月份信息详情
     */
    @ApiModelProperty(value="月份信息详情")
    private List<CustomerMonthInfo> customerMonthInfos;

    /**
     * 用户用水统计信息
     */
    @ApiModelProperty(value = "用户统计信息")
    private List<CustomerCount> customerCounts;

    /**
     * 用户信息统计
     * @author songguang.jiao
     * @date 2020/05/12 11:59:45
     */
    @Getter
    @Setter
    @ApiModel(value = "CustomerCount")
    public static final class CustomerCount {
        /**
         * 客户名称
         */
        @ApiModelProperty(value = "客户名称")
        private String customerName;

        /**
         * 总用水量
         */
        @ApiModelProperty(value = "总用水量")
        private String totalMeter;

        /**
         * 总费用
         */
        @ApiModelProperty(value = "总费用")
        private String totalCost;
    }

    
    /**
     * 月份信息详情
     * @author songguang.jiao
     * @date 2020/05/12 12:00:03
     */
    @Getter
    @Setter
    @ApiModel(value = "CustomerMonthInfo")
    public static final class CustomerMonthInfo {
        /**
         * 客户名称
         */
        @ApiModelProperty(value = "统计月份")
        private String date;

        /**
         * 统计详细
         */
        @ApiModelProperty(value = "统计详细")
        private List<SettlementStatisticsVo> listDetail;
        
        public CustomerMonthInfo(String date, List<SettlementStatisticsVo> listDetail) {
            super();
            this.date = date;
            this.listDetail = listDetail;
        }

        public CustomerMonthInfo() {
            super();
        }
        
    }

}
