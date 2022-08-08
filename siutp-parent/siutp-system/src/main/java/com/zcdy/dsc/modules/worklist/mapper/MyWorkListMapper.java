package com.zcdy.dsc.modules.worklist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.worklist.entity.WorkList;
import com.zcdy.dsc.modules.worklist.entity.WorkListMatter;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Description: 我的工单
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public interface MyWorkListMapper extends BaseMapper<WorkList> {

    IPage<WorkList> selectPageDate(IPage<WorkList> page, WorkList query);

    @Update("<script>" +
            "update work_list_matter\n" +
            "set status = #{status}, update_by = #{updateBy},\n" +
            "update_time = #{updateTime}, solve_id = #{solveId}, \n" +
            "solve_time = #{solveTime}, solve_longitude = #{solveLongitude}, \n" +
            "solve_latitude = #{solveLatitude}, solve_desc = #{solveDesc} \n" +
            "where id in\n" +
            "<foreach collection='idList' item='item' open='(' close=')' separator=','>\n" +
            "#{item}\n" +
            "</foreach>" +
            "</script>")
    int updateMatterComplete(WorkListMatter matter);

    @Update("update work_list_matter\n" +
            "set status = #{status}, update_by = #{updateBy},\n" +
            "update_time = #{updateTime}, solve_id = #{solveId}, \n" +
            "solve_time = #{solveTime}, solve_longitude = #{solveLongitude}, \n" +
            "solve_latitude = #{solveLatitude}, solve_desc = #{solveDesc}, \n" +
            "has_matter = #{hasMatter} \n" +
            "where id = #{id}")
    int updateMatterCompleteById(WorkListMatter matter);

    @Update({"update work_list " +
            "set status = #{status}, " +
            "update_by = #{updateBy}, " +
            "update_time = #{updateTime} " +
            "where id = (select list_id " +
                        "from work_list_matter " +
                        "where id = #{id})"})
    int updateWorkListCompleteByMatterId(WorkListMatter matter);

    @Select({"select id, status \n" +
            "from work_list_matter\n" +
            "where del_flag = #{delFlag} \n" +
                "and list_id = (select list_id " +
                                "from work_list_matter " +
                                "where id = #{id})"})
    List<WorkListMatter> getMatterListByMatterId(WorkListMatter matter);

    @Select({"select id, status " +
            "from work_list " +
            "where id = (select list_id from work_list_matter where id = #{id}) " +
                " and del_flag = #{delFlag}"})
    WorkList getWorkListByMatterId(WorkListMatter matter);

    @Insert({"insert into work_list_matter " +
            "(id, sub_id, sub_time, create_time, create_by, type, status, matter_id, description, matter_type, matter_level, matter_longitude, matter_latitude, title, equipment_id) " +
            "values" +
            "(#{id}, #{subId}, #{subTime}, #{createTime}, #{createBy}, #{type}, #{status}, #{matterId}, #{description}, #{matterType}, #{matterLevel}, #{matterLongitude}, #{matterLatitude}, #{title}, #{equipmentId})"})
    int insertNewMatter(WorkListMatter matter);

    @Update({"update work_list_matter " +
            "set del_flag = #{delFlag}, " +
                "update_by = #{updateBy}, " +
                "update_time = #{updateTime} " +
            "where matter_id = #{matterId}"})
    int deleteMatterByMatterId(WorkListMatter matter);
}
