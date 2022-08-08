package com.zcdy.dsc.vo;

import com.zcdy.dsc.entity.HKProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @Description: 海康综合安防配置Vo类
 * @Author: 在信汇通
 * @Date: 2020-12-14 10:41:13
 * @Version: V1.0
 */
@ApiModel(value="ArtemisConfigVo", description="海康综合安防配置Vo类")
@Getter
@Setter
public class ArtemisConfigVo {
    /**海康综合安防API网关*/
    @ApiModelProperty(value = "海康综合安防API网关")
    private String hkArtemisHost;
    /**海康综合安防分配合作方AppKey*/
    @ApiModelProperty(value = "海康综合安防平台分配的合作方AppKey")
    private String hkArtemisAppKey;
    /**海康综合安防分配合作方AppSecret*/
    @ApiModelProperty(value = "海康综合安防平台分配的合作方AppSecret")
    private String hkArtemisAppSecret;
    /**海康综合安防平台名称name*/
    @ApiModelProperty(value = "海康综合安防平台名称name")
    private String name;
    /**海康综合安防平台版本号version*/
    @ApiModelProperty(value = "海康综合安防平台版本号version")
    private String version;
    /**海康综合安防平台唯一标识key*/
    @ApiModelProperty(value = "海康综合安防平台唯一标识key")
    private String key;
    /**当前配置的版本所对应的接口url集合versionUrlMap*/
    @ApiModelProperty(value = "当前配置的版本所对应的接口url集合versionUrlMap")
    private Map<String, String> versionUrlMap;

    public ArtemisConfigVo(){

    }

    public ArtemisConfigVo(HKProperties.HKConfig config){
        this.name = config.getName();
        this.hkArtemisAppKey = config.getAppKey();
        this.hkArtemisAppSecret = config.getAppSecret();
        this.hkArtemisHost = config.getHost();
        this.version = config.getVersion();
        this.key = config.getKey();
    }
}
