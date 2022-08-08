package com.zcdy.dsc.modules.settle.mapper;

import com.zcdy.dsc.modules.settle.vo.AnnualStatisticalQueryParmVo;
import com.zcdy.dsc.modules.settle.vo.AnnualStatisticalRowVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 年度统计
 *
 * @author tangchao
 * @date 2020/5/25
 */
public interface AnnualStatisticalMapper {

    /**
     * 年度统计表
     *
     * @param param 查询参数
     * @return 年度
     */
    @Select({
            "<script>",
            "select",
            "sc.id as  customerId,",
            "sc.customer_name as customerName,",
            "year(scheck.settle_date) as statistic_year,",
            "month(scheck.settle_date) as statistic_month,",
            "sum(sb.used_flow) as monthTotalFlow,",
            "sum(sb.total_cost) as monthTotalCost",
            "from settle_batch sb",
            "left join settle_customer sc on sc.id = sb.customer_id",
            "left join settle_checklist scheck on scheck.id = sb.checklist_id",
            "where 1=1",
            "and sb.`status` in ('01', '02')",
            "<if test='beginYear != null and beginYear != \"\"'>",
            "and year(sb.current_flow_date) &gt;= #{beginYear}",
            "</if>",
            "<if test='endYear != null and endYear != \"\"'>",
            "and year(sb.current_flow_date) &lt;= #{endYear}",
            "</if>",
            "<if test='customerType != null and customerType != \"\"'>",
            "and sc.customer_type = #{customerType}",
            "</if>",
            "<if test='likeCustomerName != null and likeCustomerName != \"\"'>",
            "and sc.customer_name like concat('%',#{likeCustomerName},'%')",
            "</if>",
            "<if test='customerIds != null and customerIds.length != 0'>",
            "and sc.id in ",
            "<foreach item='item' collection='customerIds' separator=',' open='(' close=')' index=''>",
            "#{item}",
            "</foreach>",
            "</if>",
            "group by concat(customer_id,DATE_FORMAT(scheck.settle_date,'%Y%m'))",
            "order by customer_id asc, year(scheck.settle_date) asc,month(scheck.settle_date) asc",
            "</script>",
    })
    List<AnnualStatisticalRowVo> queryAnnualStatistical(AnnualStatisticalQueryParmVo param);
}
