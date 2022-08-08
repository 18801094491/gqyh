package com.zcdy.dsc.modules.collection.iot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.system.api.ISysBaseApi;
import com.zcdy.dsc.common.util.HttpUtil;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.collection.iot.entity.IOServerTag;
import com.zcdy.dsc.modules.collection.iot.entity.IotVariableInfo;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.collection.iot.mapper.IotVariableInfoMapper;
import com.zcdy.dsc.modules.collection.iot.service.IIotVariableInfoService;
import com.zcdy.dsc.modules.collection.iot.vo.IotVariableInfoVo;
import com.zcdy.dsc.modules.system.entity.SysDictItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 采集变量信息
 * @author： 智能无人硬核心项目组
 * 创建时间： 2020-01-03
 * 版本号: V1.0
 */
@Service
public class IotVariableInfoServiceImpl extends ServiceImpl<IotVariableInfoMapper, IotVariableInfo>
		implements IIotVariableInfoService {

	private Logger logger = LoggerFactory.getLogger(IotVariableInfoServiceImpl.class);

	@Value("${com.zcdy.dsc.Ioserver.variable.url}")
	private String ioserverVariableUrl;

	@Resource
	private ISysBaseApi sysBaseAPI;

	@Autowired
	IotVariableInfoMapper iotVariableInfoMapper;

	// 查看启用停用结果集
	@Override
	public IPage<IotVariableInfoVo> getPageData(IPage<IotVariableInfoVo> page, String variableName,
			String variableTitle, String variableType) {
		return iotVariableInfoMapper.selectIotVariable(page, variableName, variableTitle, variableType);
	}

	// 查看启用停用结果集
	@Override
	public List<SysDictItem> queryWorking() {
		return iotVariableInfoMapper.selectWorking();
	}

	// 导出excel
	@Override
	public List<IotVariableInfoVo> getExportXls(String variableName, String variableTitle, String variableType) {
		return iotVariableInfoMapper.selectExportXls(variableName, variableTitle, variableType);
	}

	@Override
	public List<IotVariableInfoVo> getIotVariableInfoVoByName(String id, String variableName) {
		return iotVariableInfoMapper.getIotVariableInfoVoByName(id, variableName);
	}

	@Override
	public void editWorkingStatus(String workStatus, String id) {
		iotVariableInfoMapper.editWorkingStatus(workStatus, id);
	}

	/**
	 * @see com.zcdy.dsc.modules.collection.iot.service.IIotVariableInfoService#syncIoServerTags()
	 */
	@Override
	public JSONObject syncIoServerTags() {
		String url = this.ioserverVariableUrl.concat("/iot/var/vars");
		try {
			String content = HttpUtil.doGet(url);
			JSONObject json = JSON.parseObject(content);
			return json;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * @see com.zcdy.dsc.modules.collection.iot.service.IIotVariableInfoService#syncDbTags(List, String)
	 */
	@Async
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void syncDbTags(List<IOServerTag> data, String userName) {
		int addTotal = 0, updateTotal = 0;
		if (null != data && data.size() > 0) {
			List<IotVariableInfo> addList = new ArrayList<IotVariableInfo>();
			List<IotVariableInfo> updateList = new ArrayList<IotVariableInfo>();
			for (IOServerTag tag : data) {
				QueryWrapper<IotVariableInfo> query = new QueryWrapper<>();
				query.lambda().eq(IotVariableInfo::getVariableName, tag.getTagName());
				IotVariableInfo one = this.iotVariableInfoMapper.selectOne(query);
				if (null == one) {
					IotVariableInfo varinfo = new IotVariableInfo();
					varinfo.setVariableName(tag.getTagName());
					long dateType = tag.getDataType();
					String type = getDataType(dateType + "");
					varinfo.setDataType(type);
					if(!StringUtils.isEmpty(tag.getDescription())){
						varinfo.setVariableTitle(tag.getDescription());
						varinfo.setRemark(tag.getDescription());
					}
					varinfo.setWorkingStatus(StatusConstant.RUN + "");
					varinfo.setDelFlag(0);

					addList.add(varinfo);
				} else {
					IotVariableInfo updateOne = new IotVariableInfo();
					updateOne.setId(one.getId());
					if(!StringUtils.isEmpty(tag.getDescription())){
						updateOne.setRemark(tag.getDescription());
						updateOne.setVariableTitle(tag.getDescription());
					}
					updateOne.setVariableName(one.getVariableName());
					updateOne.setWorkingStatus(StatusConstant.RUN+"");
					updateOne.setDelFlag((int) StatusConstant.WORKING);
					updateList.add(updateOne);
				}
			}
			if (addList.size() > 0) {
				this.saveBatch(addList);
				addTotal = addTotal + addList.size();
			}
			if (updateList.size() > 0) {
				this.saveOrUpdateBatch(updateList);
				updateTotal += updateList.size();
			}
		}
		if (!StringUtils.isEmpty(userName)) {
			sysBaseAPI.sendSysAnnouncement("系统", userName, "变量同步完成通知",
					"变量同步已完成，具体信息如下：<p>新增：" + addTotal + "</p><p>更新：" + updateTotal + "</p><p>详情请查看变量管理！</p>");
		}
	}

	/**
	 * @author：Roberto
	 * @create:2020年4月7日 下午1:40:47
	 * 描述:<p>转换IOServer的变量数据类型为对应的数据库数据类型</p>
	 */
	private String getDataType(String dateType) {
		String type = "0";
		switch (dateType) {
		case "1":
			type = "1";
			break;
		case "2":
			type = "0";
			break;
		case "3":
			type = "0";
			break;
		case "4":
			type = "0";
			break;
		case "5":
			type = "0";
			break;
		case "6":
			type = "0";
			break;
		case "7":
			type = "0";
			break;
		case "8":
			type = "0";
			break;
		case "9":
			type = "3";
			break;
		case "10":
			type = "4";
			break;
		case "11":
			type = "2";
			break;
		default:
			break;
		}
		return type;
	}

	@Override
	@Cacheable(cacheNames="com.zcdy.dsc.modules.collection.iot.service.impl",key="#root.methodName",unless="#result==null")
	public List<VariableInfo> getVariinfos(List<String> varNames) {
		if(varNames.size()<=0){
			return null;
		}
		return iotVariableInfoMapper.selectVariInfos(varNames);
	}
}
