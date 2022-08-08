package com.zcdy.dsc.modules.centre.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author: Roberto
 * 创建时间:2020年4月23日 下午2:32:09
 * 描述: <p>设备管理查询参数</p>
 */
@Setter
@Getter
@ToString
@ApiModel(value="EquipmentQueryParam", description="设备管理查询参数")
public class EquipmentQueryParam extends AbstractPageParam {

	@ApiModelProperty(value="标段", notes="所属标段")
	private String optSection;
	
	@ApiModelProperty(value="设备类型", notes="设备类型")
	private String equipmentType;
	
	@ApiModelProperty(value="放置位置", notes="放置位置")
	private String optLocation;
	
	@ApiModelProperty(value="设备状态", notes="设备状态")
	private String equipmentRevstop;
	
	@ApiModelProperty(value="设备类别", notes="设备类别")
	private String equipmentCategory;
	
	@ApiModelProperty(value="供应商", notes="供应商")
	private String equipmentSupplier;
	
	@ApiModelProperty(value="设备编码", notes="设备编码")
	private String equipmentSn;
	
	@ApiModelProperty(value="设备名称", notes="设备名称")
	private String optName;
	@ApiModelProperty(value="所属中心", notes="所属中心")
	private String centre;
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
