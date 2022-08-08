package com.zcdy.dsc.modules.operation.equipment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.equipment.entity.Knowlege;
import com.zcdy.dsc.modules.operation.equipment.param.KnowlegePageParam;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeDataVo;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeVo;

import java.util.List;

/**
 * 描述: 知识库管理
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
public interface IKnowlegeService extends IService<Knowlege> {
    /**
     * 知识库管理-添加
     *
     * @param knowlegeVo
     */
    void saveInfo(KnowlegeVo knowlegeVo);

    /**
     * 通过id查询知识库信息
     *
     * @param id
     * @return
     */
    KnowlegeVo selectInfo(String id);

    /*分页条件查询*/
    IPage<KnowlegeDataVo> getListInfo(Page<KnowlegeDataVo> page, KnowlegePageParam param);

    /**
     * 查询设备详情
     *
     * @param param
     * @return
     */
    List<KnowlegeDataVo> getData(KnowlegePageParam param);
}
