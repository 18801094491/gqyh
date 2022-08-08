package com.zcdy.dsc.modules.centre.mapper;

import com.zcdy.dsc.modules.centre.entity.OptObjTree;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 对象与树的绑定关系
 * @Author: 在信汇通
 * @Date:   2021-03-11
 * @Version: V1.0
 */
public interface OptObjTreeMapper extends BaseMapper<OptObjTree> {

    @Insert("insert into opt_obj_tree " +
            "(id, data_id, tree_id, obj_id, centre, create_by, create_time) " +
            "values " +
            "(#{id}, #{dataId}, #{treeId}, " +
            "(select id from sys_object where obj_type = #{objType}), " +
            "#{centre}, #{createBy}, #{createTime})")
    int treeObjBind(OptObjTree optObjTree);

    @Update("update opt_obj_tree " +
            "set del_flag = #{delFlag}, " +
                "update_by = #{updateBy}, " +
                "update_time = #{updateTime} " +
            "where data_id = #{dataId} " +
                "and obj_id = (select id from sys_object where obj_type = #{objType}) " +
                "and centre = #{centre}")
    int deleteTreeObjBind(OptObjTree optObjTree);
}
