package com.zcdy.dsc.modules.worklist.mapper;

import com.zcdy.dsc.modules.worklist.entity.WorkListMatter;
import com.zcdy.dsc.modules.worklist.entity.WorkListMatterFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Description: 工单任务附件
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public interface WorkListMatterFileMapper extends BaseMapper<WorkListMatterFile> {
    @Insert({"<script>" +
                    "insert into work_list_matter_file\n" +
                    "(id, matter_id, file_type, url,\n" +
                    "create_by, create_time)\n" +
                    "values\n" +
                    "<foreach item='item' index='index' collection='list' separator=','>\n" +
                    "(#{item.id}, #{item.matterId}, #{item.fileType}, #{item.url},\n" +
                    "#{item.createBy}, #{item.createTime})\n" +
                    "</foreach>" +
            "</script>"})
    int insertBatch(List<WorkListMatterFile> list);

    @Update({"update work_list_matter_file " +
            "set del_flag = #{delFlag}, update_by = #{updateBy}, update_time = #{updateTime} " +
            "where matter_id = #{matterId}"})
    int delByMatterId(WorkListMatterFile file);

    @Update("<script>" +
            "update work_list_matter_file\n" +
            "set del_flag = #{delFlag}, update_by = #{updateBy},\n" +
            "update_time = #{updateTime}\n" +
            "where matter_id in\n" +
            "<foreach collection='idList' item='item' open='(' close=')' separator=','>\n" +
            "#{item}\n" +
            "</foreach>" +
            "</script>")
    int delBatchByMatterId(WorkListMatter matter);
}
