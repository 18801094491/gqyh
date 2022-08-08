package com.zcdy.dsc.modules.collection.iot.service;

import com.zcdy.dsc.modules.collection.gis.entity.GisModel;
import com.zcdy.dsc.modules.collection.gis.vo.IotEquipInfoVo;
import com.zcdy.dsc.modules.collection.iot.entity.Iot2GisEntity;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;

import java.util.List;
import java.util.Map;

/**
 * @author： Roberto
 * 创建时间：2019年12月25日 下午4:29:05
 * 描述: <p>GIS模型自定义业务逻辑层</p>
 */
public interface IOTModelService {

	/**
	 * @author：Roberto
	 * @create:2020年1月6日 下午5:07:28
	 * 描述:<p></p>
	 */
	public Map<String, VariableInfo> getAllVarinfo();
	
	public List<VariableInfo> getAllVarinfoData();

	/**
	 * @author：Roberto
	 * @create:2020年1月6日 下午5:25:15
	 * 描述:<p>根据模型id查询模型绑定的变量</p>
	 */
	public List<VariableInfo> getVarsByModelId(String modelId);

	/**
	 * @author：Roberto
	 * @create:2020年3月5日 下午1:05:09
	 * 描述:<p>获取所有的模型信息</p>
	 */
	public List<GisModel> getAllGisModel();

	/**
	 * @author：Roberto
	 * @create:2020年3月10日 下午1:24:15
	 * 描述:<p>获取所有采集设备对照gis模型id</p>
	 */
	public List<Iot2GisEntity> getIot2Gis();

	/**
	 * @author：Roberto
	 * @create:2020年3月28日 下午4:20:12
	 * 描述:<p>获取采集设备的一个随机变量</p>
	 */
	public String getOneVarNameRandom(String equipmentId);

	/**
	 * 通过设备类型查询对应设备信息
	 * @author songguang.jiao
	 * @date 2020/05/20 12:00:26
	 * @param typeCode
	 * @return
	 */
    public List<IotEquipInfoVo> getEquipInfoByModelType(String typeCode);
}
