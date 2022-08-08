package com.zcdy.dsc.modules.settle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zcdy.dsc.constant.ScaleConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author tangchao
 */
@Data
@ApiModel(value = "结算管理", description = "结算管理-详情")
public class SettlementStatisticsVo {
    
    /**
     * 区域名称
     */
    @Excel(name="区域名称")
    @ApiModelProperty(value = "区域名称")
    private String equipmentDistrictName;
    
    /**
     * 结算月份
     */
    @Excel(name="结算月份",format="yyyy-MM")
    @ApiModelProperty(value = "结算月份")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM")
    private Date settleDate;
    
    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    @Excel(name="客户名称")
    private String customerName;

   
    /**
     * 客户类型
     */
    @Excel(name="客户类型")
    @ApiModelProperty(value = "客户类型")
    private String customerType;
    /**
     * 签约次数
     */
    @Excel(name="签约次数")
    @ApiModelProperty(value = "签约次数")
    private String signCount;
    /**
     * 合同id
     */
    @ApiModelProperty(value = "合同id")
    private String contractId;
    /**
     * 合同名称
     */
    @Excel(name="合同名称")
    @ApiModelProperty(value = "合同名称")
    private String contractName;
    /**
     * 合同起始时间
     */
    @Excel(name="合同起始时间")
    @ApiModelProperty(value = "合同起始时间")
    private String contractValidateDate;
    /**
     * 合同终止时间
     */
    @Excel(name="合同终止时间")
    @ApiModelProperty(value = "合同终止时间")
    private String contractInvalidateDate;
    /**
     * 水表id
     */
    @ApiModelProperty(value = "水表id")
    private String equipmentId;
    /**
     * 水表信息 设备类型[标段 + 放置位置][设备编号]
     */
    @Excel(name="水表信息")
    @ApiModelProperty(value = "水表信息 设备类型[标段 + 放置位置][设备编号]")
    private String equipmentInfo;
    /**
     * 水表区域ID
     */
    @ApiModelProperty(value = "水表区域ID")
    private String equipmentDistrictId;
   
    /**
     * 本次抄表日期
     */
    @Excel(name="本次抄表日期",format="yyyy-MM-dd")
    @ApiModelProperty(value = "本次抄表日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date currentFlowDate;
    /**
     * 本次抄表时间
     */
    @ApiModelProperty(value = "本次抄表时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date currentFlowTime;
    /**
     * 本次表底
     */
    @Excel(name="本次表底")
    @ApiModelProperty(value = "本次表底")
    private String currentFlow;
    /**
     * 上次抄表日期
     */
    @Excel(name="上次抄表日期",format="yyyy-MM-dd")
    @ApiModelProperty(value = "上次抄表日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date previousFlowDate;
    /**
     * 上次抄表时间
     */
    @ApiModelProperty(value = "上次抄表时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date previousFlowTime;
    /**
     * 上次表底
     */
    @Excel(name="上次表底")
    @ApiModelProperty(value = "上次表底")
    private String previousFlow;
    /**
     * 本期实际用水量 = 本次表底 - 上次表底
     */
    @Excel(name="本次用水量")
    @ApiModelProperty(value = "本期实际用水量 = 本次表底 - 上次表底")
    private String currentUsedFlow;
    /**
     * 本期水价
     */
    @Excel(name="本期水价")
    @ApiModelProperty(value = "本期水价")
    private String currentWaterPrice;
    /**
     * 本期应缴费用 = 本期实际用水量 * 本期水价
     */
    @Excel(name="本期应缴费用")
    @ApiModelProperty(value = "本期应缴费用 = 本期实际用水量 * 本期水价")
    private String currentCost;
    
    /**
     * 状态
     */
    @Excel(name="结算结果")
    @ApiModelProperty(value = "状态code")
    private String status;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String statusTitle;

    /**
     * 结算周期id
     */
    @ApiModelProperty(value = "结算周期id")
    private String id;
    /**
     * 上次结算周期id
     */
    @ApiModelProperty(value = "上次结算周期id")
    private String previousId;
    
    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private String customerId;

    /**
     * 用作数值计算
     * 
     * @return 小数类型
     */
    @JsonIgnore
    public BigDecimal getCurrentFlowDecimal() {
        return this.retVal(this.currentFlow, ScaleConstant.LL_SCALE_STORE);
    }

    public String getCurrentFlow() {
        return this.retVal(this.currentFlow, ScaleConstant.LL_SCALE_SHOW).toEngineeringString();
    }

    public String getPreviousFlow() {
        return this.retVal(this.previousFlow, ScaleConstant.LL_SCALE_SHOW).toEngineeringString();
    }

    public String getCurrentUsedFlow() {
        return this.retVal(this.currentUsedFlow, ScaleConstant.LL_SCALE_SHOW).toEngineeringString();
    }

    public String getCurrentCost() {
        return this.retVal(this.currentCost, ScaleConstant.PRICE).toEngineeringString();
    }

    public String getCurrentWaterPrice() {
        return this.retVal(this.currentWaterPrice, ScaleConstant.PRICE).toEngineeringString();
    }

    public BigDecimal retVal(String val, int scale) {
        BigDecimal bigDecimal = StringUtils.isBlank(val) ? BigDecimal.ZERO : new BigDecimal(val);
        return bigDecimal.setScale(scale, RoundingMode.HALF_UP);
    }
    
    public void setCurrentFlowDateStr(String currentFlowDate) {
        this.currentFlowDate =
            Date.from(LocalDateTime.parse(currentFlowDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .atZone(ZoneId.systemDefault()).toInstant());
    }
    public void setCurrentFlowTimeStr(String currentFlowTime) {
        this.currentFlowTime = Date.from(LocalDateTime.parse(currentFlowTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.systemDefault()).toInstant());
    }

    public String getCurrentFlowTimeStr(){
        if(this.currentFlowTime == null){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(this.currentFlowTime);
    }

    public void setPreviousFlowDateStr(String previousFlowDate) {
        this.previousFlowDate =
            Date.from(LocalDateTime.parse(previousFlowDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .atZone(ZoneId.systemDefault()).toInstant());
    }

}
