package com.zcdy.dsc.modules.rdp.constant;

/**
 * 描述: RDP大屏设计表格模板
 *
 * @author: 在信汇通
 * 创建时间:  2020年12月11日 下午3:26:26
 * 版本: V1.0
 */
public interface TableConstant {
    /**
     * RDP实时预警告警表头数据
     */
    public static final String[] REAL_TIME_EARLY_WARNING_KEY = {"warnLevel", "equipmentType", "warnContent", "warnTime"};
    /**
     * RDP实时预警告警表头名字
     */
    public static final String[] REAL_TIME_EARLY_WARNING_TEXT = {"等级", "类型", "地址", "时间"};
    /**
     * RDP工单问题记录表头数据
     */
    public static final String[] WORK_JOB_KEY = {"problemName", "problemStatus", "personInCharge"};
    /**
     * RDP工单问题记录表头名字
     */
    public static final String[] WORK_JOB_TEXT = {"本周问题", "状态", "负责人"};
    /**
     * RDP水质旭日图I层数据
     */
    public static final String[] WATER_QUALITY_TYPE = {"Ⅰ类", "Ⅱ类", "Ⅲ类", "Ⅳ类", "Ⅴ类"};
    /**
     * RDP水质旭日图II层数据
     */
    public static final String WATER_QUALITY_9 = "九参数";
    /**
     * RDP水质旭日图II层数据
     */
    public static final String WATER_QUALITY_7 = "七参数";
    /**
     * 水文监测TYPE
     */
    public static final String HYDROLOGY_TYPE = "A03A14";
    /**
     * 水质监测TYPE
     */
    public static final String WATER_TYPE = "A03A13";
    /**
     * 视频监控TYPE
     */
    public static final String VIDEO_SURVEILLANCE_TYPE = "A03A17";
    /**
     * 分类字典中项目CODE
     */
    public static final String PROJECT_CODE = "Z05";
    /**
     * 机房设备TYPE
     */
    public static final String ENGINE_TYPE = "A03A16";
    /**
     * 广播设备TYPE
     */
    public static final String BROADCAST_TYPE = "A03A18";
}
