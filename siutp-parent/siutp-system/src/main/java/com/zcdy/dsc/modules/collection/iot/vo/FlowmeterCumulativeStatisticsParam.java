package com.zcdy.dsc.modules.collection.iot.vo;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

/**
 * @author tangchao
 * @since 2020-5-6 19:30:37
 */
@Getter
@Setter
public class FlowmeterCumulativeStatisticsParam {
    /**
     * beginDate 开始时间
     */
    private String beginTime;
    /**
     * cycle 周期
     */
    private Integer cycle;
    /**
     * interval 间隔
     */
    private Integer interval;
    /**
     * cycleUnit 周期单位: 周期单位, YEARS-年 MONTHS-月 DAYS-日 HOURS-小时 MINUTES-分
     */
    private String cycleUnit;
    /**
     * intervalUnit 间隔单位:  MONTHS-月 DAYS-日 HOURS-小时 MINUTES-分
     */
    private String intervalUnit;
    /**
     * 设备ids, 之间用,号隔开
     */
    private String[] equipmentIds;
    private String endTime;

    /**
     * 前端数据分割为数组 分隔符为逗号
     *
     * @param equipmentIds 设备id集合
     */
    public void setEquipmentIds(String equipmentIds) {
        if (StringUtils.isBlank(equipmentIds)) {
            this.equipmentIds = new String[]{};
            return;
        }
        this.equipmentIds = equipmentIds.split(",");
    }

    public void validate() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        if (StringUtils.isEmpty(this.beginTime)) {
            throw new IllegalArgumentException("开始时间不能为空.");
        }
        try {
            DateTimeFormatter.ofPattern(pattern).parse(this.beginTime);
        } catch (Exception e) {
            throw new IllegalArgumentException("开始时间格式不正确, 正确格式为yyyy-MM-dd HH:mm:ss.");
        }
        if (this.cycle == null || this.cycle == 0) {
            throw new IllegalArgumentException("时间长度不能为0.");
        }
        if (this.cycleUnit == null) {
            throw new IllegalArgumentException("时间长度单位不能为空.");
        }
        if (this.interval == null || this.interval <= 0) {
            throw new IllegalArgumentException("间隔必须大于0.");
        }
        if (this.intervalUnit == null) {
            throw new IllegalArgumentException("间隔单位不能为空.");
        }
        String[] unit = {"YEARS", "MONTHS", "DAYS", "HOURS", "MINUTES", "SECONDS"};
        List<String> strings = Arrays.asList(unit);
        if (!strings.contains(cycleUnit)) {
            throw new IllegalArgumentException("时间长度单位错误: " + cycleUnit);
        }
        if (!strings.contains(intervalUnit)) {
            throw new IllegalArgumentException("间隔单位错误: " + intervalUnit);
        }

        if ("YEARS".equals(cycleUnit) && !("YEARS".equals(intervalUnit) || "MONTHS".equals(intervalUnit) || "DAYS".equals(intervalUnit))) {
            throw new IllegalArgumentException("时间长度单位为年情况下, 间隔单位只能是月或天");
        }
        if ("MONTHS".equals(cycleUnit) && !("MONTHS".equals(intervalUnit) || "DAYS".equals(intervalUnit))) {
            throw new IllegalArgumentException("时间长度单位为月情况下, 间隔单位只能是天");
        }
        if ("DAYS".equals(cycleUnit) && !("DAYS".equals(intervalUnit) || "HOURS".equals(intervalUnit))) {
            throw new IllegalArgumentException("时间长度单位为天情况下, 间隔单位只能是小时");
        }
        if ("HOURS".equals(cycleUnit) && !("HOURS".equals(intervalUnit) || "MINUTES".equals(intervalUnit))) {
            throw new IllegalArgumentException("时间长度单位为年情况下, 间隔单位只能是分钟");
        }

        LocalDateTime endTimeLocalDateTime = LocalDateTime.parse(this.beginTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).plus(this.cycle, ChronoUnit.valueOf(this.cycleUnit));
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        if(endTimeLocalDateTime.compareTo(now) > 0){
            endTimeLocalDateTime = now;
        }

        this.endTime = endTimeLocalDateTime.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(pattern)).toString();
        if (this.cycle < 0) {
            String temp = this.beginTime;
            this.beginTime = this.endTime;
            this.endTime = temp;
        }
    }

}
