package com.zcdy.dsc.modules.rdp.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @Package: com.fhzm.common
 * @Author: 曹帅华
 * @CreateTime: 2021/4/28 16:01
 * @Description: 数字运算工具类
 */
public class NumberUtil {

    /**
     * 计算百分比值
     * 占比 = 所求占比例数值 / 总数值
     * @param proportionNumber 当前值
     * @param totalNumber 总值
     * @return
     */
    public static String percentage(int proportionNumber, int totalNumber) {
        if (0 == proportionNumber && 0 == totalNumber) return "0%";
        double baiy = proportionNumber * 1.0;
        double baiz = totalNumber * 1.0;
        double fen = baiy / baiz;
        DecimalFormat df = new DecimalFormat("##%");
        return df.format(fen);
    }

    /**
     * 计算百分比值
     * @param proportionNumber
     * @param totalNumber
     * @return
     */
    public static int percentageInt(int proportionNumber, int totalNumber) {
        if (0 == proportionNumber && 0 == totalNumber) return 0;
        double baiy = proportionNumber * 1.0;
        double baiz = totalNumber * 1.0;
        double fen = baiy / baiz;
        DecimalFormat df = new DecimalFormat("##%");
        return Integer.parseInt(df.format(fen).replace("%", ""));
    }

    /**
     * 获取占比
     * @param proportionNumber
     * @param totalNumber
     * @return
     */
    public static double percentageResult(int proportionNumber, int totalNumber) {
        if (0 == proportionNumber && 0 == totalNumber) return 0;
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        return new Double(numberFormat.format(((double) proportionNumber / (double) totalNumber) * 100));
    }
}
