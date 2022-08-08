package com.zcdy.dsc.modules.inspection.constant;

/**
 * @author： 在信汇通
 * 创建时间：2021-01-28
 * 描述: <p>巡检常量定义</p>
 */
public class InspConstant {

    /**
     * 巡检点类型在数据库数据字典表中的code
     */
    public static final String INSP_POINT_TYPE_CODE = "insp_point_type";
    /**
     * 巡检点是否重点在数据库数据字典表中的code
     */
    public static final String INSP_POINT_IMPORTANT_CODE = "insp_point_important";

    /**
     * 巡检工单频次类型在数据库数据字典表中的code
     */
    public static final String WORK_LIST_INSP_PLAN_TYPE_CODE = "work_list_insp_type";
    /**
     * 巡检计划状态在数据库数据字典表中的code
     */
    public static final String INSP_PLAN_STATUS_CODE = "insp_plan_status";
    /**
     * 巡检计划状态：启用
     */
    public static final String INSP_PLAN_STATUS_ENABLE = "1";
    /**
     * 巡检计划状态：停用
     */
    public static final String INSP_PLAN_STATUS_DISABLE = "0";

    /**
     * 巡检计划频次类型：每天
     */
    public static final String INSP_PLAN_FREQUENCY_TYPE_DAY = "0";
    /**
     * 巡检计划频次类型：每周
     */
    public static final String INSP_PLAN_FREQUENCY_TYPE_WEEK = "1";
    /**
     * 巡检计划频次类型：每月
     */
    public static final String INSP_PLAN_FREQUENCY_TYPE_MONTH = "2";


}
