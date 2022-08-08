package com.zcdy.dsc.modules.settle.service;

import com.zcdy.dsc.modules.settle.vo.ManualContractInfoVo;
import com.zcdy.dsc.modules.settle.vo.ManualFlowFormVo;

import java.util.List;

/**
 * @author tangchao
 * @date 2020/5/12
 */
public interface ManualFlowService {
    /**
     * 非周期水表手工录入 新增
     * @param manualFlowFormVo 表单
     */
    void add(ManualFlowFormVo manualFlowFormVo);
    /**
     * 非周期水表手工录入 修改
     * @param manualFlowFormVo 表单
     */
    void edit(ManualFlowFormVo manualFlowFormVo);

    /**
     * 根据设备id, 和日期 获取合同
     * @return 合同信息
     */
    public List<ManualContractInfoVo> listContractByOptDate(String equipmentId, String date);

}
