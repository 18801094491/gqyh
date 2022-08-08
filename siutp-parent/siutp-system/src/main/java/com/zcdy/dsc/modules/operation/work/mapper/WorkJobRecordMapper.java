package com.zcdy.dsc.modules.operation.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.operation.work.entity.WorkJobRecord;
import com.zcdy.dsc.modules.operation.work.vo.WorkJobDatasourceTree;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 工单录入
 * @author: songguang.jiao
 * @date:   2020-07-16
 * @version: V1.0
 */
public interface WorkJobRecordMapper extends BaseMapper<WorkJobRecord> {


    /**
     * 通过模板id 查询数据并封装,从数据项表查询
     * @param tplId
     * @return
     */
    List<WorkJobRecord> getItemsFromDataSouece(String tplId);

    /**
     * 查询保存的记录信息
     * @param workJobId 工单id
     * @param planId 计划id
     * @param userType 用户类型
     * @return
     */
    List<WorkJobDatasourceTree> getRecordItems(@Param("workJobId") String workJobId, @Param("planId") String planId, @Param("userType") String userType);

}
