package com.zcdy.dsc.modules.operation.alarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.constant.ShowConstant;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.constant.ProcessRouteConstant;
import com.zcdy.dsc.modules.operation.alarm.constant.WarnStatusConstant;
import com.zcdy.dsc.modules.operation.alarm.entity.*;
import com.zcdy.dsc.modules.operation.alarm.param.BusinessWarnParam;
import com.zcdy.dsc.modules.operation.alarm.vo.*;
import com.zcdy.dsc.modules.operation.alarm.vo.PolicyListVo.PolicyUser;
import com.zcdy.dsc.modules.operation.equipment.entity.OptEquipment;
import com.zcdy.dsc.modules.operation.work.vo.TeamDropdown;
import com.zcdy.dsc.modules.operation.work.vo.WorkPointDropdown;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * 描述: 报警相关dao层
 *
 * @author： songguang.jiao
 * 创建时间： 2020年2月18日 上午11:06:37
 * 版本号: V1.0
 */
public interface BusinessDao extends BaseMapper<BusinessWarn> {


    /**
     * 分页查询报警数据 排序告警等级跟时间
     * @param page 分页参数
     * @param warnName 告警名称
     * @param warnLevel 告警等级
     * @param warnType 告警类型
     * @param warnStatus 状态
     * @param equipmentType 设备类型
     * @param startTime 开始时间
     * @param endTime 截至时间
     * @return
     */
    @Select({
            " <script>",
            " select",
            " t.id id,",
            " t1.opt_id optId,",
            " t.warn_sn warnSn,",
            " t.confirm_status confirmStatus,",
            " t.warn_name warnName,",
            " t.warn_level warnLevelCode,",
            " t.warn_content warnContent,",
            " t.rule_content ruleContent,",
            " CONCAT((SELECT t3.name from sys_category t3 where t3.code=t2.equipment_type),'[',t2.equipment_sn,']') as equipmentType,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_level' ) AND item_value = t.warn_level ) warnLevel,",
            " t.warn_type warnTypeCode,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_type' ) AND item_value = t.warn_type ) warnType,",
            " t.warn_time warnTime,",
            " t.warn_status warnStatusCode,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_status' ) AND item_value = t.warn_status ) warnStatus,",
            " t.warn_way warnWayCode,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_way' ) AND item_value = t.warn_way ) warnWay,",
            " t.operate_time operateTime,",
            " t.duration duration,",
            " t.operator operatorId,",
            " (SELECT realname from sys_user where id=t.operator) operator,",
            " t.description ",
            " FROM	",
            " business_warn t",
            " LEFT JOIN iot_opt_equipment t1 on t1.iot_id=t.iot_id",
            " LEFT JOIN opt_equipment t2 on t2.id=t1.opt_id",
            " where 1=1 " +
                "AND t.warn_level IN ( SELECT warn_level_code FROM iot_equipment_rule_warn WHERE type = '0' ) ",
            " <if test='warnName!=null and warnName!=\"\"'>",
            "  and t.warn_name like concat('%',#{warnName},'%')",
            " </if>",
            " <if test='warnLevel!=null and warnLevel!=\"\"'>",
            " and t.warn_level=#{warnLevel} ",
            " </if>",
            " <if test='warnType!=null and warnType!=\"\"'>",
            " and t.warn_type=#{warnType} ",
            " </if>",
            " <if test='warnStatus!=null and warnStatus!=\"\"'>",
            " and t.warn_status=#{warnStatus} ",
            " </if>",
            " <if test='equipmentType!=null and equipmentType!=\"\"'>",
            " and t2.equipment_type=#{equipmentType} ",
            " </if>",
            " <if test='startTime!=null and startTime!=\"\"'>",
            " and  date_format(t.warn_time,'%Y-%m-%d %H:%i')&gt;=#{startTime}",
            " </if>",
            " <if test ='endTime!=null and endTime!=\"\"'>",
            " and  date_format(t.warn_time,'%Y-%m-%d %H:%i')&lt;=#{endTime}",
            " </if>",
            " ORDER BY t.warn_status asc,t.warn_level desc,t.warn_time desc",
            " </script>",
    })
    IPage<BusinessWarnVo> queryWarnData(Page<BusinessWarnVo> page, @Param("warnName") String warnName,
                                        @Param("warnLevel") String warnLevel, @Param("warnType") String warnType,
                                        @Param("warnStatus") String warnStatus, @Param("equipmentType") String equipmentType,
                                        @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 描述: 关闭消息
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月18日 下午2:38:37
     * 版本号: V1.0
     */
    @Update({
            " UPDATE `business_warn` ",
            " SET `confirm_status` = " + WarnStatusConstant.CLOSED,
            "  ,`warn_status` = " + WarnStatusConstant.CLOSED,
            " ,`operate_time` = #{date},",
            " `duration` = #{duration},",
            " `operator` = #{userId},",
            " `description` = #{description} ,",
            " process_route = " + ProcessRouteConstant.ROUTE_MANUAL,
            " WHERE",
            " `id` = #{id};",
    })
    void closeData(@Param("id") String id, @Param("description") String description,
                   @Param("userId") String userId, @Param("date") Date date, @Param("duration") Long duration);


    /**
     * 描述: 批量插入报警新表
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月20日 下午1:59:03
     * 版本号: V1.0
     */
    @Insert({
            " <script>",
            " INSERT INTO `business_warn` ( `id`, `warn_sn`, `warn_name`, `warn_level`, `warn_type`, `warn_time`, `warn_status`, `warn_way` )",
            " VALUES",
            " <foreach collection='list' item='item' index='index' separator=','>",
            " (",
            " #{item.id},",
            " #{item.warnSn},",
            " #{item.warnName},",
            " #{item.warnLevel},",
            " #{item.warnType},",
            " #{item.warnTime},",
            " #{item.warnStatus},",
            " #{item.warnWay}",
            " )",
            " </foreach>",
            " </script>",
    })
    void insertWarnInfo(@Param("list") List<BusinessWarn> list);

    /**
     * 描述: 查询所有变量信息
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月20日 下午4:38:41
     * 版本号: V1.0
     */
    @Select({
            " SELECT",
            " ( SELECT NAME FROM sys_category WHERE CODE = t2.model_type_code ) typeName,",
            " t2.model_name modelName,",
            " variable_name varName,",
            " variable_title varTitle,",
            " variable_unit united",
            " FROM",
            " iot_variable_info t",
            " LEFT JOIN model_variable t1 ON t.id = t1.variable_id",
            " LEFT JOIN gis_model t2 ON t2.id = t1.model_id",
    })
    List<VariInfo> selectAllVar();


    /**
     * 描述: 校验报警信息是否存在
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月20日 下午6:18:20
     * 版本号: V1.0
     */
    @Select("SELECT count(id) from business_warn where warn_name=#{warnName}  and DATE(warn_time) =CURRENT_DATE and warn_status='1'")
    boolean checkWarnExist(@Param("warnName") String warnName);

    /**
     * 描述: 查询未处理的当天报警id
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月21日 上午11:17:38
     * 版本号: V1.0
     */
    @Select("SELECT id from business_warn where warn_name like CONCAT(#{warnName},'%')  and DATE(warn_time) =CURRENT_DATE and warn_status='1'")
    String getWarnId(@Param("warnName") String warnName);

    /**
     * 描述: 批量更新报警数据
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月21日 上午11:28:16
     * 版本号: V1.0
     */
    @Update({
            " <script>",
            " <foreach collection='list' item='item' index='index' separator=';'>",
            " update business_warn",
            " <set>",
            " <if test='item.warnName!=null'>",
            " warn_name=#{item.warnName}",
            " </if>",
            " </set>",
            " where id=#{item.id}",
            " </foreach>",
            " </script>",
    })
    void updateWarnInfo(@Param("list") List<BusinessWarn> list);

    /**
     * 描述: 确认按钮
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月24日 上午11:15:50
     * 版本号: V1.0
     */
    @Update({
            " UPDATE `business_warn` ",
            " SET `confirm_status` = #{warnStatus}, ",
            "  warn_status = " + WarnStatusConstant.UNDEAL,
            " WHERE",
            " `id` = #{id};",
    })
    void confirmData(@Param("id") String id, @Param("warnStatus") String warnStatus);

    /**
     * 描述: 查询未处理的告警事件
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月28日 下午2:22:27
     * 版本号: V1.0
     */
    @Select({
            " select",
            " t.id id,",
            " t.warn_sn warnSn,",
            " t.confirm_status confirmStatus,",
            " t.warn_name warnName,",
            " t.warn_level warnLevelCode,",
            " t.warn_content warnContent,",
            " t.rule_content ruleContent,",
            " (SELECT t3.name from sys_category t3 where t3.code=t2.equipment_type) as equipmentType,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_level' ) AND item_value = t.warn_level ) warnLevel,",
            " t.warn_type warnTypeCode,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_type' ) AND item_value = t.warn_type ) warnType,",
            " t.warn_time warnTime,",
            " t.warn_status warnStatusCode,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_status' ) AND item_value = t.warn_status ) warnStatus,",
            " t.warn_way warnWayCode,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_way' ) AND item_value = t.warn_way ) warnWay,",
            " t.operate_time operateTime,",
            " t.duration duration,",
            " t.operator operatorId,",
            " (SELECT realname from sys_user where id=t.operator) operator,",
            " t.description ",
            " FROM	",
            " business_warn t",
            " LEFT JOIN iot_opt_equipment t1 on t1.iot_id=t.iot_id",
            " LEFT JOIN opt_equipment t2 on t2.id=t1.opt_id" +
                    " left join iot_equipment ie on t.iot_id=ie.id",
            " where  t.warn_status=#{undeal2}" +
                    " AND t.warn_level IN ( select warn_level_code from iot_equipment_rule_warn where type='0'   )" +
                    " and ie.iot_category IN ( select warn_level_code from iot_equipment_rule_warn where type='1'  ) "
    })
    List<BusinessWarnVo> queryUndealData(@Param("undeal1") String undeal1, @Param("undeal2") String undeal2);

    /**
     * 描述: 查询未处理的告警事件（新）
     *
     * @author： 在信汇通
     * 创建时间： 2021年6月15日
     * 版本号: V1.0
     */
    @Select({
            " select",
            " t.id id,",
            " t.warn_sn warnSn,",
            " t.confirm_status confirmStatus,",
            " t.warn_name warnName,",
            " t.warn_level warnLevelCode,",
            " t.warn_content warnContent,",
            " t.rule_content ruleContent,",
            " CONCAT(ifnull(s.name,''), '-', ifnull(t2.equipment_location, '')) equ, ",
            " (SELECT t3.name from sys_category t3 where t3.code=t2.equipment_type) as equipmentType,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_level' ) AND item_value = t.warn_level ) warnLevel,",
            " t.warn_type warnTypeCode,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_type' ) AND item_value = t.warn_type ) warnType,",
            " t.warn_time warnTime,",
            " t.warn_status warnStatusCode,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_status' ) AND item_value = t.warn_status ) warnStatus,",
            " t.warn_way warnWayCode,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_way' ) AND item_value = t.warn_way ) warnWay,",
            " t.operate_time operateTime,",
            " t.duration duration,",
            " t.operator operatorId,",
            " (SELECT realname from sys_user where id=t.operator) operator,",
            " t.description ",
            " FROM	",
            " business_warn t",
            " LEFT JOIN iot_opt_equipment t1 on t1.iot_id=t.iot_id",
            " LEFT JOIN opt_equipment t2 on t2.id=t1.opt_id" +
                    " left join sys_category s on s.code = t2.equipment_section" +
                    " left join iot_equipment ie on t.iot_id=ie.id",
            " where  1=1 " +
                    //" and (t.warn_status=#{undeal1} or t.warn_status=#{undeal2}) " +
                    " AND t.warn_level IN ( select warn_level_code from iot_equipment_rule_warn where type='0'   )" +
                    " and ie.iot_category IN ( select warn_level_code from iot_equipment_rule_warn where type='1'  ) "
    })
    List<BusinessWarnVo> queryUndealDataNew(@Param("undeal1") String undeal1, @Param("undeal2") String undeal2);

    @Select("select warn_level warnLevelCode, count(*) num, " +
            "(SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_level' ) AND item_value = warn_level ) warn_level\n" +
            "from business_warn \n" +
            "where (warn_status = '1' or warn_status = '0') " +
            "group by warnLevelCode")
    List<BusinessWarnVo> queryWarnNum();

    /**
     * 分页全部告警事件列表
     * @param page 分页对象
     * @param param 列表参数
     * @return
     */
    @Select({
            "<script>" +
            "        SELECT\n" +
                    "            t.id id,\n" +
                    "            t.warn_sn warnSn,\n" +
                    "            t.confirm_status confirmStatus,\n" +
                    "            t.warn_name warnName,\n" +
                    "            t.warn_level warnLevelCode,\n" +
                    "            t.warn_content warnContent,\n" +
                    "            t.rule_content ruleContent,\n" +
                    "            ( SELECT t3.NAME FROM sys_category t3 WHERE t3.CODE = t2.equipment_type ) equipmentType,\n" +
                    "            ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_level' ) AND item_value = t.warn_level ) warnLevel,\n" +
                    "            t.warn_type warnTypeCode,\n" +
                    "            ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_type' ) AND item_value = t.warn_type ) warnType,\n" +
                    "            t.warn_time warnTime,\n" +
                    "            t.warn_status warnStatusCode,\n" +
                    "            ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_status' ) AND item_value = t.warn_status ) warnStatus,\n" +
                    "            t.warn_way warnWayCode,\n" +
                    "            ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_way' ) AND item_value = t.warn_way ) warnWay,\n" +
                    "            t.operate_time operateTime,\n" +
                    "            t.duration duration,\n" +
                    "            t.operator operatorId,\n" +
                    "            ( SELECT realname FROM sys_user WHERE id = t.operator ) operator,\n" +
                    "            t.description \n" +
                    "        FROM\n" +
                    "        business_warn t\n" +
                    "        LEFT JOIN iot_opt_equipment t1 ON t1.iot_id = t.iot_id\n" +
                    "        LEFT JOIN opt_equipment t2 ON t2.id = t1.opt_id\n" +
                    "        LEFT JOIN iot_equipment ie ON t.iot_id = ie.id \n" +
                    "        <where>\n" +
                    "            <if test=\"param.warnStatus != null and param.warnStatus != ''\">\n" +
                    "                AND t.warn_status = #{param.warnStatus}\n" +
                    "            </if>\n" +
                    "            <if test=\"param.warnLevel != null and param.warnLevel != ''\">\n" +
                    "                AND t.warn_level = #{param.warnLevel}\n" +
                    "            </if>\n" +
                    "            AND ie.iot_category IN ( SELECT warn_level_code FROM iot_equipment_rule_warn WHERE type = '1' )\n" +
                    "        </where>\n" +
                    "        ORDER BY t.warn_time DESC" +
            "</script>"
    })
    IPage<BusinessWarnVo> getAppBusinessPageList(Page<BusinessWarnVo> page, @Param("param") BusinessWarnParam param);

    /**
     * 查询全部告警事件列表
     * @param param 列表参数
     * @return
     */
    @Select({
            "<script>" +
                    "        SELECT\n" +
                    "            t.id id,\n" +
                    "            t.warn_sn warnSn,\n" +
                    "            t.confirm_status confirmStatus,\n" +
                    "            t.warn_name warnName,\n" +
                    "            t.warn_level warnLevelCode,\n" +
                    "            t.warn_content warnContent,\n" +
                    "            t.rule_content ruleContent,\n" +
                    "            ( SELECT t3.NAME FROM sys_category t3 WHERE t3.CODE = t2.equipment_type ) equipmentType,\n" +
                    "            ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_level' ) AND item_value = t.warn_level ) warnLevel,\n" +
                    "            t.warn_type warnTypeCode,\n" +
                    "            ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_type' ) AND item_value = t.warn_type ) warnType,\n" +
                    "            t.warn_time warnTime,\n" +
                    "            t.warn_status warnStatusCode,\n" +
                    "            ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_status' ) AND item_value = t.warn_status ) warnStatus,\n" +
                    "            t.warn_way warnWayCode,\n" +
                    "            ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_way' ) AND item_value = t.warn_way ) warnWay,\n" +
                    "            t.operate_time operateTime,\n" +
                    "            t.duration duration,\n" +
                    "            t.operator operatorId,\n" +
                    "            ( SELECT realname FROM sys_user WHERE id = t.operator ) operator,\n" +
                    "            t.description \n" +
                    "        FROM\n" +
                    "        business_warn t\n" +
                    "        LEFT JOIN iot_opt_equipment t1 ON t1.iot_id = t.iot_id\n" +
                    "        LEFT JOIN opt_equipment t2 ON t2.id = t1.opt_id\n" +
                    "        LEFT JOIN iot_equipment ie ON t.iot_id = ie.id \n" +
                    "        <where>\n" +
                    "            <if test=\"param.warnStatus != null and param.warnStatus != ''\">\n" +
                    "                AND t.warn_status = #{param.warnStatus}\n" +
                    "            </if>\n" +
                    "            <if test=\"param.warnLevel != null and param.warnLevel != ''\">\n" +
                    "                AND t.warn_level = #{param.warnLevel}\n" +
                    "            </if>\n" +
                    "            AND ie.iot_category IN ( SELECT warn_level_code FROM iot_equipment_rule_warn WHERE type = '1' )\n" +
                    "        </where>\n" +
                    "        ORDER BY t.warn_time DESC" +
                    "</script>"
    })
    List<BusinessWarnVo> getAppBusinessList(@Param("param") BusinessWarnParam param);

    /**
     * 查询告警事件列表详情
     * @param id 告警事件id
     * @return
     */
    @Select({
                    "        SELECT\n" +
                    "            t.id id,\n" +
                    "            t.warn_sn warnSn,\n" +
                    "            t.confirm_status confirmStatus,\n" +
                    "            t.warn_name warnName,\n" +
                    "            t.warn_level warnLevelCode,\n" +
                    "            t.warn_content warnContent,\n" +
                    "            t.rule_content ruleContent,\n" +
                    "            ( SELECT t3.NAME FROM sys_category t3 WHERE t3.CODE = t2.equipment_type ) equipmentType,\n" +
                    "            ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_level' ) AND item_value = t.warn_level ) warnLevel,\n" +
                    "            t.warn_type warnTypeCode,\n" +
                    "            ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_type' ) AND item_value = t.warn_type ) warnType,\n" +
                    "            t.warn_time warnTime,\n" +
                    "            t.warn_status warnStatusCode,\n" +
                    "            ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_status' ) AND item_value = t.warn_status ) warnStatus,\n" +
                    "            t.warn_way warnWayCode,\n" +
                    "            ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_way' ) AND item_value = t.warn_way ) warnWay,\n" +
                    "            t.operate_time operateTime,\n" +
                    "            t.duration duration,\n" +
                    "            t.operator operatorId,\n" +
                    "            ( SELECT realname FROM sys_user WHERE id = t.operator ) operator,\n" +
                    "            t.description \n" +
                    "        FROM\n" +
                    "        business_warn t\n" +
                    "        LEFT JOIN iot_opt_equipment t1 ON t1.iot_id = t.iot_id\n" +
                    "        LEFT JOIN opt_equipment t2 ON t2.id = t1.opt_id\n" +
                    "        LEFT JOIN iot_equipment ie ON t.iot_id = ie.id \n" +
                    "        WHERE t.id = #{id} \n"
    })
    BusinessWarnVo getAppBusinessDetail(@Param("id") String id);

    /**
     * 描述: 查询近7天数据
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月28日 下午4:36:16
     * 版本号: V1.0
     */
    @Select({
            " SELECT",
            " a.click_date AS date,",
            " IFNULL( b.sumNum, 0 ) AS sumNum,",
            " IFNULL( b.unDealNum, 0 ) AS unDealNum,",
            " IFNULL( b.dealNum, 0 ) AS dealNum ",
            " FROM",
            " ( SELECT CURRENT_DATE AS click_date UNION ALL SELECT date_sub( CURRENT_DATE, INTERVAL 1 DAY ) AS click_date UNION ALL SELECT date_sub( CURRENT_DATE, INTERVAL 2 DAY ) AS click_date UNION ALL SELECT date_sub( CURRENT_DATE, INTERVAL 3 DAY ) AS click_date UNION ALL SELECT date_sub( CURRENT_DATE, INTERVAL 4 DAY ) AS click_date UNION ALL SELECT date_sub( CURRENT_DATE, INTERVAL 5 DAY ) AS click_date UNION ALL SELECT date_sub( CURRENT_DATE, INTERVAL 6 DAY ) AS click_date ) a",
            " LEFT JOIN",
            " (",
            " SELECT",
            " DATE_FORMAT( t.warn_time, '%Y-%m-%d' ) AS date,",
            " ( SELECT count( id ) FROM business_warn WHERE DATE_FORMAT( warn_time, '%Y-%m-%d' ) = DATE_FORMAT( t.warn_time, '%Y-%m-%d' ) ) sumNum,",
            " ( SELECT count( id ) FROM business_warn WHERE warn_status = '1' AND DATE_FORMAT( warn_time, '%Y-%m-%d' ) = DATE_FORMAT( t.warn_time, '%Y-%m-%d' ) ) unDealNum,",
            " ( SELECT count( id ) FROM business_warn WHERE warn_status = '2' AND DATE_FORMAT( warn_time, '%Y-%m-%d' ) = DATE_FORMAT( t.warn_time, '%Y-%m-%d' ) ) dealNum ",
            " FROM",
            " business_warn t ",
            " WHERE",
            " DATE_SUB( CURRENT_DATE, INTERVAL 7 DAY ) < date( t.warn_time ) ",
            " GROUP by",
            " DATE_FORMAT( t.warn_time, '%Y-%m-%d' )",
            " ) b",
            " ON a.click_date = b.date",
            " ORDER BY a.click_date"
    })
    List<WeekData> getSevenDayData();

    /**
     * 描述: 安装年份查询报警数据
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月28日 下午6:01:08
     * 版本号: V1.0
     */
    @Select({
            " SELECT",
            " DATE_FORMAT( t.warn_time, '%Y-%m' ) AS time,",
            " ( SELECT count( id ) FROM business_warn WHERE DATE_FORMAT( t.warn_time, '%Y-%m' ) = DATE_FORMAT( warn_time, '%Y-%m' ) ) AS sumNum ",
            " FROM",
            " business_warn t ",
            " WHERE",
            " CONCAT(year(now()),'-01-01') < date( t.warn_time ) ",
            " GROUP BY",
            " DATE_FORMAT( t.warn_time, '%Y-%m' )",
    })
    List<MonthYearData> queryByYear();

    /**
     * 描述: 安装月份查询报警数据
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月28日 下午6:01:08
     * 版本号: V1.0
     */
    @Select({
            " SELECT",
            " DATE_FORMAT( t.warn_time, '%Y-%m-%d' ) AS time,",
            " ( SELECT count( id ) FROM business_warn WHERE DATE_FORMAT( t.warn_time, '%Y-%m-%d' ) = DATE_FORMAT( warn_time, '%Y-%m-%d' ) ) AS sumNum ",
            " FROM",
            " business_warn t",
            " WHERE",
            " DATE_ADD(CURRENT_DATE,interval -day(curdate())+1 day) < date( t.warn_time ) ",
            " GROUP BY",
            " DATE_FORMAT( t.warn_time, '%Y-%m-%d' )",
    })
    List<MonthYearData> queryByMonth();

    /**
     * 描述: 批量插入用户信息
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月4日 上午11:28:34
     * 版本号: V1.0
     */
    @Insert({
            " <script>",
            " INSERT INTO `business_warn_policy_users` ( `id`, `policy_id`, `user_choose_type`, `data_id` )",
            " VALUES",
            " <foreach collection='list' item='item' index='index' separator=','>",
            " (",
            " #{item.id},",
            " #{item.policyId},",
            " #{item.userChooseType},",
            " #{item.dataId}",
            " )",
            " </foreach>",
            " </script>",
    })
    void insertBatch(@Param("list") List<BusinessWarnPolicyUsers> list);

    /**
     * 描述: 查询所有角色用户
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月4日 下午3:24:22
     * 版本号: V1.0
     */
    @Select({
            " <script>",
            " SELECT t.id,t.role_name roleName FROM sys_role t where 1=1",
            " <if test='roleName!=null and roleName!=\"\"'>",
            " and t.role_name like concat('%',#{roleName},'%')  ",
            " </if>",
            " </script>",
    })
    List<RoleNameVo> getRoleUsers(@Param("roleName") String roleName);

    /**
     * 描述: 告警策略已关联用户
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月4日 下午3:50:57
     * 版本号: V1.0
     */
    @Select("SELECT user_id  from business_warn_policy_users where policy_id=#{policyId}")
    List<String> queryUserIds(@Param("policyId") String policyId);

    /**
     * 描述: 查询所有启用班组用户
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月4日 下午3:24:22
     * 版本号: V1.0
     */
    @Select({
            " <script>",
            " SELECT t.id,t.team_name teamName FROM work_team t where t.del_flag='0' and t.team_status='1' ",
            " <if test='name!=null and name!=\"\"'>",
            " and t.team_name like concat('%',#{name},'%')  ",
            " </if>",
            " </script>",
    })
    List<TeamDropdown> getWorkUsers(@Param("name") String name);


    /**
     * 描述: 校验策略等级是否存在
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月5日 上午10:20:30
     * 版本号: V1.0
     */
    @Select({
            "<script>",
            " SELECT count(id) from business_warn_policy where warn_level=#{vo.warnLevel} ",
            " <if test='vo.id!=null and vo.id!=\"\"'>",
            " and id !=#{vo.id} ",
            " </if>",
            "</script>"
    })
    boolean checkPolicyExist(@Param("vo") PolicyParamVo vo);

    /**
     * 描述: 删除报警信息存储用户
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月5日 上午10:46:58
     * 版本号: V1.0
     */
    @Delete(" delete from business_warn_policy_users where policy_id=#{policyId}")
    void deletePolicyUsers(@Param("policyId") String policyId);

    /**
     * 描述: 分页查询告警策略列表
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月5日 下午2:23:22
     * 版本号: V1.0
     */
    @Results(id = "queryPolicyPageDatas", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "warnLevel", column = "warnLevel"),
            @Result(property = "list", many = @Many(select = "queryPolicyUser"), column = "id")
    })
    @Select({
            " <script>",
            " SELECT",
            " t.id,",
            " t.NAME name,",
            " t.work_status workStatusCode,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'working_status' ) AND item_value = t.work_status ) workStatus,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_level' ) AND item_value = t.warn_level ) warnLevel",
            " FROM",
            " business_warn_policy t where t.del_flag='0'",
            " <if test='name!=null and name!=\"\"'>",
            "  and t.name like concat('%',#{name},'%')",
            " </if>",
            " </script>",
    })
    IPage<PolicyListVo> queryPolicyPageData(Page<PolicyListVo> page, @Param("name") String name);

    /**
     * 描述: 用户信息数据
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月5日 下午2:37:29
     * 版本号: V1.0
     */
    @Select({
            " SELECT",
            " t.user_choose_type userChooseTypeCode,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'user_choose_type' ) AND item_value = t.user_choose_type ) userChooseType,",
            " ( CASE t.user_choose_type WHEN 0 THEN ( SELECT realname FROM sys_user WHERE id = t.data_id ) WHEN 1 THEN ( SELECT role_name FROM sys_role WHERE id = t.data_id ) ELSE ( SELECT team_name FROM work_team WHERE id = t.data_id ) END  ) AS dataName ",
            " FROM",
            " business_warn_policy_users t ",
            " WHERE",
            " t.policy_id = #{policyId}",
    })
    List<PolicyUser> queryPolicyUser(@Param("policyId") String policyId);

    /**
     * 描述: 通知策略详情
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月5日 下午4:24:39
     * 版本号: V1.0
     */
    @Results(id = "getDetailId", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "list", many = @Many(select = "getDeatilUsers"), column = "id")
    })
    @Select({
            " <script>",
            " SELECT",
            " t.id,",
            " t.NAME name,",
            " t.work_status workStatusCode,",
            " t.warn_level warnLevelCode,",
            " t.sms_template smsTemplateCode,",
            " (SELECT template_name from sys_sms_template where template_code=t.sms_template) smsTemplate,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'working_status' ) AND item_value = t.work_status ) workStatus,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_level' ) AND item_value = t.warn_level ) warnLevel",
            " FROM",
            " business_warn_policy t where t.del_flag='0' and t.id=#{id}",
            " </script>",
    })
    PoliceDetailData getDetail(@Param("id") String id);


    /**
     * 描述: 用户组合信息
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月5日 下午4:39:51
     * 版本号: V1.0
     */
    @Select({
            " SELECT",
            " t.user_choose_type userChooseType,",
            " ( SELECT GROUP_CONCAT( data_id ) FROM business_warn_policy_users WHERE user_choose_type = t.user_choose_type and policy_id=t.policy_id) AS dataId ",
            " FROM",
            " business_warn_policy_users t ",
            " WHERE",
            " t.policy_id = #{id}",
            " GROUP BY	t.user_choose_type",
    })
    List<BusinessWarnPolicyUsers> getDeatilUsers(@Param("id") String id);

    /**
     * 描述: 删除告警通知策略信息
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月5日 下午4:50:06
     * 版本号: V1.0
     */
    @Delete({
            " DELETE from business_warn_policy where id=#{id}",
    })
    void deletePolicy(@Param("id") String id);

    /**
     * 描述: 更改启告警策略停用状态
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月5日 下午5:13:49
     * 版本号: V1.0
     */
    @Update({
            "update business_warn_policy set work_status=(case work_status when 0 then 1 when 1 then 0 else work_status end) where id=#{id}"
    })
    void changeStatus(@Param("id") String id);

    /**
     * 描述: 查询消息模板下拉选择
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月6日 下午3:44:28
     * 版本号: V1.0
     */
    @Select({
            " <script>",
            " SELECT t.template_code code,t.template_name templateName from sys_sms_template t where 1=1 ",
            " <if test='templateName!=null and templateName!=\"\"'>",
            "  and t.template_name like concat('%',#{templateName},'%')",
            " </if>",
            " </script>",
    })
    List<SmsTemplateVo> queryTemplate(@Param("templateName") String templateName);

    /**
     * 描述:  查询所有用户id
     *
     * @param policyId 通知策略id
     * @author： songguang.jiao
     * 创建时间： 2020年3月6日 下午6:56:14
     * 版本号: V1.0
     */
    @Select({
            " SELECT",
            " ( SELECT GROUP_CONCAT( data_id ) FROM business_warn_policy_users WHERE policy_id = #{policyId} AND user_choose_type = '0' ) AS usersId,",
            " ( SELECT GROUP_CONCAT( user_id ) FROM sys_user_role WHERE role_id IN ( SELECT data_id FROM business_warn_policy_users WHERE policy_id = #{policyId} AND user_choose_type = '1' ) ) AS roleUsersId,",
            " ( SELECT GROUP_CONCAT( user_id ) FROM work_team_member WHERE team_id IN ( SELECT data_id FROM business_warn_policy_users WHERE policy_id = #{policyId} AND user_choose_type = '2' ) ) AS teamUsersId ",
            " FROM",
            " business_warn_policy_users t LIMIT 1",
    })
    WarnUsersId queryPolicyAllUserId(@Param("policyId") String policyId);

    /**
     * 描述:  查询当天报警数据(饼状图,未处理,已处理,已关闭)
     *
     * @return
     * @author： songguang.jiao
     * 创建时间： 2020年3月8日 下午2:53:00
     * 版本号: V1.0
     */
    @Select({
            " SELECT",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_status' ) AND item_value = t.warn_status ) item,",
            " ( SELECT count( id ) FROM business_warn WHERE warn_status = t.warn_status AND DATE_FORMAT( warn_time, '%Y-%m-%d' ) = CURRENT_DATE ) AS count",
            " FROM",
            " business_warn t ",
            " WHERE",
            " DATE_FORMAT( t.warn_time, '%Y-%m-%d' ) = CURRENT_DATE ",
            " GROUP BY",
            " t.warn_status",
    })
    List<PolicyOneDayVo> oneDayData();

    /**
     * 描述:  通过等级查询告警通知策略
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月8日 下午8:58:50
     * 版本号: V1.0
     */
    @Select({" select * from business_warn_policy where warn_level =#{warnLevel} limit 1"})
    BusinessWarnPolicy queryPolicyByLevel(@Param("warnLevel") String warnLevel);

    /**
     * 描述:  地图界面查询所有图标列表
     *
     * @return
     * @author： songguang.jiao
     * 创建时间： 2020年3月9日 上午10:10:23
     * 版本号: V1.0
     */
    @Select({
            " SELECT",
            " CONCAT( cat.NAME, ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'gis_image_type' ) AND item_value = t1.image_type ) ) AS name,",
            " t1.img_url as url",
            " FROM",
            " gis_model_res t",
            " LEFT JOIN gis_model_res_item t1 ON t1.res_id = t.id",
            " LEFT JOIN sys_category cat ON cat.CODE = t.model_type_code",
            " where t.legend_show=" + ShowConstant.SHOW,
            " and  t.del_flag=" + StatusConstant.WORKING,
    })
    List<IconListVo> queryAllIcon();

    /**
     * 描述:  查询资产设备信息
     *
     * @param iotId 采集设备id
     * @return
     * @author： songguang.jiao
     * 创建时间： 2020年3月9日 下午7:34:09
     * 版本号: V1.0
     */
    @Select({
            " select * from opt_equipment where id=(SELECT opt_id from iot_opt_equipment where iot_id=#{iotId}) "
    })
    OptEquipment queryEquipment(@Param("iotId") String iotId);

    @Select("SELECT " +
            "t1.iot_id  iotId," +
            "t.id id," +
            "t.warn_sn warnSn," +
            "t.confirm_status confirmStatus," +
            "t.warn_name warnName," +
            "t.warn_level warnLevelCode," +
            "t.warn_content warnContent," +
            "t.rule_content ruleContent," +
            "CONCAT( ( SELECT t3.NAME FROM sys_category t3 WHERE t3.CODE = t2.equipment_type ), '[', t2.equipment_sn, ']' ) AS equipmentType," +
            "( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_level' ) AND item_value = t.warn_level ) warnLevel," +
            "t.warn_type warnTypeCode," +
            "( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_type' ) AND item_value = t.warn_type ) warnType," +
            "t.warn_time warnTime," +
            "t.warn_status warnStatusCode," +
            "( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_status' ) AND item_value = t.warn_status ) warnStatus," +
            "t.warn_way warnWayCode," +
            "( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_way' ) AND item_value = t.warn_way ) warnWay," +
            "t.operate_time operateTime," +
            "t.duration duration," +
            "t.operator operatorId," +
            "( SELECT realname FROM sys_user WHERE id = t.operator ) operator," +
            "t.description " +
            "FROM " +
            "gis_equipment ge " +
            "LEFT JOIN iot_opt_equipment t1 ON ge.equipment_id = t1.opt_id " +
            "LEFT JOIN business_warn t ON t1.iot_id = t.iot_id " +
            "AND ( t.warn_status = '0' OR t.warn_status = '1' ) " +
            "AND (confirm_status = '0' or confirm_status = '1') " +
            "LEFT JOIN opt_equipment t2 ON t2.id = t1.opt_id " +
            "WHERE " +
            "ge.model_id = #{id}")
    List<BusinessWarnVo> getBusinessWarnVoById(String id);

    /**
     * @param startIndex
     * @param pageSize
     * @return 数据列表
     */
    //    @Select({
    //            "select t.id, t.warn_content alarmContent, t.warn_time occurTime,t.rule_content ruleContent,b.`name` optTypeName,c.item_value alarmLevelCode, c.item_text alarmLevel,t.warn_status warnStatus",
    //            "from (select * from business_warn where warn_status in('1','0') ORDER by warn_status, warn_level DESC limit #{startIndex}, #{pageSize}) t ",
    //            "LEFT JOIN iot_equipment iot on t.iot_id = iot.id",
    //            "LEFT JOIN sys_category b on b.`code` = iot.iot_category",
    //            "LEFT JOIN (SELECT item_text,item_value from sys_dict_item item LEFT join sys_dict dic on item.dict_id = dic.id where dic.dict_code='warn_level') c on c.item_value = t.warn_level",
    //    }
    /**
     * @param startIndex
     * @param pageSize
     * @return 数据列表 20200828不要阀门报警
     */
    @Select({
            "select t.id, t.warn_content alarmContent, t.warn_time occurTime,t.rule_content ruleContent,b.`name` optTypeName,c.item_value alarmLevelCode, c.item_text alarmLevel,t.warn_status warnStatus",
            "from (",
            "select w.*,iot.iot_category from business_warn w",
            "inner JOIN iot_equipment iot on w.iot_id = iot.id",
            "where warn_status in('1','0') and iot.iot_category <> 'A03A01'",
            "ORDER by warn_status, warn_level DESC limit #{startIndex}, #{pageSize}",
            ") t",
            "left JOIN sys_category b on b.`code` = t.iot_category",
            "LEFT JOIN (SELECT item_text,item_value from sys_dict_item item LEFT join sys_dict dic on item.dict_id = dic.id where dic.dict_code='warn_level') c on c.item_value = t.warn_level",
    })
    List<AlarmResultVO> selectAlarmFullDataList(@Param("startIndex") Long startIndex, @Param("pageSize") Integer pageSize);

    /**
     * @param pageinfo
     * @param param
     * @return 数据分页列表
     */
    @Select({
            "<script>" +
            "    SELECT\n" +
            "        t.id, t.warn_content alarmContent, t.warn_time occurTime, t.rule_content ruleContent,\n" +
            "        b.`name` optTypeName, c.item_value alarmLevelCode, c.item_text alarmLevel\n" +
            "    FROM business_warn t\n" +
            "        LEFT JOIN iot_equipment iot ON t.iot_id = iot.id\n" +
            "        LEFT JOIN iot_opt_equipment io ON io.iot_id = iot.id\n" +
            "        LEFT JOIN opt_equipment opt ON io.opt_id = opt.id\n" +
            "        LEFT JOIN sys_category ca ON ca.`code` = opt.equipment_section\n" +
            "        LEFT JOIN sys_category b ON b.`code` = iot.iot_category\n" +
            "        LEFT JOIN sys_category d ON d.`code` = opt.equipment_mode\n" +
            "        LEFT JOIN (SELECT item_text, item_value FROM sys_dict_item item LEFT JOIN sys_dict dic ON item.dict_id = dic.id WHERE dic.dict_code = 'warn_level') c ON c.item_value = t.warn_level\n" +
            "        <where>\n" +
            "            <if test=\"param != null and param != ''\">\n" +
            "                and (opt.equipment_sn like concat('%',#{param},'%') or opt.equipment_location like concat('%',#{param},'%') or ca.name like concat('%',#{param},'%'))\n" +
            "            </if>\n" +
            "            <if test=\"equipTypes != null and equipTypes.length > 0\">\n" +
            "                and iot.iot_category in\n" +
            "                <foreach item='item' index='index' collection='equipTypes' separator=',' open='(' close=')'>\n" +
            "                    #{item}\n" +
            "                </foreach>\n" +
            "            </if>\n" +
            "            and t.warn_status in('0','1')\n" +
            "        </where>\n" +
            "        ORDER by t.warn_status, t.warn_level DESC" +
            "</script>"
    })
    Page<AlarmResultVO> selectAlarmFullDataPage(Page<AlarmResultVO> pageinfo, @Param("param") String param, @Param("equipTypes") String[] equipTypes);

    /**
     * @param bid
     * @return 告警详情数据
     */
    @Select({
            "select t.id, t.warn_content alarmContent, t.warn_time occurTime,t.rule_content ruleContent,b.`name` optTypeName,c.item_text alarmLevel, t.iot_id iotId,",
            "    opt.equipment_sn optSn,opt.equipment_name optName,ca.name optSection,d.name optType,opt.equipment_img optPicture,",
            "    opt.equipment_location optPosition, su.supplier_name optSupplier",
            "from business_warn t ",
            "LEFT JOIN iot_equipment iot on t.iot_id = iot.id",
            "LEFT JOIN iot_opt_equipment io on io.iot_id = iot.id",
            "LEFT JOIN opt_equipment opt on io.opt_id = opt.id",
            "LEFT JOIN sys_category ca on ca.`code` = opt.equipment_section",
            "LEFT JOIN opt_supplier su on su.id = opt.equipment_supplier",
            "LEFT JOIN sys_category b on b.`code` = iot.iot_category",
            "LEFT JOIN sys_category d on d.`code` = opt.equipment_mode",
            "LEFT JOIN (SELECT item_text,item_value from sys_dict_item item LEFT join sys_dict dic on item.dict_id = dic.id where dic.dict_code='warn_level') c on c.item_value = t.warn_level",
            "where t.id = #{value}"
    })
    AlarmResultDetailVO selectAlarmDataDetail(String bid);

    /**
     * @param iotId
     * @return 变量信息
     */
    @Select({
            "SELECT",
            "    t.variable_name varName,",
            "    t.variable_title varTitle,",
            "    t.variable_unit united,",
            "    t.scale,",
            "    t.variable_type variableType,",
            "    b.display_order displayOrder",
            "FROM",
            "    iot_variable_info t",
            "LEFT JOIN model_variable b ON t.id = b.variable_id",
            "LEFT JOIN gis_equipment c ON c.model_id = b.model_id",
            "LEFT JOIN iot_opt_equipment d ON d.opt_id = c.equipment_id",
            "WHERE d.iot_id = #{value}",
            "ORDER BY display_order DESC"
    })
    List<VariableInfo> selectVarsByIotId(String iotId);

    /**
     * 今日各个级别告警统计
     *
     * @return
     * @author songguang.jiao
     * @date 2020/05/29 11:11:59
     */
    @Select({
            " SELECT",
            " t.item_text item,",
            " (SELECT count(id) FROM business_warn WHERE warn_level = t.item_value AND DATE_FORMAT( warn_time, '%Y-%m-%d' ) = CURDATE() ) AS count",
            " FROM v_sys_dict_item t",
            " WHERE t.dict_code = 'warn_level'",
    })
    List<PolicyOneDayVo> selectWarnLevelInfoToday();

    /**
     * 查询未处理告警下拉选
     *
     * @param name
     * @return
     */
    @Select({
            " <script>",
            " SELECT",
            " t.id,",
            " t.warn_name name,",
            " t4.latitude,",
            " t3.equipment_id equipmentId,",
            " t4.longitude",
            " FROM",
            " business_warn t",
            " LEFT JOIN iot_opt_equipment t2 on t2.iot_id=t.iot_id",
            " LEFT JOIN gis_equipment t3 on t3.equipment_id=t2.opt_id",
            " LEFT JOIN gis_model t4 on t4.id=t3.model_id",
            " where t.warn_status='1'",
            " <if test='name!=null and name!=\"\"'>",
            " and t.warn_name like concat('%',#{name},'%')",
            " </if>",
            " order by t.warn_time desc",
            " </script>",

    })
    List<WorkPointDropdown> undealDropdown(@Param("name") String name);
}
