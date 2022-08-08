package com.zcdy.dsc.modules.settle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsQueryParam;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsVo;
import org.apache.ibatis.annotations.Param;
import org.quartz.JobExecutionContext;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author : songguang.jiao
 */
public interface SettlementStatisticsService {
    /**
     * 通过客户查询未结算数据
     * 1. 之前未结算抄表记录
     * 2. 当前时间至上次抄表记录时间的抄表数据
     * @param customerId 客户id
     * @return 未结算记录
     */
    List<SettlementStatisticsVo> getSettleByCustomerId(String customerId);

    /**
     * 结算管理首页数据
     * @param queryParam 查询条件
     * @return 数据
     */
    List<SettlementStatisticsVo> getSettleByQueryParam(SettlementStatisticsQueryParam queryParam);

    /**
     * 通过水表id, 和日期 获取水表的单价
     * 返回值可能为null, 在水价表中找不到数据. 注意处理NullPointException
     * @param equipmentId 获取水表单价
     * @param date 时间 yyyy-MM-dd
     * @return 单价
     */
    public BigDecimal getUnitPrice(String equipmentId, String date);

    /**
     * 手工结算
     * @author tangchao
     * @param settlementList 结算数据
     */
    void manualSettle(List<SettlementStatisticsVo> settlementList);

    /**
     * 获取当前
     * @param customerId 客户id
     */
    List<SettlementStatisticsVo> getDynamicDataForNow(String customerId);

    /**
     * 根据日期获取结算数据
     * @param customerId 客户id
     * @param date 日期
     * @return 结算数据
     */
    List<SettlementStatisticsVo> getSettleByCustomerId(String customerId, String date);

    /**
     * 分页获取结算数据
     * @param page 分页信息
     * @param param 查询参数
     * @return 分页数据
     */
    IPage<SettlementStatisticsVo> pageSettle(Page<SettlementStatisticsVo> page, @Param("param") SettlementStatisticsQueryParam param);

    /**
     * 获取可以结算的客户ID
     * @param queryParam 查询条件
     * @return 客户ids
     */
    List<String> getCustomerIds(SettlementStatisticsQueryParam queryParam);

    /**
     * 自动结算
     * @param settlementList 结算数据
     */
    void autoSettle(List<SettlementStatisticsVo> settlementList);

    /**
     * 生成结算单.
     * 不包含非周期水表
     * @param custoemrId 客户id
     */
    void execute(String custoemrId);

    /**
     * 任务调度
     * @param context 调度上下文
     */
    void execute(JobExecutionContext context);
}
