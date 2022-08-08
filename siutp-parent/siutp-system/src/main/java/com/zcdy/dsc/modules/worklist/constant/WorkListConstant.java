package com.zcdy.dsc.modules.worklist.constant;

/**
 * @Description: 工单管理常量
 * @Author: 在信汇通
 * @Date:   2021-03-02
 * @Version: V1.0
 */
public class WorkListConstant {

    /**
     * 工单问题类型
     */
    public static final String WORK_LIST_MATTER_TYPE = "work_list_matter_type";

    /**
     * 我的工单小程序日期查询条件列表
     */
    public static final String WORK_LIST_APP_DATE_SEARCH_LIST = "my_work_list_app_date_search_list";
    /**
     * 我的工单小程序日期查询条件：全部
     */
    public static final String WORK_LIST_APP_DATE_SEARCH_LIST_ALL = "all";
    /**
     * 我的工单小程序日期查询条件：本周
     */
    public static final String WORK_LIST_APP_DATE_SEARCH_LIST_WEEK = "week";
    /**
     * 我的工单小程序日期查询条件：本月
     */
    public static final String WORK_LIST_APP_DATE_SEARCH_LIST_MONTH = "month";
    /**
     * 我的工单小程序日期查询条件：今年
     */
    public static final String WORK_LIST_APP_DATE_SEARCH_LIST_YEAR = "year";

    /**
     * 工单问题状态
     */
    public static final String WORK_LIST_MATTER_STATUS = "work_list_matter_status";

    /**
     * 工单问题默认状态
     */
    public static final String WORK_LIST_MATTER_STATUS_DEFAULT = "1";
    /**
     * 工单问题状态：已完成
     */
    public static final String WORK_LIST_MATTER_STATUS_COMPLETE = "3";
    /**
     * 工单问题状态：未完成
     */
    public static final String WORK_LIST_MATTER_STATUS_INCOMPLETE = "4";

    /**
     * 工单问题已分配状态
     */
    public static final String WORK_LIST_MATTER_STATUS_ALLOT = "2";

    /**
     * 工单默认状态
     */
    public static final String WORK_LIST_STATUS_DEFAULT = "1";
    /**
     * 工单状态：已分配
     */
    public static final String WORK_LIST_STATUS_ALLOT = "2";
    /**
     * 工单状态：已完成
     */
    public static final String WORK_LIST_STATUS_COMPLETE = "3";
    /**
     * 工单状态：未完成
     */
    public static final String WORK_LIST_STATUS_INCOMPLETE = "4";
    /**
     * 工单状态在数据字典中的code
     */
    public static final String WORK_LIST_STATUS_CODE = "work_list_status";
    /**
     * 工单类型在数据字典中的code
     */
    public static final String WORK_LIST_TYPE_CODE = "work_list_type";

    /**
     * 问题工单类型编码
     */
    public static final String WORK_LIST_TYPE_MATTER = "3";
    /**
     * 维养工单类型编码
     */
    public static final String WORK_LIST_TYPE_KEEP = "2";
    /**
     * 巡检工单类型编码
     */
    public static final String WORK_LIST_TYPE_INSP = "1";
    /**
     * 工单任务问题类型在数据字典中的编码
     */
    public static final String WORK_LIST_MATTER__MATTER_TYPE_CODE = "matter_type";
    /**
     * 工单任务问题等级在数据字典中的编码
     */
    public static final String WORK_LIST_MATTER__MATTER_LEVEL_CODE = "matter_level";

    /**
     * 我的工单查询条件：进行中
     */
    public static final String MY_WORK_LIST_STATUS_DOING = "doing";
    /**
     * 我的工单查询条件：已完成
     */
    public static final String MY_WORK_LIST_STATUS_FINISH = "finish";
}
