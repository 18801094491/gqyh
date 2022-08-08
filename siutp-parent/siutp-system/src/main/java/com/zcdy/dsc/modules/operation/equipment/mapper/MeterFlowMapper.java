package com.zcdy.dsc.modules.operation.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.datacenter.statistic.vo.MeterFlowMonthPriceVo;
import com.zcdy.dsc.modules.operation.equipment.entity.MeterFlow;
import com.zcdy.dsc.modules.operation.equipment.vo.MeterFlowVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 描述: 水表日累计量信
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-19
 * 版本号: V1.0
 */
public interface MeterFlowMapper extends BaseMapper<MeterFlow> {

    MeterFlow getMeterFlowByDateAndEquipmentId(@Param("equipmentId") String equipmentId, @Param("prevDate") String prevDate);

    void deleteMeterFlowByDateAndEquipmentId(@Param("equipmentId") String equipmentId);

    List<MeterFlow> querynetFlowDay(@Param("daycount") String daycount, @Param("staticsDateEnd") String staticsDateEnd);

    List<MeterFlowVo> queryAllNetFlowDay(@Param("nowMonth") String nowMonth, @Param("lastMonth") String lastMonth, @Param("nowYear") String nowYear);


    /**
     * 查询所有客户水表往月抄表单价
     * 2020-4-26 09:58:00
     *
     * @param page       分页对象
     * @param customerId
     * @return 水表往月抄表单价
     * @author tangchao
     */
    @Select({
            "<script>",
            "select ",
            "sc.customer_sn as customerSn,",
            "sc.customer_name as customerName,",
            "oe.equipment_sn as equipmentSn,",
            "scon.contract_sn as contractSn,",
            "scon.contract_name as contractName,",
            "mfm.water_price waterPrice,",
            "mfm.statics_date staticsDate",
            "from meter_flow_month mfm",
            "left join opt_equipment oe on oe.id = mfm.equipment_id",
            "left join settle_customer sc on mfm.customer_id = sc.id",
            "left join settle_contract scon on mfm.contract_id = scon.id",
            "where 1=1 ",
            "<if test='customerId != null and customerId != \"\"'>",
            "and mfm.customer_id = #{customerId}",
            "</if>",
            "order by scon.create_time , oe.create_time , mfm.statics_date asc",
            "</script>"
    })
    IPage<MeterFlowMonthPriceVo> queryAllWaterMonth(Page<MeterFlowMonthPriceVo> page, @Param("customerId") String customerId);


    /**
     * 查询客户水表历史所有时间抄表水价
     * 2020-4-26 09:58:00
     *
     * @param param 查询参数
     *              customerId 客户id, 允许为空, 为空查询全部客户用水量(总用水量)
     *              startDate 查询开始时间 如果为空 默认30天之内
     *              endDate 查询结束时间 如果为空 默认30天之内
     * @return 水表往月抄表单价
     * @author tangchao
     */
    @Select({
            "<script>",
                "select ",
                "(UNIX_TIMESTAMP(mf.statics_date) * 1000) staticsDate,",
                "sum(mf.net_flow_day) netFlowDay",
                "from ",
                "settle_customer_equipment sce,",
                "meter_flow mf ",
                "where 1=1",
                "and mf.equipment_id = sce.equipment_id",
                "and mf.statics_date &lt; IFNULL(sce.unbind_time,#{now})",
                "and mf.statics_date &gt; sce.bind_time ",
                "<if test='customerId != null and customerId != \"\"'>",
                    "and sce.customer_id =  #{customerId}",
                "</if>",
                "<if test='startDate != null and startDate != \"\" and endDate != null and endDate != \"\"'>",
                    "and mf.statics_date between #{startDate} and #{endDate}",
                "</if>",
                "group by mf.statics_date order by statics_date",
            "</script>"
    })
    List<Map<String, Number>> queryAllDateWaterFlow(Map<String, String> param);

    /**
     * 描述: 通过id集合跟时间求和
     * @author: songguang.jiao
     * 创建时间:  2020年4月28日 下午2:52:43
     * 版本: V1.0
     */
    @Select({
    	" <script>",
    	" SELECT sum(net_flow_day) from meter_flow  where  statics_date=#{localDate} and equipment_id in",
    	" <foreach collection='list' item='item' index='index' open='(' separator=',' close=')'>",
        " #{item}",  
         " </foreach>",
    	" </script>",
    })
	String queryRsLs(@Param("list") List<String> list,@Param("localDate") LocalDate localDate);

	/**
	 * 创建人:Roberto
	 * 创建时间:2020年4月30日 下午4:55:24
	 * 描述:<p>删除指定日期的水表统计数据</p>
	 */
    @Delete({
    	"delete from meter_flow where equipment_id=#{equipmentId} and statics_date=#{yestodyStr}"
    })
	void deleteMeterByDateAndEquipmentId(@Param("equipmentId") String equipmentId, @Param("yestodyStr") String yestodyStr);
}
