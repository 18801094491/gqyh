package com.zcdy.dsc.modules.operation.equipment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.equipment.entity.EquipmentUpkeep;
import com.zcdy.dsc.modules.operation.equipment.vo.EquipmentUpkeepParamVo;
import com.zcdy.dsc.modules.operation.equipment.vo.EquipmentUpkeepVo;
import com.zcdy.dsc.modules.operation.equipment.vo.EquipmentUpkeepdetVo;

import java.util.List;

/**
 * 描述: 维保记录
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-17
 * 版本号: V1.0
 */
public interface EquipmentUpkeepService extends IService<EquipmentUpkeep> {

    //查维保记录列表
    IPage<EquipmentUpkeepVo> getListInfo(Page<EquipmentUpkeepVo> page, String equipmentName, String upkeepTimeStart,
                                         String upkeepTimeEnd, String type);

    //维保记录详情
    EquipmentUpkeepdetVo queryById(String upkeepId);

    //导出 维保记录列表
    List<EquipmentUpkeepVo> getExportXls();

    //通过资产id查询-具体的维保信息
    List<EquipmentUpkeepVo> getUpkeepInfo(String equipmentId);

    /**
     * 描述: 维保信息修改
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月24日 下午3:20:34
     * 版本号: V1.0
     */
    void editUpkeep(EquipmentUpkeepParamVo equipmentUpkeepParamVo);

    /**
     * 描述: 维保信息新增
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月24日 下午3:20:34
     * 版本号: V1.0
     */
    void saveUpkeep(EquipmentUpkeepParamVo equipmentUpkeepParamVo);

}
