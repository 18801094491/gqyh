 package com.zcdy.dsc.modules.collection.iot.constant;

 /**
  * 告警事件分类枚举常量
 * @author Roberto
 * @date 2020/05/11
 */
public class WarnClassificationConstant {

    private WarnClassificationConstant(){}

    /**
     * 历史数据未定义
     */
    public static final String UNDEFINED = "0";
    
    /**
     * 离线状态
     */
    public static final String OFFLINE = "1";
    
    /**
     * 数据告警
     */
    public static final String DATAALARM = "2";
    
    /**
     * 未知
     */
    public static final String UNKNOWN = "3";
}
