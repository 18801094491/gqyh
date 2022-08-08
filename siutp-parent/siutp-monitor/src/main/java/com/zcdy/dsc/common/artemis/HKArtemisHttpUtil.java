package com.zcdy.dsc.common.artemis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.zcdy.dsc.common.util.FastJsonUtil;
import com.zcdy.dsc.constants.MonitorConstant;
import com.zcdy.dsc.entity.HKProperties;
import com.zcdy.dsc.vo.ArtemisConfigVo;
import com.zcdy.dsc.vo.ArtemisMonitorPageVo;
import com.zcdy.dsc.vo.ArtemisMonitorPreviewVo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 海康综合安防工具类
 * @Author: 在信汇通
 * @Date:   2020-12-07 18:19:14
 * @Version: V1.0
 */
@Slf4j
@Configuration
public class HKArtemisHttpUtil {
    @Value("${com.zcdy.dsc.hk.debug}")
    private String isDebug;

    @Autowired
    private HKProperties hkProperties;

    //海康能力开放平台的网站路径，固定
    private static final String ARTEMIS_PATH = "/artemis";

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 通用海康接口
     * 调用POST请求类型(application/json)接口
     * @param url 请求url地址
     * @param jsonBody 请求json数据
     * @param configKey 海康配置唯一标识
     * @return
     */
    public String postStringApi(String url, JSONObject jsonBody, String configKey) {
        HKProperties.HKConfig config = hkProperties.getConfigByKey(configKey);
        //artemis网关服务器ip端口
        ArtemisConfig.host = config.getHost();
        //秘钥appkey
        ArtemisConfig.appKey = config.getAppKey();
        //秘钥appSecret
        ArtemisConfig.appSecret = config.getAppSecret();
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", ARTEMIS_PATH + url);//根据现场环境部署确认是http还是https
            }
        };
        String contentType = "application/json";
        String requestBody = FastJsonUtil.toJSONString(jsonBody);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, requestBody, null, null, contentType, null);
        return result;
    }

    /**
     * 查询监控点列表v2
     * 接口说明：根据条件查询目录下有权限的监控点列表。
     * 接口文档地址：https://open.hikvision.com/docs/aec7933a37f34aedb03853ce289e74b7#a11a856d
     * @param jsonBody 请求参数
     * @param configKey 海康配置唯一标识
     * @return
     */
    public ArtemisMonitorPageVo getCameraSearchV2(JSONObject jsonBody, String configKey){
        String resultStr = null;
        HKProperties.HKConfig config = hkProperties.getConfigByKey(configKey);
        if(MonitorConstant.isDebugModel.equals(isDebug))
        {
            //测试环境，返回测试数据
            resultStr = "{\"code\":\"0\",\"msg\":\"SUCCESS\",\"data\":{\"total\":20,\"pageNo\":1,\"pageSize\":10,\"list\":[{\"regionIndexCode\":\"e30a2db1-f67f-4588-bf09-59fea5870730\",\"dacIndexCode\":\"--\",\"regionPathName\":\"张家湾再生水厂/门禁识别一体机\",\"latitude\":\"\",\"regionName\":\"门禁识别一体机\",\"indexCode\":\"e1cc2b879f2c4c8899b1038875b01721\",\"channelType\":\"analog\",\"disOrder\":750,\"capability\":\"@vss@net@maintenance@event_device@status@\",\"parentIndexCode\":\"e1a98650151f4565889eeb15b28ec4dc\",\"longitude\":\"\",\"chanNum\":1,\"regionPath\":\"@root000000@e30a2db1-f67f-4588-bf09-59fea5870730@\",\"cascadeType\":0,\"installLocation\":\"\",\"updateTime\":\"2020-11-11T12:34:07.342+08:00\",\"sort\":750,\"cameraType\":0,\"treatyType\":\"hiksdk_net\",\"cameraRelateTalk\":\"54cfa5c9cf54456eaf9d5e9a7fa5451b\",\"transType\":1,\"createTime\":\"2020-11-11T12:31:28.899+08:00\",\"name\":\"PLC1室\",\"decodeTag\":\"hikvision\",\"resourceType\":\"camera\"},{\"regionIndexCode\":\"e30a2db1-f67f-4588-bf09-59fea5870730\",\"dacIndexCode\":\"--\",\"regionPathName\":\"张家湾再生水厂/门禁识别一体机\",\"latitude\":\"\",\"regionName\":\"门禁识别一体机\",\"indexCode\":\"c60d24dafdcf43d190023cc61783dfc8\",\"channelType\":\"analog\",\"disOrder\":749,\"capability\":\"@vss@net@maintenance@event_device@status@\",\"parentIndexCode\":\"1f7064571769485c89df40012f292ec4\",\"longitude\":\"\",\"chanNum\":1,\"regionPath\":\"@root000000@e30a2db1-f67f-4588-bf09-59fea5870730@\",\"cascadeType\":0,\"installLocation\":\"\",\"updateTime\":\"2020-11-11T12:34:34.831+08:00\",\"sort\":749,\"cameraType\":0,\"treatyType\":\"hiksdk_net\",\"cameraRelateTalk\":\"e212f04622214549a657eddaff03254d\",\"transType\":1,\"createTime\":\"2020-11-11T12:31:28.569+08:00\",\"name\":\"粗格栅闸室\",\"decodeTag\":\"hikvision\",\"resourceType\":\"camera\"},{\"regionIndexCode\":\"e30a2db1-f67f-4588-bf09-59fea5870730\",\"dacIndexCode\":\"--\",\"regionPathName\":\"张家湾再生水厂/门禁识别一体机\",\"latitude\":\"\",\"regionName\":\"门禁识别一体机\",\"indexCode\":\"58cb3a3374274e2ab6245f2479e597d7\",\"channelType\":\"analog\",\"disOrder\":748,\"capability\":\"@vss@net@maintenance@event_device@status@\",\"parentIndexCode\":\"763a0c94c6584a7aabe1391f308954b4\",\"longitude\":\"\",\"chanNum\":1,\"regionPath\":\"@root000000@e30a2db1-f67f-4588-bf09-59fea5870730@\",\"cascadeType\":0,\"installLocation\":\"\",\"updateTime\":\"2020-11-11T12:34:56.976+08:00\",\"sort\":748,\"cameraType\":0,\"treatyType\":\"hiksdk_net\",\"cameraRelateTalk\":\"4adcb7397bf14dd5bdb91a0f2e414fc6\",\"transType\":1,\"createTime\":\"2020-11-11T12:31:28.377+08:00\",\"name\":\"危废间\",\"decodeTag\":\"hikvision\",\"resourceType\":\"camera\"},{\"regionIndexCode\":\"e30a2db1-f67f-4588-bf09-59fea5870730\",\"dacIndexCode\":\"--\",\"regionPathName\":\"张家湾再生水厂/门禁识别一体机\",\"latitude\":\"\",\"regionName\":\"门禁识别一体机\",\"indexCode\":\"5b1e4b3549ee488f8a583e9473e8c2d7\",\"channelType\":\"analog\",\"disOrder\":747,\"capability\":\"@vss@net@maintenance@event_device@status@\",\"parentIndexCode\":\"d8e7f37f9ea54fe88d75818b359c53ec\",\"longitude\":\"\",\"chanNum\":1,\"regionPath\":\"@root000000@e30a2db1-f67f-4588-bf09-59fea5870730@\",\"cascadeType\":0,\"installLocation\":\"\",\"updateTime\":\"2020-11-11T12:35:12.701+08:00\",\"sort\":747,\"cameraType\":0,\"treatyType\":\"hiksdk_net\",\"cameraRelateTalk\":\"8f95c4fdf6fa4ccfbaf24f8e63eb0ee9\",\"transType\":1,\"createTime\":\"2020-11-11T12:31:29.396+08:00\",\"name\":\"检验室\",\"decodeTag\":\"hikvision\",\"resourceType\":\"camera\"},{\"regionIndexCode\":\"e30a2db1-f67f-4588-bf09-59fea5870730\",\"dacIndexCode\":\"--\",\"regionPathName\":\"张家湾再生水厂/门禁识别一体机\",\"latitude\":\"\",\"regionName\":\"门禁识别一体机\",\"indexCode\":\"7ca561ff314443d1b81da7001bdc6e08\",\"channelType\":\"analog\",\"disOrder\":746,\"capability\":\"@vss@net@maintenance@event_device@status@\",\"parentIndexCode\":\"87ecc389e2454741a3bad69c98762809\",\"longitude\":\"\",\"chanNum\":1,\"regionPath\":\"@root000000@e30a2db1-f67f-4588-bf09-59fea5870730@\",\"cascadeType\":0,\"installLocation\":\"\",\"updateTime\":\"2020-11-11T12:35:28.313+08:00\",\"sort\":746,\"cameraType\":0,\"treatyType\":\"hiksdk_net\",\"cameraRelateTalk\":\"9f8c505d1e75483883168c895ca35c92\",\"transType\":1,\"createTime\":\"2020-11-11T12:31:28.131+08:00\",\"name\":\"财务室\",\"decodeTag\":\"hikvision\",\"resourceType\":\"camera\"},{\"regionIndexCode\":\"e30a2db1-f67f-4588-bf09-59fea5870730\",\"dacIndexCode\":\"--\",\"regionPathName\":\"张家湾再生水厂/门禁识别一体机\",\"latitude\":\"\",\"regionName\":\"门禁识别一体机\",\"indexCode\":\"21b5a8b7e67e4361ac7821fbd3c6fd3a\",\"channelType\":\"analog\",\"disOrder\":745,\"capability\":\"@vss@net@maintenance@event_device@status@\",\"parentIndexCode\":\"343309d064fc488398799c6164c739da\",\"longitude\":\"\",\"chanNum\":1,\"regionPath\":\"@root000000@e30a2db1-f67f-4588-bf09-59fea5870730@\",\"cascadeType\":0,\"installLocation\":\"\",\"updateTime\":\"2020-11-11T12:35:49.637+08:00\",\"sort\":745,\"cameraType\":0,\"treatyType\":\"hiksdk_net\",\"cameraRelateTalk\":\"98e6ca0357f34722967a34c5d0911eb5\",\"transType\":1,\"createTime\":\"2020-11-11T12:31:28.730+08:00\",\"name\":\"直流屏室\",\"decodeTag\":\"hikvision\",\"resourceType\":\"camera\"},{\"regionIndexCode\":\"e30a2db1-f67f-4588-bf09-59fea5870730\",\"dacIndexCode\":\"--\",\"regionPathName\":\"张家湾再生水厂/门禁识别一体机\",\"latitude\":\"\",\"regionName\":\"门禁识别一体机\",\"indexCode\":\"96520c4295bc4e3580e35d6a5f6505db\",\"channelType\":\"analog\",\"disOrder\":744,\"capability\":\"@vss@net@maintenance@event_device@status@\",\"parentIndexCode\":\"c2d6958d6e2a41fbadef9259ada4b7f6\",\"longitude\":\"\",\"chanNum\":1,\"regionPath\":\"@root000000@e30a2db1-f67f-4588-bf09-59fea5870730@\",\"cascadeType\":0,\"installLocation\":\"\",\"updateTime\":\"2020-11-11T12:36:18.031+08:00\",\"sort\":744,\"cameraType\":0,\"treatyType\":\"hiksdk_net\",\"cameraRelateTalk\":\"1381a9206b8a42b9860eb80229ee2bfe\",\"transType\":1,\"createTime\":\"2020-11-11T12:31:29.253+08:00\",\"name\":\"东侧加药间\",\"decodeTag\":\"hikvision\",\"resourceType\":\"camera\"},{\"regionIndexCode\":\"e30a2db1-f67f-4588-bf09-59fea5870730\",\"dacIndexCode\":\"--\",\"regionPathName\":\"张家湾再生水厂/门禁识别一体机\",\"latitude\":\"\",\"regionName\":\"门禁识别一体机\",\"indexCode\":\"ed885fd9fd8b4208af9ba211573a3de2\",\"channelType\":\"analog\",\"disOrder\":743,\"capability\":\"@vss@net@maintenance@event_device@status@\",\"parentIndexCode\":\"6003e31392db41f8b1c53320cd99be82\",\"longitude\":\"\",\"chanNum\":1,\"regionPath\":\"@root000000@e30a2db1-f67f-4588-bf09-59fea5870730@\",\"cascadeType\":0,\"installLocation\":\"\",\"updateTime\":\"2020-11-11T12:36:38.133+08:00\",\"sort\":743,\"cameraType\":0,\"treatyType\":\"hiksdk_net\",\"cameraRelateTalk\":\"ab8c67a8eef04472ae608c8c8b198998\",\"transType\":1,\"createTime\":\"2020-11-11T12:31:30.155+08:00\",\"name\":\"出水在线间\",\"decodeTag\":\"hikvision\",\"resourceType\":\"camera\"},{\"regionIndexCode\":\"e30a2db1-f67f-4588-bf09-59fea5870730\",\"dacIndexCode\":\"--\",\"regionPathName\":\"张家湾再生水厂/门禁识别一体机\",\"latitude\":\"\",\"regionName\":\"门禁识别一体机\",\"indexCode\":\"77b867f86f2b4ceeb2583186d6ce6d80\",\"channelType\":\"analog\",\"disOrder\":742,\"capability\":\"@vss@net@maintenance@event_device@status@\",\"parentIndexCode\":\"2e7c7f2cb03c4f1a9812967906a6fe60\",\"longitude\":\"\",\"chanNum\":1,\"regionPath\":\"@root000000@e30a2db1-f67f-4588-bf09-59fea5870730@\",\"cascadeType\":0,\"installLocation\":\"\",\"updateTime\":\"2020-11-11T12:37:02.083+08:00\",\"sort\":742,\"cameraType\":0,\"treatyType\":\"hiksdk_net\",\"cameraRelateTalk\":\"9874e0caa9454a6cb1cd92b2c1f9ef8c\",\"transType\":1,\"createTime\":\"2020-11-11T12:31:27.928+08:00\",\"name\":\"提升泵房\",\"decodeTag\":\"hikvision\",\"resourceType\":\"camera\"},{\"regionIndexCode\":\"e30a2db1-f67f-4588-bf09-59fea5870730\",\"dacIndexCode\":\"--\",\"regionPathName\":\"张家湾再生水厂/门禁识别一体机\",\"latitude\":\"\",\"regionName\":\"门禁识别一体机\",\"indexCode\":\"cc9ec4f9916544719f0141eb28fd682f\",\"channelType\":\"analog\",\"disOrder\":741,\"capability\":\"@vss@net@maintenance@event_device@status@\",\"parentIndexCode\":\"e7e556f8e436466aab05ea099a047bb5\",\"longitude\":\"\",\"chanNum\":1,\"regionPath\":\"@root000000@e30a2db1-f67f-4588-bf09-59fea5870730@\",\"cascadeType\":0,\"installLocation\":\"\",\"updateTime\":\"2020-11-11T12:37:20.022+08:00\",\"sort\":741,\"cameraType\":0,\"treatyType\":\"hiksdk_net\",\"cameraRelateTalk\":\"a7a5a59bcd9047b293a19a83850f626c\",\"transType\":1,\"createTime\":\"2020-11-11T12:31:29.917+08:00\",\"name\":\"悬浮机房\",\"decodeTag\":\"hikvision\",\"resourceType\":\"camera\"},{\"regionIndexCode\":\"e30a2db1-f67f-4588-bf09-59fea5870730\",\"dacIndexCode\":\"--\",\"regionPathName\":\"张家湾再生水厂/门禁识别一体机\",\"latitude\":\"\",\"regionName\":\"门禁识别一体机\",\"indexCode\":\"4306ae7f4eca424992d38144f64d9be5\",\"channelType\":\"analog\",\"disOrder\":740,\"capability\":\"@vss@net@maintenance@event_device@status@\",\"parentIndexCode\":\"2d0f8633ce584d778095badd7e93feaa\",\"longitude\":\"\",\"chanNum\":1,\"regionPath\":\"@root000000@e30a2db1-f67f-4588-bf09-59fea5870730@\",\"cascadeType\":0,\"installLocation\":\"\",\"updateTime\":\"2020-11-11T12:37:40.083+08:00\",\"sort\":740,\"cameraType\":0,\"treatyType\":\"hiksdk_net\",\"cameraRelateTalk\":\"b019c2959e2f4ec2877ea428db5dcdbf\",\"transType\":1,\"createTime\":\"2020-11-11T12:31:30.052+08:00\",\"name\":\"监控室\",\"decodeTag\":\"hikvision\",\"resourceType\":\"camera\"},{\"regionIndexCode\":\"e30a2db1-f67f-4588-bf09-59fea5870730\",\"dacIndexCode\":\"--\",\"regionPathName\":\"张家湾再生水厂/门禁识别一体机\",\"latitude\":\"\",\"regionName\":\"门禁识别一体机\",\"indexCode\":\"27b39a1d17084cc2ab4cf4896a22eb1c\",\"channelType\":\"analog\",\"disOrder\":739,\"capability\":\"@vss@net@maintenance@event_device@status@\",\"parentIndexCode\":\"74c9f6ae830743b7a1810dbeec586b11\",\"longitude\":\"\",\"chanNum\":1,\"regionPath\":\"@root000000@e30a2db1-f67f-4588-bf09-59fea5870730@\",\"cascadeType\":0,\"installLocation\":\"\",\"updateTime\":\"2020-11-11T12:37:53.720+08:00\",\"sort\":739,\"cameraType\":0,\"treatyType\":\"hiksdk_net\",\"cameraRelateTalk\":\"a6ba13cd247b4d089aa0325d03c3d69e\",\"transType\":1,\"createTime\":\"2020-11-11T12:31:29.075+08:00\",\"name\":\"设备间\",\"decodeTag\":\"hikvision\",\"resourceType\":\"camera\"}]}}";
            logger.warn("测试环境获取测试数据，{}", configKey);
        }
        else
        {
            //正式环境，返回接口数据
            //api名称，必须和配置文件中com.zcdy.dsc.hk.versionUrlMap后面的属性一致，才能找到不同版本对应的接口url
            String apiName = "cameraSearch";
            String apiVersion = config.getVersion();
            Map<String, String> urlMap = hkProperties.getVersionUrlMap().get(apiName);
            String url = urlMap.get(apiVersion);
            resultStr = postStringApi(url, jsonBody, configKey);
        }
       
        Map<String, Object> resultMap = FastJsonUtil.strToMap(resultStr);
        String code = String.valueOf(resultMap.get("code"));
        if ("0".equals(code)) {
            Object data = resultMap.get("data");
            ArtemisMonitorPageVo monitorPageVo = JSON.parseObject(JSON.toJSONString(data), ArtemisMonitorPageVo.class);
            ArtemisConfigVo artemisConfigVo = new ArtemisConfigVo(config);
            artemisConfigVo.setVersionUrlMap(hkProperties.getVerUrlMap(configKey));
            monitorPageVo.setConfig(artemisConfigVo);
            return monitorPageVo;
        } else {
            return null;
        }
    }

    /**
     * 获取监控点预览取流URL
     * 接口说明:
     *      1.平台正常运行；平台已经添加过设备和监控点信息。
     *      2.平台需要安装mgc取流服务。
     *      3.三方平台通过openAPI获取到监控点数据，依据自身业务开发监控点导航界面。
     *      4.调用本接口获取预览取流URL，协议类型包括：hik、rtsp、rtmp、hls。
     *      5.通过开放平台的开发包进行实时预览或者使用标准的GUI播放工具进行实时预览。
     *      6.为保证数据的安全性，取流URL设有有效时间，有效时间为5分钟。
     * 接口地址：https://open.hikvision.com/docs/69d3d37a56ec4d24a6596c3e6ed436af#b5bd6fd9
     * @param cameraIndexCode 设备编号
     * @param protocol 取流协议（应用层协议），
     *                  “hik”:HIK私有协议，使用视频SDK进行播放时，传入此类型；
     *                  “rtsp”:RTSP协议；
     *                  “rtmp”:RTMP协议；
     *                  “hls”:HLS协议（HLS协议只支持海康SDK协议、EHOME协议、ONVIF协议接入的设备；只支持H264视频编码和AAC音频编码）。
     *                  参数不填，默认为HIK协议
     * @return
     */
    public ArtemisMonitorPreviewVo getCamerasPreviewURLs(String cameraIndexCode, String protocol, String configKey){
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", cameraIndexCode);
        jsonBody.put("protocol", protocol);
        String methodName = "camerasPreview";
        String url = hkProperties.getUrlByMethodKey(methodName, configKey);
        String resultStr = postStringApi(url, jsonBody, configKey);
        Map<String, Object> resultMap = FastJsonUtil.strToMap(resultStr);
        String code = String.valueOf(resultMap.get("code"));
        if ("0".equals(code)) {
            Object data = resultMap.get("data");
            ArtemisMonitorPreviewVo monitorPreviewVo = JSON.parseObject(JSON.toJSONString(data), ArtemisMonitorPreviewVo.class);
            return monitorPreviewVo;
        } else {
            return null;
        }
    }
}
