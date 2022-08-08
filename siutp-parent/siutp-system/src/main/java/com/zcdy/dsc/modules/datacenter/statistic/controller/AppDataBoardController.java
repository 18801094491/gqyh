package com.zcdy.dsc.modules.datacenter.statistic.controller;

import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.framework.swagger.ApiVersion;
import com.zcdy.dsc.common.framework.swagger.VersionConstant;
import com.zcdy.dsc.modules.datacenter.statistic.service.DataBoardService;
import com.zcdy.dsc.modules.datacenter.statistic.vo.HydrologysVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: APP数据看板controller
 * @Author: 在信汇通
 * @Date: 2020-12-29 10:54:08
 * @Version: V1.0
 */

@Slf4j
@Api(tags = "APP数据看板")
@RestController
@RequestMapping("/data-board/app")
public class AppDataBoardController {
    @Autowired
    private DataBoardService dataBoardService;

    /**
     * 获取水文实时数据检测
     * @return
     */
    @AutoLog(value = "获取水文实时数据检测")
    @ApiOperation(value = "获取水文实时数据检测", notes = "获取水文实时数据检测")
    @GetMapping(value = "/hydrologys")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<List<HydrologysVo>> getHydrologyList() {
        ResultData<List<HydrologysVo>> resultData = new ResultData<>();
        try {
            List<HydrologysVo> sunriseChartDataList = dataBoardService.getHydrologyAllList();
            resultData.setData(sunriseChartDataList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }
}
