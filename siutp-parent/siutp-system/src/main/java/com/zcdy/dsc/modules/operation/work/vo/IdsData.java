package com.zcdy.dsc.modules.operation.work.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 接收json数据id组
 *
 * @author： songguang.jiao
 * 创建时间： 2020年2月13日 下午3:19:09
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value = "IdsData", description = "接收json数据id组")
public class IdsData {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

}
