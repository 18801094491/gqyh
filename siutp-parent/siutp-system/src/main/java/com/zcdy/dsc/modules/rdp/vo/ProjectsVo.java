package com.zcdy.dsc.modules.rdp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 大屏重点项目Vo类
 * @Author: 在信汇通
 * @Date:   2020-12-10
 * @Version: V1.0
 */
@ApiModel(value="ProjectsVo", description="大屏重点项目Vo类")
@Getter
@Setter
public class ProjectsVo {
    /**主键id*/
    @ApiModelProperty(value = "主键id")
    private String id;

    /**名字*/
    @ApiModelProperty(value = "名字")
    private String name;
    /**经度*/
    @ApiModelProperty(value = "经度")
    private String longitude;
    /**纬度*/
    @ApiModelProperty(value = "纬度")
    private String latitude;

}
