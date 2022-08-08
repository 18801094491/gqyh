package com.zcdy.dsc.modules.worklist.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.worklist.entity.WorkListMatter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Description: 工单任务
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public interface WorkListMatterMapper extends BaseMapper<WorkListMatter> {
    @Select({"<script>" +
            "select m.id, m.title, m.type, sub.realname subName, " +
                "m.sub_time, sol.realname solveName, m.solve_time, m.status, " +
                "m.matter_type, m.matter_level, \n" +
                "(select di.item_text \n" +
                "from sys_dict d \n" +
                    "left join sys_dict_item di on d.id = di.dict_id\n" +
                "where d.dict_code = #{query.queryTypeCode} and m.type = di.item_value) typeDes,\n" +
                "(select di.item_text \n" +
                "from sys_dict d \n" +
                    "left join sys_dict_item di on d.id = di.dict_id\n" +
                "where d.dict_code = #{query.queryStatusCode} and m.status = di.item_value) statusDes,\n" +
                "(select di.item_text \n" +
                "from sys_dict d \n" +
                    "left join sys_dict_item di on d.id = di.dict_id\n" +
                "where d.dict_code = #{query.queryMatterTypeCode} and m.matter_type = di.item_value) matterTypeDes,\n" +
                "(select di.item_text \n" +
                "from sys_dict d \n" +
                    "left join sys_dict_item di on d.id = di.dict_id\n" +
                "where d.dict_code = #{query.queryMatterLevelCode} and m.matter_level = di.item_value) matterLevelDes\n" +
            "from work_list_matter m\n" +
            "left join sys_user sub on m.sub_id = sub.username\n" +
            "left join sys_user sol on m.solve_id = sol.username\n" +
            "where m.del_flag = #{query.delFlag} and m.type = #{query.type} " +
            "<if test='query.subTimeStart != null'>",
            "and m.sub_time &gt;= #{query.subTimeStart} ",
            "</if>" +
            "<if test='query.subTimeEnd != null'>",
            "and m.sub_time &lt;= date_add(#{query.subTimeEnd}, interval 1 day) ",
            "</if>" +
            "<if test='query.status != null and query.status != \"\"'>",
            "and m.status = #{query.status} ",
            "</if>" +
            "<if test='query.subId != null and query.subId != \"\"'>",
            "and m.sub_id = #{query.subId} ",
            "</if>" +
            "order by m.create_time desc" +
            "</script>"})
    IPage<WorkListMatter> selectPageDate(IPage<WorkListMatter> page, WorkListMatter query);

    @Update("<script>" +
            "update work_list_matter\n" +
            "set del_flag = #{delFlag}, update_by = #{updateBy},\n" +
            "update_time = #{updateTime}\n" +
            "where id in\n" +
            "<foreach collection='idList' item='item' open='(' close=')' separator=','>\n" +
            "#{item}\n" +
            "</foreach>" +
            "</script>")
    int delBatch(WorkListMatter matter);

    WorkListMatter getOneById(WorkListMatter matter);

    @Update({"<script>" +
            "update work_list_matter\n" +
            "set list_id = #{listId}, update_by = #{updateBy},\n" +
            "update_time = #{updateTime}, status = #{status} \n" +
            "where type = #{typeCodeMatter} \n" +
            "and id in\n" +
            "<foreach collection='idList' item='item' open='(' close=')' separator=','>\n" +
            "#{item}\n" +
            "</foreach>" +
            "</script>"})
    int updateMatterListId(WorkListMatter matter);

    @Update({"<script>" +
            "<foreach item='item' index='index' collection='list' separator=';'>\n" +
            "update work_list_matter \n" +
            "set list_id = #{item.listId}, update_by = #{item.updateBy},\n" +
            "update_time = #{item.updateTime}, status = #{item.status}, \n" +
            "seq = #{item.seq} \n" +
            "where id = #{item.id} and type = #{item.typeCodeMatter}\n" +
            "</foreach>" +
            "</script>"})
    int updateMatterListWhenAddWorkList(List<WorkListMatter> matterList);

    @Update({"<script>" +
            "<foreach item='item' index='index' collection='list' separator=';'>\n" +
            "update work_list_matter\n" +
            "set list_id = #{item.listId}, update_by = #{item.updateBy},\n" +
            "update_time = #{item.updateTime}, status = #{item.status}, " +
            "seq = #{item.seq} \n" +
            "where id = #{item.id} and status = #{item.statusCodeMatter}\n" +
            "</foreach>" +
            "</script>"})
    int updateBatchByForeachScript(List<WorkListMatter> list);

    @Update({"update work_list_matter\n" +
            "set list_id = null, update_by = #{updateBy},\n" +
            "update_time = #{updateTime}, status = #{status} \n " +
            "where list_id = #{listId} " +
            "and type = #{typeCodeMatter} " +
            "and status = #{statusCodeMatter}"})
    int updateListId2NullByListId(WorkListMatter matter);

    @Update({"update work_list_matter\n" +
            "set del_flag = #{delFlag}, update_by = #{updateBy},\n" +
            "update_time = #{updateTime}\n " +
            "where list_id = #{listId} and type = #{typeCodeMatter} " +
                "and status = #{statusCodeMatter}"})
    int delByListIdAndType(WorkListMatter matter);

    @Update({"<script>" +
            "update work_list_matter\n" +
            "set del_flag = #{delFlag}, update_by = #{updateBy},\n" +
            "update_time = #{updateTime}\n " +
            "where type = #{typeCodeMatter} and status = #{statusCodeMatter} " +
                "and list_id in " +
            "<foreach collection='idList' item='item' open='(' close=')' separator=','>\n" +
            "#{item}\n" +
            "</foreach>" +
            "</script>"})
    int delBatchByListIdAndType(WorkListMatter matter);

    @Update({"<script>" +
            "update work_list_matter\n" +
            "set list_id = null, update_by = #{updateBy},\n" +
            "update_time = #{updateTime}, status = #{status} \n " +
            "where type = #{typeCodeMatter} and status = #{statusCodeMatter} " +
                "and list_id in " +
                "<foreach collection='idList' item='item' open='(' close=')' separator=','>\n" +
                "#{item}" +
                "</foreach>" +
            "</script>"})
    int updateBatchListId2NullByListId(WorkListMatter matter);

    @Select({"<script>" +
            "select m.id, m.title, u.realname subName, m.sub_time, m.type, m.status,\n" +
                "m.description, m.matter_type, m.matter_level, " +
                "(select di.item_text \n" +
                "from sys_dict d \n" +
                    "left join sys_dict_item di on d.id = di.dict_id \n" +
                "where d.dict_code = #{queryTypeCode} and m.type = di.item_value) typeDes,\n" +
                "(select di.item_text \n" +
                "from sys_dict d \n" +
                    "left join sys_dict_item di on d.id = di.dict_id \n" +
                "where d.dict_code = #{queryStatusCode} and m.status = di.item_value) statusDes,\n" +
                "(select di.item_text \n" +
                "from sys_dict d \n" +
                    "left join sys_dict_item di on d.id = di.dict_id\n" +
                "where d.dict_code = #{queryMatterTypeCode} and m.matter_type = di.item_value) matterTypeDes,\n" +
                "(select di.item_text \n" +
                "from sys_dict d \n" +
                    "left join sys_dict_item di on d.id = di.dict_id\n" +
                "where d.dict_code = #{queryMatterLevelCode} and m.matter_level = di.item_value) matterLevelDes\n" +
            "from work_list_matter m\n" +
                "left join sys_user u on u.username = m.sub_id\n" +
            "where m.list_id is null " +
                "and m.status = #{status} " +
                "and m.del_flag = #{delFlag} " +
                "and m.type = #{type} " +
                "<if test='subTimeStart != null'>",
                "and m.sub_time &gt;= #{subTimeStart} ",
                "</if>" +
                "<if test='subTimeEnd != null'>",
                "and m.sub_time &lt;= date_add(#{subTimeEnd}, interval 1 day) ",
                "</if>" +
            "order by m.create_time desc" +
            "</script>"})
    List<WorkListMatter> getAddList(WorkListMatter matter);

    /**
     * 将某个工单所属下未完成的巡检或维养任务状态设置为未完成
     * @param matter
     * @return
     */
    @Update({"update work_list_matter " +
            "set status = #{status}, " +
                "update_by = #{updateBy}, " +
                "update_time = #{updateTime} " +
            "where list_id = #{listId} " +
                "and (type = #{type} or type = #{queryTypeCode}) " +
                "and (status = #{queryStatusCode} or status = #{statusCodeMatter}) " +
                "and del_flag = #{delFlag}"})
    int updateMatterIncompleteByListidAndType(WorkListMatter matter);

    /**
     * 将某个工单下未执行的问题任务解绑，并将状态设为已创建
     * @param matter
     * @return
     */
    @Update({"update work_list_matter " +
            "set status = #{status}, " +
                "list_id = null, " +
                "update_by = #{updateBy}, " +
                "update_time = #{updateTime} " +
            "where list_id = #{listId} " +
                "and type = #{type} " +
                "and del_flag = #{delFlag} " +
            "and (status = #{queryStatusCode} or status = #{statusCodeMatter}) "})
    int updateMatterListid2NullByListidAndType(WorkListMatter matter);
}
