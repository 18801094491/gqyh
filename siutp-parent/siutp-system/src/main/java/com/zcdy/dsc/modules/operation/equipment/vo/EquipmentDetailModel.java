package com.zcdy.dsc.modules.operation.equipment.vo;

import java.util.List;

import com.zcdy.dsc.modules.collection.gis.vo.EquipmentAttrVO;
import com.zcdy.dsc.modules.collection.gis.vo.IotDataVO;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeEquipData;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资产设备所有状态信息
 * @author songguang.jiao
 * 2019年12月20日 下午7:20:17
 *
 * descriptions:
 *
 */
@Data
@ApiModel(value="EquipmentDetailModel",description="资产设备所有状态信息")
public class EquipmentDetailModel {
	
	
	/**
	 * 设备资产共有信息
	 */
	@ApiModelProperty(value="设备资产共有信息")
	private OptEquipmentModel optEquipmentModel;
	
	/**
	 * 资产设备特有属性列表
	 */
	@ApiModelProperty(value="资产设备特有属性列表")
	private List<EquipmentAttrVO> list;
	
	/**
	 * 设备采集数据
	 */	
	@ApiModelProperty(value="设备采集数据")
	private List<IotDataVO> equipData;
	
	//维保知识数据
	@ApiModelProperty(value="维保知识数据")
	private List<KnowlegeEquipData> knowlegeEquipData; 
}
