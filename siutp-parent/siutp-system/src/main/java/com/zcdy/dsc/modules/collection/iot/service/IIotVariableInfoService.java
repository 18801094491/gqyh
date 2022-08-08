package com.zcdy.dsc.modules.collection.iot.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.collection.iot.entity.IOServerTag;
import com.zcdy.dsc.modules.collection.iot.entity.IotVariableInfo;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.collection.iot.vo.IotVariableInfoVo;
import com.zcdy.dsc.modules.system.entity.SysDictItem;

import java.util.List;

/**
 * 描述: 采集变量信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-03
 * 版本号: V1.0
 */
public interface IIotVariableInfoService extends IService<IotVariableInfo> {
    //分页查看采集变量信息
    IPage<IotVariableInfoVo> getPageData(IPage<IotVariableInfoVo> page, String variableName,String variableTitle,String variableType);
    //查看启用停用结果集(暂时未用到)
    List<SysDictItem> queryWorking();
    //导出excel
    List<IotVariableInfoVo> getExportXls(String variableName,String variableTitle,String variableType);

    //查询要添加的变量是否名称重复
    List<IotVariableInfoVo> getIotVariableInfoVoByName(String id,String variableName);

    void editWorkingStatus(String workStatus, String id);
	/**
	 * @author：Roberto
	 * 创建时间:2020年4月7日 上午10:41:00
	 * 描述:<p>调用接口从IOServer同步变量信息，目前只有变量名称和变量类型信息</p>
	 */
	JSONObject syncIoServerTags();
	/**
	 * @author：Roberto
	 * 创建时间:2020年4月7日 上午11:59:06
	 * 描述:<p>同步IOServer变量到数据库表</p>
	 */
	void syncDbTags(List<IOServerTag> data, String userName);
	
	/**
	 * 描述: 获取7标段 2标段压力，流量，河东水厂瞬时流量
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月9日 下午8:32:13
	 * 版本: V1.0
	 */
	List<VariableInfo> getVariinfos(List<String> varNames);
}
