package com.zcdy.dsc.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.common.exception.BusinessException;
import com.zcdy.dsc.modules.system.entity.SysCategory;
import com.zcdy.dsc.modules.system.model.TreeSelectModel;
import com.zcdy.dsc.modules.system.vo.SysCateDropdown;

import java.util.List;
import java.util.Map;

/**
 * 描述: 分类字典
 @author : songguang.jiao
 
 * 版本号: V1.0
 */
public interface ISysCategoryService extends IService<SysCategory> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";

	void addSysCategory(SysCategory sysCategory);
	
	void updateSysCategory(SysCategory sysCategory);
	
	/**
	  * 根据父级编码加载分类字典的数据
	 * @param pcode
	 * @return
	 */
	public List<TreeSelectModel> queryListByCode(String pcode) throws BusinessException;
	
	/**
	  * 根据pid查询子节点集合
	 * @param pid
	 * @return
	 */
	public List<TreeSelectModel> queryListByPid(String pid);

	/**
	 * 根据pid查询子节点集合,支持查询条件
	 * @param pid
	 * @param condition
	 * @return
	 */
	public List<TreeSelectModel> queryListByPid(String pid, Map<String,String> condition);

	/**
	 * 根据code查询id
	 * @param code
	 * @return
	 */
	public String queryIdByCode(String code);
	
	/**
	 * 查询下拉选
	 * @param pid
	 * @return
	 */
	public List<SysCateDropdown> queryKeyValue(String pid);
	/**
	 * 查询下拉选
	 * @param code
	 * @return
	 */
	public String queryKeyValueCode(String code);

	/**
	 * @author：Roberto
	 * @create:2020年1月16日 下午12:44:10
	 * 描述:<p></p>
	 */
	List<SysCateDropdown> queryKeyValueByParentCode(String key);

}
