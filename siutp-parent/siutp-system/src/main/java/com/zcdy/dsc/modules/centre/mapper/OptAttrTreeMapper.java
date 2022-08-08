package com.zcdy.dsc.modules.centre.mapper;

import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.centre.entity.OptAttrTree;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 属性与树形结构关系
 * @Author: 在信汇通
 * @Date:   2021-03-10
 * @Version: V1.0
 */
public interface OptAttrTreeMapper extends BaseMapper<OptAttrTree> {

    @Select("select t.id, t.tree_id, t.obj_id, t.attrs, t.attrs_name, " +
            "o.name objName " +
            "from opt_attr_tree t " +
                "left join sys_object o on o.id = t.obj_id and o.del_flag = " + StatusConstant.WORKING + " " +
            "where t.tree_id = #{treeId} and t.del_flag = " + StatusConstant.WORKING)
    OptAttrTree getAttrByTreeId(String treeId);
}
