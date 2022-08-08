package com.zcdy.dsc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description: 海康综合安防监控点查询资源列表Vo类
 * @Author: 在信汇通
 * @Date: 2020-12-14 10:57:13
 * @Version: V1.0
 */
@ApiModel(value="ArtemisMonitorPageVo", description="海康综合安防查询监控点列表Vo类")
@Getter
@Setter
public class ArtemisMonitorPageVo {
    /* 记录总数 */
    @ApiModelProperty(value = "记录总数")
    private Integer total;
    /* 当前页码 */
    @ApiModelProperty(value = "当前页码")
    private Integer pageNo;
    /* 分页大小 */
    @ApiModelProperty(value = "分页大小")
    private Integer pageSize;
    /* 监控点列表 */
    @ApiModelProperty(value = "监控点列表")
    private List<ArtemisMonitorVo> list;
    /* 监控平台配置 */
    @ApiModelProperty(value = "监控平台配置")
    private ArtemisConfigVo config;
}
