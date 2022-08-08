package com.zcdy.dsc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 海康综合安防监控点预览取流Vo类
 * @Author: 在信汇通
 * @Date: 2020-12-14 14:18:20
 * @Version: V1.0
 */
@ApiModel(value="ArtemisMonitorPreviewVo", description="海康综合安防监控点预览取流Vo类")
@Getter
@Setter
public class ArtemisMonitorPreviewVo {
    @ApiModelProperty(value = "取流URL，有效时间为5分钟")
    private String url;
}
