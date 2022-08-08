package com.zcdy.dsc.modules.operation.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.work.entity.WorkProblemReport;
import com.zcdy.dsc.modules.operation.work.param.ProblemReportPageParam;
import com.zcdy.dsc.modules.operation.work.vo.ProblemReportVo;
import com.zcdy.dsc.modules.operation.work.vo.WorkPointDropdown;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 问题上报管理
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-06-04
 * @Version: V1.0
 */
public interface WorkProblemReportMapper extends BaseMapper<WorkProblemReport> {

    /**
     * 查询最大编号
     * @return
     */
   String  selectMaxSn();

    /**
     * 分页查询
     * @param page
     * @param param
     * @return
     */
    IPage<ProblemReportVo> selectPageData(Page<ProblemReportVo> page,@Param("param") ProblemReportPageParam param);

    /**
     * 问题上报下拉选
     * @param name
     * @return
     */
    List<WorkPointDropdown> problemDropdown(@Param("name") String name);
}
