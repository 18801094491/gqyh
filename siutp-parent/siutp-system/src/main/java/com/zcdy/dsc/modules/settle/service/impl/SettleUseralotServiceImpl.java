package com.zcdy.dsc.modules.settle.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.system.vo.DictModel;
import com.zcdy.dsc.modules.settle.entity.SettleUseralot;
import com.zcdy.dsc.modules.settle.mapper.SettleUseralotMapper;
import com.zcdy.dsc.modules.settle.service.SettleUseralotService;
import com.zcdy.dsc.modules.settle.vo.SettleUseralotVo;
import com.zcdy.dsc.modules.system.mapper.SysDictMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述: 用户报装信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-11
 * 版本号: V1.0
 */
@Service
public class SettleUseralotServiceImpl extends ServiceImpl<SettleUseralotMapper, SettleUseralot> implements SettleUseralotService {

    @Autowired
    private  SettleUseralotMapper useralotMapper;
    @Autowired
    private SysDictMapper sysDictMapper;

    //查询用户报装信息
    @Override
    public IPage<SettleUseralotVo> getUseralotInfo(Page<SettleUseralotVo> page, String customerName, String startTime, String endTime) {
        return useralotMapper.selectUseralotInfo(page,customerName,startTime,endTime);
    }

    //查看安装状态
    @Override
    public List<DictModel> queryByStatus(String code) {
        return sysDictMapper.queryDictItemsByCode(code);
    }

    //查询导出信息
    @Override
    public List<SettleUseralotVo> getExportXls(String customerName, String startTime, String endTime) {
        return useralotMapper.getExportXls(customerName,startTime,endTime);
    }
}
