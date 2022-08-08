package com.zcdy.dsc.modules.operation.work.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 工单录入情况组合
 * @author songguang.jiao
 * @date 2020/7/20/0020  11:18:49
 */
@Getter
@Setter
@ApiModel("WorkJobRecordInfosVo")
public class WorkJobRecordInfosVo {

    /**
     * 派单人录入信息
     */
    List<WorkJobDatasourceTree> dispatchData;
    /**
     * 巡检人录入信息
     */
    List<WorkJobDatasourceTree> inspectorData;
}
