package com.zcdy.dsc.modules.centre.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.centre.entity.CentreTree;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.centre.param.CustQueryParam;
import com.zcdy.dsc.modules.centre.param.EquipmentQueryParam;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.vo.EquipmentQueryVO;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;
import com.zcdy.dsc.modules.settle.vo.CustomerVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Description: 四大中心树形结构
 * @Author: 在信汇通
 * @Date:   2021-02-20
 * @Version: V1.0
 */
public interface CentreTreeMapper extends BaseMapper<CentreTree> {
    @Select({"<script>" +
            "select id, name, parent_id parentId, \n" +
                "((select count(*) from opt_label_tree ol2 where ol2.parent_id = ol1.id and ol2.del_flag = 0) + " +
                    "(select count(*) from opt_attr_tree at where at.tree_id = ol1.id and at.del_flag = 0)) childrenNum \n" +
            "from opt_label_tree ol1 \n" +
            "where del_flag = #{delFlag} \n" +
                "and centre = #{centre} \n" +
                "and obj_id = (select id from sys_object where obj_type = #{objType} and del_flag = #{delFlag}) \n" +
                "<choose>" +
                    "<when test='parentId != null and parentId != \"\"'>" +
                        "and parent_id = #{parentId} " +
                    "</when>" +
                    "<otherwise>" +
                        "and parent_id is null " +
                    "</otherwise>" +
                "</choose>" +
            "order by name" +
            "</script>"})
    List<CentreTree> getTreeList(CentreTree centreTree);

    @Select({"select id, name, parent_id parentId, \n" +
            "(select count(*) from opt_label_tree ol2 where ol2.parent_id = ol1.id) childrenNum \n" +
            "from opt_label_tree ol1 \n" +
            "where del_flag = #{delFlag} \n" +
            "and centre = #{centre} \n" +
            "and obj_id = (select id from sys_object where obj_type = #{objType} and del_flag = #{delFlag}) \n" +
            "order by name"})
    List<CentreTree> getAllTreeList(CentreTree centreTree);

    @Select("<script>" +
            "select t.id, t.name, t.centre, o.id objId, o.name objName " +
            "from opt_label_tree t " +
                "left join sys_object o on o.id = t.obj_id and o.del_flag = #{delFlag} " +
            "where t.del_flag = #{delFlag} and t.parent_id is null " +
                "<if test='name != null and name != \"\"'>" +
                "and t.name like concat('%',#{name},'%') " +
                "</if>" +
                "<if test='centre != null and centre != \"\"'>" +
                "and t.centre = #{centre} " +
                "</if>" +
                "<if test='objId != null and objId != \"\"'>" +
                "and t.obj_id = #{objId} " +
                "</if>" +
            "order by t.create_time desc" +
            "</script>")
    List<CentreTree> getRootTreeList(CentreTree centreTree);

    @Insert({"insert into opt_label_tree " +
            "(id, name, parent_id, " +
            "parent_ids, " +
            "obj_id, " +
            "centre, create_by, create_time) " +
            "values " +
            "(#{id}, #{name}, #{parentId}, " +
            "concat(ifnull((select * from (select parent_ids from opt_label_tree where id = #{parentId}) parentIds), ''), #{parentId}, ','), " +
            "(select * from (select id from sys_object where obj_type = #{objType} and del_flag = 0) objId) , " +
            "#{centre}, #{createBy}, #{createTime})"})
    int add(CentreTree centreTree);

    @Insert({"insert into opt_label_tree " +
            "(id, name, " +
            "obj_id, " +
            "centre, create_by, create_time) " +
            "values " +
            "(#{id}, #{name}, " +
            "#{objId}, " +
            "#{centre}, #{createBy}, #{createTime})"})
    int addRoot(CentreTree centreTree);

    @Select("select count(*) " +
            "from opt_label_tree " +
            "where centre = #{centre} " +
                "and obj_id = #{objId} " +
                "and del_flag = 0 " +
                "and parent_id is null")
    Integer getRootNumByCentreObjId(CentreTree centreTree);

    @Select("select count(*) " +
            "from opt_label_tree " +
            "where obj_id = #{objId} " +
                "and parent_id is null " +
            "and del_flag = " + StatusConstant.WORKING)
    Integer getRootTreeNumByObjId(String objId);

    @Select("select count(*) from \n" +
            "(select id from opt_label_tree where parent_id = #{treeId} and del_flag = " + StatusConstant.WORKING + " " +
            "UNION\n" +
            "select id from opt_obj_tree where tree_id = #{treeId} and del_flag = " + StatusConstant.WORKING + " " +
            "UNION\n" +
            "select id from opt_attr_tree where tree_id = #{treeId} and del_flag = " + StatusConstant.WORKING + ") a")
    Integer getObjNum4UseTreeId(String treeId);

    @Update("update opt_label_tree " +
            "set name = #{name}, " +
                "update_by = #{updateBy}," +
                "update_time = #{updateTime} " +
            "where id = #{id}")
    int updateNameById(CentreTree centreTree);

    @Select("select t.id, t.name, t.centre, o.id objId, o.name objName," +
            "t.parent_id parentId, t.parent_ids parentIds " +
            "from opt_label_tree t " +
            "left join sys_object o on o.id = t.obj_id and o.del_flag = #{delFlag} " +
            "where t.del_flag = #{delFlag} and t.id = #{id}")
    CentreTree getById(CentreTree centreTree);

    IPage<CustomerVo> selectCustPageListByParam(Page<CustomerVo> page, @Param("param") CustQueryParam param);

    IPage<OptEquipmentModel> selectEquPageListByParam(Page<OptEquipmentModel> page, @Param("param") EquipmentQueryParam param);

    List<OptEquipmentModel> getEquListByParentIdNoPage(@Param("param") EquipmentQueryParam param);

    List<CustomerVo> getCustListByParentIdNoPage(@Param("param") CustQueryParam param);

    IPage<OptEquipmentModel> getEquListByAllSearch(Page<OptEquipmentModel> page, @Param("param") EquipmentQueryParam param, @Param("attr") OptEquipmentModel attr);

    IPage<CustomerVo> getCustListByAllSearch(Page<CustomerVo> page, @Param("param") CustQueryParam param, @Param("attr") CustomerVo attr);
}
