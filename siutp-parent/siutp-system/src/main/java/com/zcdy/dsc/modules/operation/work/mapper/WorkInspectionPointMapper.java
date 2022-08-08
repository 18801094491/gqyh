package com.zcdy.dsc.modules.operation.work.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.work.entity.WorkInspectionPoint;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.operation.work.param.WorkInspectionPointPageParam;
import com.zcdy.dsc.modules.operation.work.vo.WorkInspectionPointVo;
import com.zcdy.dsc.modules.operation.work.vo.WorkPointDropdown;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 巡检点
 * @author: songguang.jiao
 * @date:   2020-07-01
 * @version: V1.0
 */
public interface WorkInspectionPointMapper extends BaseMapper<WorkInspectionPoint> {

    /**
     * 分页查询
     * @param page
     * @param param
     * @return
     */
    IPage<WorkInspectionPointVo> selectPageData(Page<WorkInspectionPointVo> page,@Param("param") WorkInspectionPointPageParam param);

    /**
     * 巡检点下拉选
     * @param name
     * @return
     */
    List<WorkPointDropdown> pointDropdown(@Param("name") String name);
}
