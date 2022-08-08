package com.zcdy.dsc.modules.rdp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具
 *
 * @author 在信汇通
 */
public class TimeUtil {
    /**
     * 获取当前日期是星期几
     *
     * @param date
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 获取当前年月日时分秒
     *
     * @param date
     * @return 获取当前年月日时分秒
     */
    public static String getTheDateTimeStr(Date date) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        String theTime = now.get(Calendar.YEAR) +
                "年" +
                (now.get(Calendar.MONTH) + 1) +
                "月" +
                now.get(Calendar.DAY_OF_MONTH) +
                "日 " +
                now.get(Calendar.HOUR_OF_DAY) +
                "时" +
                now.get(Calendar.MINUTE) +
                "分" +
                now.get(Calendar.SECOND) +
                "秒 ";
        return theTime;
    }

    /**
     * 获取当前年月日时分秒
     *
     * @param date
     * @return 获取当前年月日时分秒
     */
    public static String getTheDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }



}
