package com.zcdy.dsc.modules.settle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.settle.entity.SettleCustomer;
import com.zcdy.dsc.modules.settle.vo.TreeVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 描述: 客户信息管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-02
 * 版本号: V1.0
 */
public interface CustomerMapper extends BaseMapper<SettleCustomer> {

    /**
     * 查询所有客户树形 禅道#292
     * 2020-4-26 09:58:00
     *
     * @return 所有客户
     * @author tangchao
     */
    @Select({
            "<script>",
            "select ",
            "id as id,",
            "'0' as pid,",
            "customer_name as title",
            "from settle_customer",
            "where 1=1 ",
            "<if test='keyword != null and keyword != \"\"'>",
                "and customer_name like concat('%', #{keyword}, '%') ",
            "</if>",
            "</script>",
    })
    List<TreeVo> queryCustomerTreeList(@Param(value = "keyword") String keyword);
}
