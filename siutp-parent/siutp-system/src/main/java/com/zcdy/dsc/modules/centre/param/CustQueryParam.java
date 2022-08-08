package com.zcdy.dsc.modules.centre.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 客户信息查询参数
 * @Author: 在信汇通
 * @Date:   2021-03-13
 * @Version: V1.0
 */
@Data
@ApiModel(value="客户信息查询参数", description="客户信息查询参数")
public class CustQueryParam  extends AbstractPageParam
{
    /**
     * 客户编码
     */
    @ApiModelProperty(value="客户编码", notes="客户编码")
    private String customerSn;
    /**
     * 客户名称
     */
    @ApiModelProperty(value="客户名称", notes="客户名称")
    private String customerName;
    /**
     * 创建日期开始
     */
    @ApiModelProperty(value="创建日期开始", notes="创建日期开始")
    private String startTime;
    /**
     * 创建日期结束
     */
    @ApiModelProperty(value="创建日期结束", notes="创建日期结束")
    private String endTime;
    /**
     * 所属中心
     */
    @ApiModelProperty(value="所属中心", notes="所属中心")
    private String centre;
    /**
     * 对象类型
     */
    @ApiModelProperty(value="对象类型", notes="对象类型")
    private String objType;
    @ApiModelProperty(value="父节点id", notes="父节点id")
    private String parentId;
    @ApiModelProperty(value="父节点id集合，逗号隔开", notes="父节点id集合，逗号隔开")
    private String parentIds;
    @ApiModelProperty(value="属性名集合，逗号隔开", notes="属性名集合，逗号隔开")
    private String attrNames;
    @ApiModelProperty(value="属性值集合，逗号隔开", notes="属性值集合，逗号隔开")
    private String attrValues;

    @ApiModelProperty(value="父节点id集合，sql使用", notes="父节点id集合，sql使用")
    private List<String> parentIdList;
    @ApiModelProperty(value="父节点id集合", notes="父节点id集合")
    private String parentIdsQueryStr;
}
