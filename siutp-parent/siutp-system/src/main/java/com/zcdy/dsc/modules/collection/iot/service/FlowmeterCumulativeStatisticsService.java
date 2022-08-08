package com.zcdy.dsc.modules.collection.iot.service;
import java.util.List;
import java.util.Map;

import com.zcdy.dsc.modules.collection.iot.vo.FlowmeterCumulativeStatisticsParam;


/**
 * @author tangchao
 * @since 2020-4-28 13:00:58
 * 累计流量统计
 */

public interface FlowmeterCumulativeStatisticsService{

    /**
     * @author tangchao
     *  beginDate 开始时间
     *  cycle 周期
     *  interval 间隔
     *  cycleUnit 周期单位: 周期单位, YEARS-年 MONTHS-月 DAYS-日 HOURS-小时 MINUTES-分
     *  intervalUnit 间隔单位:  MONTHS-月 DAYS-日 HOURS-小时 MINUTES-分
     * @since 2020年4月28日12:58:58
     * @return
     */
    List<Map<String, Object>> ioDataByCycleAndInterval(FlowmeterCumulativeStatisticsParam param);
    List<Map<String, Object>> ioDataByCycleAndIntervalDataFormat(FlowmeterCumulativeStatisticsParam param);


}
