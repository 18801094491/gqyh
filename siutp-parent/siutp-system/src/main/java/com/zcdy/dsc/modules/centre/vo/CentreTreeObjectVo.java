package com.zcdy.dsc.modules.centre.vo;

import com.zcdy.dsc.modules.objecttype.vo.SysObjectVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 树形结构和绑定的属性vo
 * @Author: 在信汇通
 * @Date:   2021-02-20
 * @Version: V1.0
 */
@Data
@ApiModel(value="树形结构和绑定的属性vo", description="树形结构和绑定的属性vo")
public class CentreTreeObjectVo {
    
    /**id*/
    @ApiModelProperty(value = "id")
	private String id;
    /**名称*/
    @ApiModelProperty(value = "名称")
	private String name;
    /**所属中心*/
    @ApiModelProperty(value = "所属中心")
	private String centre;

    /**
     * 绑定的属性信息
     */
    @ApiModelProperty(value = "绑定的属性信息")
    private OptAttrTreeVo optAttrTreeVo;

    /**
     * 对应的对象类型
     */
    @ApiModelProperty(value = "对应的对象类型")
    private SysObjectVo sysObjectVo;
}
