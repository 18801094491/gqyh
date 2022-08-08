package com.zcdy.dsc.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 分页查询海康监控点列表参数
 * @Author: 在信汇通
 * @Date: 2020-12-10 16:41:18
 * @Version: V1.0
 */
@ApiModel(value = "ArtemisCameraPageParam", description = "分页查询海康监控点列表参数")
@Getter
@Setter
public class ArtemisMonitorPageParam extends AbstractPageParam {
    @ApiModelProperty(value = "监控点名称", notes = "模糊搜索，最大长度32，若包含中文，最大长度指不超过按照指定编码的字节长度，即getBytes(“utf-8”).length")
    private String name;
    @ApiModelProperty(value = "监控平台唯一标识", required = true, notes = "以此匹配监控平台的所有配置，然后调用对应平台的接口查询监控点")
    private String configKey;
}
