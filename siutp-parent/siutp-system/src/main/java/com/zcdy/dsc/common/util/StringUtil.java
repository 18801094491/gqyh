package com.zcdy.dsc.common.util;

import java.util.List;

/**
 * @Description: 字符串工具类
 * @Author: 在信汇通
 * @Date: 2021-01-14 16:03:29
 * @Version: V1.0
 */
public class StringUtil {
    /**
     * 判断是否为空
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj) {
        boolean flag = false;
        if (null == obj || "".equals(obj))
            flag = true;
        return flag;

    }

    /**
     * 判断是否为空
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        if (null == str || "".equals(str.trim()))
            return true;
        return false;
    }

    /**
     * 判断是否为空
     * @param list
     * @return
     */
    public static boolean isNull(List list) {
        if (null == list || list.size() == 0)
            return true;
        return false;
    }

    /**
     * 判断是否为空
     * @param str
     * @return
     */
    public static boolean isNull(String[] str) {
        if (null == str || str.length == 0)
            return true;
        return false;
    }

    /**
     * 判断是否不为空
     * @param obj
     * @return
     */
    public static boolean isNotNull(Object obj) {
        boolean flag = true;
        if (null == obj || "".equals(obj))
            flag = false;
        return flag;
    }

    /**
     * 判断是否不为空
     * @param list
     * @return
     */
    public static boolean isNotNull(List list) {
        if (null != list && list.size() > 0)
            return true;
        return false;
    }

    /**
     * 判断是否不为空
     * @param str
     * @return
     */
    public static boolean isNotNull(String str) {
        if (null != str && str.trim().length() > 0)
            return true;
        return false;
    }

    /**
     * 判断是否不为空
     * @param str
     * @return
     */
    public static boolean isNotNull(String[] str) {
        if (null != str && str.length > 0)
            return true;
        return false;
    }
}
