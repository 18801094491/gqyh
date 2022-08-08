package com.zcdy.dsc.modules.operation.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.operation.work.entity.WorkJobDatasource;
import com.zcdy.dsc.modules.operation.work.vo.WorkJobDatasourceTree;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @description: 工单数据项
 * @author: 智能无人硬核心项目组
 * @date: 2020-06-24
 * @version: V1.0
 */
public interface WorkJobDatasourceMapper extends BaseMapper<WorkJobDatasource> {

    /**
     * 更改数据是否为叶子节点
     *
     * @param parentId
     * @param tplId
     * @param isLeaf
     */
    @Update(" update work_job_datasource set is_leaf=#{isLeaf} where id=#{parentId} and tpl_id=#{tplId} and del_flag='0'")
    void updateLeaf(@Param("parentId") String parentId, @Param("tplId") String tplId, @Param("isLeaf") Short isLeaf);


    /**
     * 通过模板id查询数据列表
     * @param tplId 模板id
     * @return
     */
    List<WorkJobDatasourceTree> tlpDataTree(String tplId);

}
