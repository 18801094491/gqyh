package com.zcdy.dsc.entity;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Description: 海康平台配置信息，列表形式
 * @Author: 在信汇通
 * @Date: 2021-01-07
 * @Version: V1.0
 */
@Api(tags="海康综合安防视频监控配置信息，数据来自properties文件，由于包含多个监控平台，所以数据结构为列表形式")
@Data
@Component
@Configuration
@ConfigurationProperties("com.zcdy.dsc.hk")
public class HKProperties
{
    @ApiModelProperty(value = "配置列表")
    private List<HKConfig> artemis;
    @ApiModelProperty(value = "不同版本的接口url地址")
    private Map<String, Map<String, String>> versionUrlMap = new HashMap<>();

    /**
     * 根据配置的key获取完整的配置信息
     * @param key 配置唯一标识
     * @return
     */
    @ApiOperation(value = "getConfigByKey", notes = "根据配置的key获取完整的配置信息")
    public HKConfig getConfigByKey(String key)
    {
        if(key == null || artemis == null)
        {
            return null;
        }
        for(HKConfig config : artemis)
        {
            if(key.equals(config.getKey()))
            {
                return config;
            }
        }
        return null;
    }

    /**
     * 根据key获取该配置所属版本对应的接口列表
     * @param key 配置唯一标识
     * @return
     */
    @ApiOperation(value = "getVerUrlMap", notes = "根据key获取该配置所属版本对应的接口列表")
    public Map<String, String> getVerUrlMap(String key)
    {
        Map<String, String> verUrlMap = new HashMap<>();
        HKConfig config = getConfigByKey(key);
        String version = config.getVersion();
        Set<String> keySet = versionUrlMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        while(iterator.hasNext())
        {
            String methodName = iterator.next();
            Map<String, String> versionMap = versionUrlMap.get(methodName);
            String methodUrl = versionMap.get(version);
            verUrlMap.put(methodName, methodUrl);
        }
        return verUrlMap;
    }

    /**
     * 根据方法名和版本号获取对应的接口url地址
     * @param method 方法名
     * @param key 配置唯一标识
     * @return
     */
    @ApiOperation(value = "getUrlByMethodKey", notes = "根据方法名和版本号获取对应的接口url地址")
    public String getUrlByMethodKey(String method, String key)
    {
        if(method == null || key == null)
        {
            return null;
        }
        Map<String, String> map = versionUrlMap.get(method);
        HKConfig config = getConfigByKey(key);
        return map.get(config.getVersion());
    }

    @Data
    @Api(tags="配置内部类")
    public static class HKConfig
    {
        private String name;
        private String host;
        private String appKey;
        private String appSecret;
        private String version;
        private String key;
    }

    @Override
    @ApiOperation(value = "toString", notes = "重写toString方法")
    public String toString()
    {
        return JSON.toJSONString(artemis);
    }
}
