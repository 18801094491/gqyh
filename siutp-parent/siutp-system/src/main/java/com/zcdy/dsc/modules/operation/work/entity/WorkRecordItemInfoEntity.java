package com.zcdy.dsc.modules.operation.work.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 巡检点信息
 * @author songguang.jiao
 * @date 2020/7/20/0020  12:02:08
 */
@Getter
@Setter
public class WorkRecordItemInfoEntity {

    /**
     * 巡检点id
     */
    private String inspectPointId;

    /**
     * 计划id
     */
    private String planId;

    /**
     * 计划模板（派单人录入模板）
     */
    private String tplId;


    /**
     * 工单id
     */
    private String workJobId;
}
