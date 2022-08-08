package com.zcdy.dsc.modules.operation.equipment.service;

import com.zcdy.dsc.modules.operation.equipment.entity.OptBasicInfomation;
import com.zcdy.dsc.modules.operation.equipment.param.OptBaseInfoPageParam;
import com.zcdy.dsc.modules.operation.equipment.vo.OptBaseInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 设备台账基本信息
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-05-28
 * @Version: V1.0
 */
public interface OptBasicInfomationService extends IService<OptBasicInfomation> {
    
    /**
     * 修改设备基础数据,并同步到资产信息表
     * @author songguang.jiao
     * @date 2020/05/28 15:16:28
     * @param optBasicInfomation
     */
    void updateEquipInfo(OptBasicInfomation optBasicInfomation);

    /**
     * 删除基础信息
     * @author songguang.jiao
     * @date 2020/05/28 15:38:37
     * @param id
     */
    void removeBaseInfo(String id);

    /**
     * 设备台账基本信息-分页列表查询
     * @author songguang.jiao
     * @date 2020/05/28 15:51:14
     * @param param
     * @return
     */
    IPage<OptBaseInfoVo> queryPageData(Page<OptBaseInfoVo> page,OptBaseInfoPageParam param);
}
