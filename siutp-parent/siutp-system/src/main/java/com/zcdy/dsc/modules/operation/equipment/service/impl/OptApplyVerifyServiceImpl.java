package com.zcdy.dsc.modules.operation.equipment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.system.vo.DictModel;
import com.zcdy.dsc.modules.operation.equipment.entity.OptApplyVerify;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptApplyVerifyMapper;
import com.zcdy.dsc.modules.operation.equipment.service.OptApplyVerifyService;
import com.zcdy.dsc.modules.operation.equipment.vo.OptApplyVerifyVo;
import com.zcdy.dsc.modules.system.mapper.SysDictMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * 描述: 申请审核历史
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-05
 * 版本号: V1.0
 */
@Service
public class OptApplyVerifyServiceImpl extends ServiceImpl<OptApplyVerifyMapper, OptApplyVerify> implements OptApplyVerifyService {
    @Autowired
    private  OptApplyVerifyMapper verifyMapper;
    @Autowired
    private SysDictMapper sysDictMapper;

    //审核历史弹窗
    @Override
    public LinkedList<OptApplyVerifyVo> getByApplyId(String id) {
        return verifyMapper.selectByApplyId(id);
    }
    //  查看审核结果
    @Override
    public List<DictModel> queryAuditStatus() {
        return sysDictMapper.queryDictItemsByCode("audit_status");
    }
}
