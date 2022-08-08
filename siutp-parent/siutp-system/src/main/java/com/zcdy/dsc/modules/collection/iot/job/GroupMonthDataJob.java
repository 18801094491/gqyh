package com.zcdy.dsc.modules.collection.iot.job;

import com.zcdy.dsc.common.framework.influxdb.InfluxService;
import com.zcdy.dsc.modules.datacenter.statistic.entity.PointClauseData;
import com.zcdy.dsc.modules.datacenter.statistic.entity.PointDataMonth;
import org.influxdb.dto.Query;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Roberto
 * 创建时间:2020年4月28日 下午5:47:55
 * 描述: <p>每月月初执行一次数据统计，并存入influxdb:iot_point_data_month</p>
 */
public class GroupMonthDataJob implements Job {

    @Resource
    private InfluxService influxService;

    /*
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        LocalDate today = LocalDate.now();

        LocalDate lastDay = today.plusDays(-1);
        LocalDate firstDay = lastDay.with(TemporalAdjusters.firstDayOfMonth());
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select mean(avg_value) as avg_value,max(max_value) as max_value, min(min_value) as min_value from iot_point_data_day where time>=")
                .append(firstDay.atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli()).append("ms")
                .append(" ")
                .append("and time<=")
                .append(lastDay.atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli()).append("ms")
                .append(" ")
                .append("group by var_name");
        Query query = new Query(sqlBuilder.toString());
        List<PointClauseData> data = this.influxService.query(query, PointClauseData.class, "iot_point_data_day");
        List<PointDataMonth> insert = new ArrayList<PointDataMonth>();
        for (PointClauseData pointClauseData : data) {
            PointDataMonth dataMonth = new PointDataMonth();
            dataMonth.setTime(pointClauseData.getTime().toEpochMilli());
            dataMonth.setVarName(pointClauseData.getVarName());
            dataMonth.setAvgValue(pointClauseData.getAvgValue());
            dataMonth.setDateTime(pointClauseData.getDateTime());
            dataMonth.setMinValue(pointClauseData.getMinValue());
            dataMonth.setMaxValue(pointClauseData.getMaxValue());
            insert.add(dataMonth);
        }
        this.influxService.writeBatch(insert);
    }
}
