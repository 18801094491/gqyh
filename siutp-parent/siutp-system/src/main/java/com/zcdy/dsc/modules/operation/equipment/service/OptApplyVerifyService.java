package com.zcdy.dsc.modules.operation.equipment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.common.system.vo.DictModel;
import com.zcdy.dsc.modules.operation.equipment.entity.OptApplyVerify;
import com.zcdy.dsc.modules.operation.equipment.vo.OptApplyVerifyVo;

import java.util.LinkedList;
import java.util.List;

/**
 * 描述: 申请审核历史
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-05
 * 版本号: V1.0
 */
public interface OptApplyVerifyService extends IService<OptApplyVerify> {
    //审核历史弹窗
    LinkedList<OptApplyVerifyVo> getByApplyId(String id);
    //  查看审核结果
    List<DictModel> queryAuditStatus();
}
