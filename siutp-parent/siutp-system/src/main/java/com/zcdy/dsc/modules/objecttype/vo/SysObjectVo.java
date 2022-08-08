package com.zcdy.dsc.modules.objecttype.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;
import java.util.Map;

/**
 * @Description: 对象类型
 * @Author: 在信汇通
 * @Date:   2021-03-05
 * @Version: V1.0
 */
@Data
@ApiModel(value="sys_object", description="对象类型Vo")
public class SysObjectVo {
    
    /**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
    /**对象名称*/
    @ApiModelProperty(value = "对象名称")
    private java.lang.String name;
    /**对象类型*/
    @ApiModelProperty(value = "对象类型")
    private java.lang.String objType;
    /**对应表名*/
    @ApiModelProperty(value = "对应表名")
    private java.lang.String tableName;
    @ApiModelProperty(value = "属性列表")
    private List<Map<String, String>> fieldList;
}
