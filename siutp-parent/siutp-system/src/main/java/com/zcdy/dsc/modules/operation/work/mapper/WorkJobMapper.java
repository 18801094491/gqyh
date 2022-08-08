package com.zcdy.dsc.modules.operation.work.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.work.entity.WorkJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.operation.work.param.WokJobPageParam;
import com.zcdy.dsc.modules.operation.work.vo.WorkJobVo;
import org.apache.ibatis.annotations.Param;

/**
 * @description: 工单
 * @author: songguang.jiao
 * @date:   2020-07-08
 * @version: V1.0
 */
public interface WorkJobMapper extends BaseMapper<WorkJob> {


    /**
     * 分页查询工单列表
     * @param page 分页参数
     * @param param 查询参数
     * @return
     */
     IPage<WorkJobVo> selectPageData(Page<WorkJobVo> page, @Param("param") WokJobPageParam param);
}
