package com.zcdy.dsc.modules.operation.equipment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.util.UUIDGenerator;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.entity.EquipmentUpkeep;
import com.zcdy.dsc.modules.operation.equipment.entity.EquipmentUpkeepAttach;
import com.zcdy.dsc.modules.operation.equipment.entity.EquipmentUpkeepUsers;
import com.zcdy.dsc.modules.operation.equipment.mapper.EquipmentUpkeepMapper;
import com.zcdy.dsc.modules.operation.equipment.service.EquipmentUpkeepService;
import com.zcdy.dsc.modules.operation.equipment.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述: 维保记录
 *
 * @author： 智能无人硬核心项目组
 * 创建时间： 2020-02-17
 * 版本号: V1.0
 */
@Service
public class EquipmentUpkeepServiceImpl extends ServiceImpl<EquipmentUpkeepMapper, EquipmentUpkeep>
        implements EquipmentUpkeepService {

    @Autowired
    private EquipmentUpkeepMapper upkeepMapper;

    // 查维保记录列表
    @Override
    public IPage<EquipmentUpkeepVo> getListInfo(Page<EquipmentUpkeepVo> page, String equipmentName,
                                                String upkeepTimeStart, String upkeepTimeEnd, String type) {
        return upkeepMapper.selectListInfo(page, equipmentName, upkeepTimeStart, upkeepTimeEnd, type);
    }

    // 维保记录详情
    @Override
    public EquipmentUpkeepdetVo queryById(String upkeepId) {
        // 分开查，分别查询出 用户 和图片
        EquipmentUpkeepdetVo vo = new EquipmentUpkeepdetVo();
        List<AttachVo> listAtt = upkeepMapper.selectByUpkeepIdAtt(upkeepId);
        List<UpkeepUsersVo> listUser = upkeepMapper.selectByUpkeepIdUser(upkeepId);
        vo.setUpkeepAttachList(listAtt);
        vo.setUpkeepUsersList(listUser);
        return vo;
    }

    // 导出维保记录
    @Override
    public List<EquipmentUpkeepVo> getExportXls() {
        return upkeepMapper.getExportXls();
    }


    // 通过资产id查询-具体的维保信息
    @Override
    public List<EquipmentUpkeepVo> getUpkeepInfo(String equipmentId) {
        return upkeepMapper.selectUpkeepInfo(equipmentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUpkeep(EquipmentUpkeepParamVo paramVo) {
        EquipmentUpkeep upkeep = new EquipmentUpkeep();
        upkeep.setId(paramVo.getId());
        upkeep.setUpkeepCreator(paramVo.getUpkeepCreator());
        upkeep.setEquipmentId(paramVo.getEquipmentId());
        upkeep.setType(paramVo.getType());
        upkeep.setUpkeepContent(paramVo.getUpkeepContent());
        upkeep.setUpkeepReason(paramVo.getUpkeepReason());
        upkeep.setUpkeepResult(paramVo.getUpkeepResult());
        upkeepMapper.updateById(upkeep);
        // 修改图片地址,删除原有的,重新插入
        upkeepMapper.deleteAttach(paramVo.getId());
        upkeepMapper.deleteUsers(paramVo.getId());
        this.insertUsersAndAttach(paramVo, upkeep);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUpkeep(EquipmentUpkeepParamVo paramVo) {
        Date date = new Date();
        EquipmentUpkeep upkeep = new EquipmentUpkeep();
        upkeep.setDelFlag(DelFlagConstant.NORMAL);
        upkeep.setUpkeepCreator(paramVo.getUpkeepCreator());
        upkeep.setEquipmentId(paramVo.getEquipmentId());
        upkeep.setType(paramVo.getType());
        upkeep.setUpkeepContent(paramVo.getUpkeepContent());
        upkeep.setUpkeepReason(paramVo.getUpkeepReason());
        upkeep.setUpkeepResult(paramVo.getUpkeepResult());
        upkeep.setUpkeepTime(date);
        upkeepMapper.insert(upkeep);
        this.insertUsersAndAttach(paramVo, upkeep);
    }

    private void insertUsersAndAttach(EquipmentUpkeepParamVo paramVo, EquipmentUpkeep upkeep) {
        List<EquipmentUpkeepAttach> attachList = paramVo.getList();
        // 插入图片
        if (attachList.size() > 0 && !attachList.isEmpty()) {
            for (EquipmentUpkeepAttach attachVo : attachList) {
                attachVo.setUpkeepId(upkeep.getId());
                attachVo.setId(UUIDGenerator.generate());
            }
            upkeepMapper.insertAttachData(attachList);
        }

        // 插入用户
        if (!StringUtils.isEmpty(paramVo.getUpkeepUsers())) {
            String[] userIds = paramVo.getUpkeepUsers().split(",");
            List<EquipmentUpkeepUsers> list = new ArrayList<EquipmentUpkeepUsers>();
            for (String userId : userIds) {
                EquipmentUpkeepUsers user = new EquipmentUpkeepUsers();
                user.setId(UUIDGenerator.generate());
                user.setUpkeepId(upkeep.getId());
                user.setUpkeepUser(userId);
                list.add(user);
            }
            upkeepMapper.insertUsersData(list);
        }
    }

}
