package com.zcdy.dsc.modules.settle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.system.vo.DictModel;
import com.zcdy.dsc.modules.settle.entity.SettleUseralot;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.settle.vo.SettleUseralotVo;

import java.util.List;

/**
 * 描述: 用户报装信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-11
 * 版本号: V1.0
 */
public interface SettleUseralotService extends IService<SettleUseralot> {

    //获取用户报装信息
    IPage<SettleUseralotVo> getUseralotInfo(Page<SettleUseralotVo> page, String customerName, String startTime, String endTime);

    //查看安装状态
    List<DictModel> queryByStatus(String code);
    //导出
    List<SettleUseralotVo> getExportXls(String customerName, String startTime, String endTime);
}
