package com.zcdy.dsc.modules.operation.equipment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentTreeQueryParam;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentTreeVo;

/**
 * @author tangchao
 * @date 2020/5/28
 */
public interface OptEquipmentTreeService {

    /**
     * 获取设备台账树结构
     *
     * @param page
     * @param param 查询参数
     * @return 设备台账书结果
     */
    IPage<OptEquipmentTreeVo> queryEquipmentInfoBylabelId(Page page, OptEquipmentTreeQueryParam param);

}
