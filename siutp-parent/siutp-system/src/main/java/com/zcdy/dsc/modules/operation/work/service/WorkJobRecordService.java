package com.zcdy.dsc.modules.operation.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.work.entity.WorkJobRecord;
import com.zcdy.dsc.modules.operation.work.param.RecordParam;
import com.zcdy.dsc.modules.operation.work.vo.WorkJobDatasourceTree;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 工单录入
 * @author: songgguang.jiao
 * @date:   2020-07-16
 */
public interface WorkJobRecordService extends IService<WorkJobRecord> {

    /**
     * 工单录入
     * @param recordParam 入参
     */
    void saveRecordItems(RecordParam recordParam);

    /**
     * 查询保存的记录信息  根据用户类型跟id查询已经录入的工单数据
     * @param workJobPointId 工单巡检点id
     * @param planId 计划id
     * @param userType 用户类型  分派单人跟巡检人类型
     * @return
     */
    List<WorkJobDatasourceTree> getRecordItems(@Param("workJobId") String workJobPointId, @Param("planId") String planId, @Param("userType") String userType);
}
