package com.zcdy.dsc.modules.operation.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.work.entity.WorkJobDatasource;
import com.zcdy.dsc.modules.operation.work.param.WorkJobDatasourceParam;
import com.zcdy.dsc.modules.operation.work.vo.WorkJobDatasourceTree;

import java.util.List;

/**
 * @description: 工单数据项
 * @author: 智能无人硬核心项目组
 * @date:   2020-06-24
 * @version: V1.0
 */
public interface WorkJobDatasourceService extends IService<WorkJobDatasource> {

    /**
     * 保存数据项
     * @param datasourceParam
     */
    void saveData(WorkJobDatasourceParam datasourceParam);

    /**
     * 删除数据项
     * @param id
     */
    void delete(String id);


    /**
     * 通过模板id查询数据树结构展示
     * @param tplId 模板id
     * @return
     */
    List<WorkJobDatasourceTree> tplDataTree(String tplId);

}
