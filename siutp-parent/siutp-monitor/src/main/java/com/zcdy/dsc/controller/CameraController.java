package com.zcdy.dsc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.artemis.HKArtemisHttpUtil;
import com.zcdy.dsc.common.util.StringUtil;
import com.zcdy.dsc.entity.HKProperties;
import com.zcdy.dsc.param.ArtemisMonitorPageParam;
import com.zcdy.dsc.vo.ArtemisConfigVo;
import com.zcdy.dsc.vo.ArtemisMonitorPageVo;
import com.zcdy.dsc.vo.ArtemisMonitorPreviewVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 海康综合安防视频监控
 * @Author: 在信汇通
 * @Date:   2020-12-07 18:19:14
 * @Version: V1.0
 */
@Slf4j
@Api(tags="海康综合安防视频监控")
@RestController
@RequestMapping("camera")
public class CameraController {
    @Autowired
    private HKArtemisHttpUtil hkArtemisHttpUtil;

    @Autowired
    private HKProperties hkProperties;

    @GetMapping("getAllConfigs")
    @ApiOperation(value = "获取每个综合安防平台的配置集合", notes = "获取每个综合安防平台的配置集合")
    public ResultData<ArtemisConfigVo> getAllConfigs() {
        ResultData<ArtemisConfigVo> resultData = new ResultData<>();
        try {
            List<HKProperties.HKConfig> artemis = hkProperties.getArtemis();
            List<ArtemisConfigVo> volist = new ArrayList<>();
            for(HKProperties.HKConfig config : artemis)
            {
                ArtemisConfigVo vo = new ArtemisConfigVo(config);
                vo.setVersionUrlMap(hkProperties.getVerUrlMap(config.getKey()));
                volist.add(vo);
            }
            return resultData.success(volist);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return resultData.error500("查询失败");
        }
    }

    /**
     * 根据configKey获取综合安防平台配置
     * @return
     */
    @GetMapping("configs")
    @ApiOperation(value = "根据configKey获取综合安防平台配置", notes = "根据configKey获取综合安防平台配置")
    public ResultData<ArtemisConfigVo> configs(@ApiParam(value = "监控平台配置唯一编码", required = true) String configKey) {
        ResultData<ArtemisConfigVo> resultData = new ResultData<>();
        try {
            if(configKey == null)
            {
                resultData.error500("监控平台配置为空");
                return resultData;
            }
            HKProperties.HKConfig config = hkProperties.getConfigByKey(configKey);
            if(config == null)
            {
                resultData.error500("configKey错误，未获取到监控平台配置信息");
                return resultData;
            }
            ArtemisConfigVo artemisConfigVo = new ArtemisConfigVo(config);
            artemisConfigVo.setVersionUrlMap(hkProperties.getVerUrlMap(configKey));
            resultData.setData(artemisConfigVo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     * 获取监控点列表
     * 由于监控点列表来自多个平台api，而且查询结果采用分页，所以查询时必须单个平台进行查询，即pageParam.configKey不能为空
     * @param pageParam 监控点查询分页列表参数
     * @return
     */
    @GetMapping("monitors")
    @ApiOperation(value = "获取监控点列表", notes = "获取监控点列表")
    public ResultData<ArtemisMonitorPageVo> monitors(ArtemisMonitorPageParam pageParam) {
        ResultData<ArtemisMonitorPageVo> resultData = new ResultData<>();
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("pageNo", pageParam.getPageNo());
            jsonBody.put("pageSize", pageParam.getPageSize());
            jsonBody.put("name", pageParam.getName());
            String configKey = pageParam.getConfigKey();

            if(configKey == null)
            {
                return resultData.error500("监控平台配置为空");
            }
            ArtemisMonitorPageVo monitorPageVo = hkArtemisHttpUtil.getCameraSearchV2(jsonBody, configKey);

            if (monitorPageVo == null || monitorPageVo.getList() == null || monitorPageVo.getList().size() == 0) {
                return resultData.error500("暂无已授权的监控点");
            }
            resultData.setData(monitorPageVo);
            return resultData.success();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return resultData.error500("查询失败");
        }
    }

    /**
     * 获取监控点预览取流
     * @param indexCode 监控点唯一标识
     * @return
     */
    @GetMapping("preview-url")
    @ApiOperation(value = "获取监控点预览取流", notes = "获取监控点预览取流")
    public ResultData<ArtemisMonitorPreviewVo> previewUrl(@ApiParam(value = "设备唯一编码", required = true) String indexCode,
                                                          @ApiParam(value = "配置唯一编码", required = true) String configKey){
        ResultData<ArtemisMonitorPreviewVo> resultData = new ResultData<>();
        try {
            ArtemisMonitorPreviewVo monitorPreviewVo = hkArtemisHttpUtil.getCamerasPreviewURLs(indexCode, "hls", configKey);
            if (StringUtil.isNull(monitorPreviewVo.getUrl())) {
                return resultData.error500("查询失败");
            }
            resultData.setData(monitorPreviewVo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }


}
