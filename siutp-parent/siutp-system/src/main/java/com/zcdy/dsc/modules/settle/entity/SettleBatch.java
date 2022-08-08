package com.zcdy.dsc.modules.settle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zcdy.dsc.modules.settle.constant.SettleConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author tangchao
 * @version V1.0
 * @Description: 结算批次
 * @date 2020-5-8 14:42:34
 */
@Data
@TableName("settle_batch")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "settle_batch", description = "结算批次")
public class SettleBatch {

    /**
     * 结算批次主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "结算批次主键")
    private Integer id;
    /**
     * 上一次批次id, 为空说明为首条数据
     */
    @Excel(name = "上一次批次id, 为空说明为首条数据", width = 15)
    @ApiModelProperty(value = "上一次批次id, 为空说明为首条数据")
    private Integer previousId;
    /**
     * 结算清单id
     */
    @Excel(name = "结算清单id, 为空说明未结算", width = 15)
    @ApiModelProperty(value = "结算清单id, 为空说明未结算")
    private Integer checklistId;
    /**
     * 水表id
     */
    @Excel(name = "水表id", width = 15)
    @ApiModelProperty(value = "水表id")
    private String equipmentId;
    /**
     * 水表类型 周期水表 非周期水表
     */
    @Excel(name = "水表类型 周期水表 非周期水表", width = 15)
    @ApiModelProperty(value = "水表类型 周期水表 非周期水表")
    private String equipmentType;
    /**
     * 客户id, 为空表示水表没有绑定
     */
    @Excel(name = "客户id, 为空表示水表没有绑定", width = 15)
    @ApiModelProperty(value = "客户id, 为空表示水表没有绑定")
    private String customerId;
    /**
     * 合同id, 为空表示水表没有绑定合同
     */
    @Excel(name = "合同id, 为空表示水表没有绑定合同", width = 15)
    @ApiModelProperty(value = "合同id, 为空表示水表没有绑定合同")
    private String contractId;
    /**
     * 区域id, 抄表时候没有绑定水表
     */
    @Excel(name = "区域id, 抄表时候没有绑定水表", width = 15)
    @ApiModelProperty(value = "区域id, 抄表时候没有绑定水表")
    private String districtId;
    /**
     * 当前抄表日期
     */
    @Excel(name = "当前抄表日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "当前抄表日期")
    private java.util.Date currentFlowDate;
    /**
     * 当前抄表时间
     */
    @Excel(name = "当前抄表时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "当前抄表时间")
    private java.util.Date currentFlowTime;
    /**
     * 正向累计流量
     */
    @Excel(name = "正向累计流量", width = 15)
    @ApiModelProperty(value = "正向累计流量")
    private String currentPositiveFlow;
    /**
     * 负向累计流量
     */
    @Excel(name = "负向累计流量", width = 15)
    @ApiModelProperty(value = "负向累计流量")
    private String currentNavigatFlow;
    /**
     * 当前表底
     */
    @Excel(name = "当前表底", width = 15)
    @ApiModelProperty(value = "当前表底")
    private String currentFlow;
    /**
     * 批次内净用水量
     */
    @Excel(name = "批次内净用水量", width = 15)
    @ApiModelProperty(value = "批次内净用水量")
    private String usedFlow;
    /**
     * 单价
     */
    @Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.math.BigDecimal unitPrice;
    /**
     * 实际消费
     */
    @Excel(name = "实际消费", width = 15)
    @ApiModelProperty(value = "实际消费")
    private java.math.BigDecimal totalCost;
    /**
     * 00-未结算 01-手工结算 02-自动结算
     */
    @Excel(name = "00-未结算 01-手工结算 02-自动结算", width = 15)
    @ApiModelProperty(value = "00-未结算 01-手工结算 02-自动结算")
    private String status;
    /**
     * 创建人
     */
    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 修改人
     */
    @Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String updateBy;
    /**
     * 修改时间
     */
    @Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;

    public SettleBatch() {
        this.status = SettleConstant.STATUS_UNSETTLEMENT;
    }
    public SettleBatch(String status) {
        this.status = status;
    }

    public static SettleBatch initEmptyInstant() {
        Date now = new Date();
        SettleBatch settleBatch = new SettleBatch();
        settleBatch.setCurrentFlowTime(now);
        settleBatch.setCurrentFlowDate(now);
        settleBatch.setCurrentPositiveFlow("0");
        settleBatch.setCurrentNavigatFlow("0");
        settleBatch.setCurrentFlow("0");
        settleBatch.setUnitPrice(BigDecimal.ZERO);
        settleBatch.setTotalCost(BigDecimal.ZERO);
        settleBatch.setTotalCost(BigDecimal.ZERO);
        return settleBatch;
    }

    public BigDecimal getCurrentPositiveFlowDecimal() {
        return StringUtils.isBlank(this.currentPositiveFlow) ? BigDecimal.ZERO : new BigDecimal(this.currentPositiveFlow);
    }

    public BigDecimal getCurrentNavigatFlowDecimal() {
        return StringUtils.isBlank(this.currentNavigatFlow) ? BigDecimal.ZERO : new BigDecimal(this.currentNavigatFlow);
    }

    public BigDecimal getCurrentFlowDecimal() {
        return StringUtils.isBlank(this.currentFlow) ? BigDecimal.ZERO : new BigDecimal(this.currentFlow);
    }

    public BigDecimal getUsedFlowDecimal() {
        return new BigDecimal(this.usedFlow);
    }

    public String getCurrentFlowDateStr() {
        if (this.currentFlowDate == null) {
            return "";
        }
        return this.currentFlowDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getCurrentTimeDateStr() {
        if (this.currentFlowTime == null) {
            return "";
        }
        return this.currentFlowTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
