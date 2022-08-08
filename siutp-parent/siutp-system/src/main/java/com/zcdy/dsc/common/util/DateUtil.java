package com.zcdy.dsc.common.util;

import cn.hutool.core.date.DateTime;
import org.apache.commons.lang.RandomStringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 描述: 计算时间
 * @author： songguang.jiao
 * 创建时间： 2020年2月13日 下午2:16:11
 * 版本号: V1.0
 */
public class DateUtil {

	public static final int CURRENT = 0;

	public static final int NEXT = 1;

	public static final int PREVIOUS = -1;

	public static final String dateFormatStr = "yyyy-MM-dd";

	public static final String dateTimeFormatStr = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 描述: 根据起始日期,计算中间的每个日期,间隔天数
	 * @author： songguang.jiao
	 * 创建时间： 2020年2月13日 下午2:16:39
	 * 版本号: V1.0
	 */
	public static List<String> findDates(String stime, String etime) throws Exception {
		List<String> allDate = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dBegin = sdf.parse(stime);
		Date dEnd = sdf.parse(etime);
		allDate.add(sdf.format(dBegin));
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			allDate.add(sdf.format(calBegin.getTime()));
		}
		return allDate;
	}

	/**
	 * 描述: 获取下一天
	 * @author： songguang.jiao
	 * 创建时间： 2020年2月13日 下午2:28:54
	 * 版本号: V1.0
	 */
	public static Date getNextDate(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}

	/**
	 * 日期增加天数，如果减去传入负数即可
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDays(Date date, int days)
	{
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}

	/**
	 * 将Date转换为Calendar类型
	 * @param date
	 * @return
	 */
	public static Calendar getCalendarForDate(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}

	/**
	 * 获取date所处的周几(day)，0和7都代表周日
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getDayOfWeek(Date date, int day)
	{
		Calendar c = getCalendarForDate(date);
		c.set(Calendar.DAY_OF_WEEK, day + 1);
		return c.getTime();
	}

	public static String date2String(Date d, String format)
	{
		if(d == null)
			return null;
		DateFormat df = new SimpleDateFormat(format);
		return df.format(d);
	}

	public static Date string2Date(String str, String format)
	{
		DateFormat df = new SimpleDateFormat(format);
		try
		{
			return df.parse(str);
		}
		catch (ParseException e)
		{
			return null;
		}
	}

	/**
	 * 描述: 获取上个月日期信息(传入年份,月份)
	 * @author： songguang.jiao
	 * 创建时间： 2020年2月13日 下午3:58:36
	 * 版本号: V1.0
	 */
	public static String getLastMonth(String month) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(format.parse(month));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 // 设置为上一个月
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		String accDate = format.format(calendar.getTime());
		return accDate;
	}

	/**
	 * 描述: 获取下个月日期信息(传入月份)
	 * @author： songguang.jiao
	 * 创建时间： 2020年2月13日 下午3:58:36
	 * 版本号: V1.0
	 */
	public static String getNextMonth(String month) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(format.parse(month));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 // 设置为下一个月
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		String accDate = format.format(calendar.getTime());
		return accDate;
	}

	/**
	 * 描述: 根据前后时间计算相差秒数,以前时间在前,最新时间在后
	 * @author： songguang.jiao
	 * 创建时间： 2020年2月18日 下午2:55:02
	 * 版本号: V1.0
	 */
	public static long getSeconds(LocalDateTime inDate, LocalDateTime nowDate) {
		Duration duration = Duration.between(inDate, nowDate);
		return duration.getSeconds();
	}
	
	/**
	 * 描述: 通过秒数计算实际时分秒
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月18日 下午2:56:36 
	 * 版本号: V1.0
	 */
	public static String convertHourMinuSec(Long seconds){
		long daySec=ChronoUnit.DAYS.getDuration().getSeconds();
		long hourSec = ChronoUnit.HOURS.getDuration().getSeconds();
		long minuSec = ChronoUnit.MINUTES.getDuration().getSeconds();
		long day=seconds/daySec;
		long hour =(seconds%daySec) / hourSec;
		long minute = (seconds%daySec%hourSec)/ minuSec;
		long sec = seconds%daySec%hourSec%minuSec;
		StringBuilder stringBuilder=new StringBuilder();
		if(day>0){
			stringBuilder.append(day).append(" 天 ");
		}
		if(hour>0){
			stringBuilder.append(hour).append(" 时 ");
		}
		if(minute>0){
			stringBuilder.append(minute).append(" 分 ");	
		}
		stringBuilder.append(sec).append(" 秒");
		return stringBuilder.toString();
	}
	
	/**
	 * 描述: Date转换为LocalDateTime
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月18日 下午2:59:01 
	 * 版本号: V1.0
	 */
	public static LocalDateTime convertDateToLocalDate(Date date) {
	    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	/**
	 * 描述: LocalDateTime转换为Date
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月18日 下午2:59:09 
	 * 版本号: V1.0
	 */
	public static Date convertLocalDateToDate(LocalDateTime time) {
	    return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	/**
	 * 描述: 获取当前时间戳+4位随机数
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月20日 下午2:38:33 
	 * 版本号: V1.0
	 */
	public static String getTimeStamp() {
		Date now = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmm-");
		String pre = sf.format(now);
		String charStr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String tail = RandomStringUtils.random(4, charStr);
		return pre + tail;
	}
	
	/**
	 * 描述: 获取当前日期向前7天日期
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月28日 下午3:09:59 
	 * 版本号: V1.0
	 */
	public static List<String> getSevenDays(){
		List<String> sevenDays = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        start.setTime(calendar.getTime());
        Long startTime = start.getTimeInMillis();
        Long endTime = System.currentTimeMillis();
        Long oneDay = 1000 * 60 * 60 * 24L;
        Long time = startTime;
        while (time <= endTime) {
            Date day = new Date(time);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            sevenDays.add(df.format(day));
            time += oneDay;
        }
        return sevenDays;
	}

	/**
	 * 获取当前日期字符串
	 * @param pattern 日期格式
	 * @return 当前时间
	 */
	public static String currentDateStr(String pattern){
		return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
	}
	/**
	 * 获取当前日期字符串
	 * @return 当前时间 yyyy-MM-dd
	 */
	public static String currentDateStr(){
		return currentDateStr("yyyy-MM-dd");
	}
	/**
	 * 获取当前时间字符串
	 * @param pattern 日期格式
	 * @return 当前时间
	 */
	public static String currentDateTimeStr(String pattern){
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 获取当前时间字符串
	 * @return 当前时间 yyyy-MM-dd HH:mm:ss
	 */
	public static String currentDateTimeStr(){
		return currentDateTimeStr("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取某周的开始日期
	 *
	 * @param offset 0本周，1下周，-1上周，依次类推
	 * @return
	 */
	public static LocalDateTime getWeekStart(int offset) {

		return LocalDateTime.now().plusWeeks(offset).with(DayOfWeek.MONDAY).minusDays(NEXT);
	}

	/**
	 * 获取某月的开始日期
	 *
	 * @param offset 0本月，1下个月，-1上个月，依次类推
	 * @return
	 */
	public static LocalDateTime getMonthStart(int offset) {
		return LocalDateTime.now().plusMonths(offset).with(TemporalAdjusters.firstDayOfMonth()).minusDays(NEXT);
	}

	/**
	 * 获得指定日期是星期几，1表示周日，2表示周一
	 *
	 * @param date 日期
	 * @return 天
	 */
	public static int dayOfWeek(Date date) {
		return DateTime.of(date).dayOfWeek();
	}

	/**
	 * 为一个日期增加对应的时间，减的话amount传入负数即可
	 *
	 * @param date 增加前的日期
	 * @param field 需要增加的时间类型（年、月、日等，具体应用时可直接使用addYear(),addMonth()等）
	 * @param amount 需要增加的数字，负数为减
	 * @return
	 */
	public static Date add(Date date, int field, int amount)
	{
		Calendar c = getCalendarForDate(date);
		c.add(field, amount);
		return c.getTime();
	}

	/**
	 * 为一个日期增加对应的月份
	 *
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date addMonth(Date date, int month)
	{
		return add(date, Calendar.MONTH, month);
	}

	/**
	 * 为一个日期增加对应的周
	 *
	 * @param date
	 * @param week
	 * @return
	 */
	public static Date addWeek(Date date, int week)
	{
		return addDay(date, week * 7);
	}

	/**
	 * 为一个日期增加对应的天数
	 *
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDay(Date date, int day)
	{
		return add(date, Calendar.DAY_OF_MONTH, day);
	}

	/**
	 * 获取date所处月份指定日期（天数）
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getDayOfMonth(Date date, int day)
	{
		Calendar c = getCalendarForDate(date);
		c.set(Calendar.DAY_OF_MONTH, day);
		return c.getTime();
	}

	/**
	 * 获取某月最后一天
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date)
	{
		Calendar c = getCalendarForDate(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	/**
	 * 获取date所处年的第一天
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfYear(Date date)
	{
		Calendar c = getCalendarForDate(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		return c.getTime();
	}

	/**
	 * 获取date所处年的最后一天
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfYear(Date date)
	{
		Calendar c = getCalendarForDate(date);
		c.set(Calendar.MONTH, Calendar.DECEMBER);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	/**
	 * 判断date是否处于begin和end之间
	 * @param date
	 * @param begin 起始时间
	 * @param end 结束时间
	 * @return
	 */
	public static boolean isBetweenDates(Date date, Date begin, Date end)
	{
		return (date.after(begin) || date.equals(begin)) && (date.before(end) || date.equals(end));
	}

	/**
	 * 获取两个日期之间的date集合
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<Date> getEverDayBetweenDate(Date start, Date end)
	{
		if(start == null || end == null)
			return null;

		List<Date> list = new ArrayList<>();
		list.add(start);

		int i = 1;
		while(true)
		{
			Date d = addDay(start, i);
			if(!isBetweenDates(d, start, end))
			{
				break;
			}
			else
			{
				list.add(d);
			}
			i++;
		}
		return list;
	}

	/**
	 * 获取两个日期之间的周几
	 * @param weekDay 获取周几
	 * @param start 开始
	 * @param end 结束
	 * @return
	 */
	public static List<Date> getWeekDaysBetweenDate(int weekDay, Date start, Date end)
	{
		if(start == null || end == null)
			return null;

		List<Date> list = new ArrayList<>();
		Date date = getDayOfWeek(start, weekDay);//获取start所在周的周几
		//如果start所在的周几符合标准则加入列表
		if(isBetweenDates(date, start, end))
		{
			list.add(date);
		}

		int i = 1;
		while(true)
		{
			Date d = addWeek(start, i);
			d = getDayOfWeek(d, weekDay);
			if(!isBetweenDates(d, start, end))
			{
				break;
			}
			else
			{
				list.add(d);
			}

			i++;
		}
		return list;
	}

	/**
	 * 获取两个日期之间的每月的几号
	 * @param monthDay
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<Date> getMonthDaysBetweenDate(int monthDay, Date start, Date end)
	{
		if(start == null || end == null)
			return null;

		List<Date> list = new ArrayList<>();
		Date date = getDayOfMonth(start, monthDay);
		if(isBetweenDates(date, start, end))
		{
			list.add(date);
		}
		int i = 1;
		while(true)
		{
			Date d = addMonth(start, i);
			d = getDayOfMonth(d, monthDay);
			if(!isBetweenDates(d, start, end))
			{
				break;
			}
			else
			{
				list.add(d);
			}

			i++;
		}
		return list;
	}

	/**
	 * 判断两个日期是否为同一天
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static boolean isSameDay(Date day1, Date day2)
	{
		if(day1 == null || day2 == null)
			return false;

		String d1 = date2String(day1, dateFormatStr);
		String d2 = date2String(day2, dateFormatStr);
		return d1.equals(d2);
	}
}
