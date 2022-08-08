package com.zcdy.dsc.modules.settle.service;

import com.zcdy.dsc.modules.settle.vo.AnnualStatisticalQueryParmVo;

import java.util.List;
import java.util.Map;

/**
 * 年度统计
 * @author tangchao
 * @date 2020/5/25
 */
public interface AnnualStatisticalService {

    /**
     * 年度统计首页
     * @param param 查询参数
     * @return 首页年度列表
     */
    List<Map<String, String>> index(AnnualStatisticalQueryParmVo param);

}
