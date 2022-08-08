package com.zcdy.dsc.modules.settle.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.settle.entity.SettleCustomer;
import com.zcdy.dsc.modules.settle.param.EquipmentMeterPageParam;
import com.zcdy.dsc.modules.settle.param.SettleWaterParam;
import com.zcdy.dsc.modules.settle.vo.ContractParam;
import com.zcdy.dsc.modules.settle.vo.ContractVo;
import com.zcdy.dsc.modules.settle.vo.CustomerDropdown;
import com.zcdy.dsc.modules.settle.vo.CustomerVo;
import com.zcdy.dsc.modules.settle.vo.CustomerWaterInfoVo;
import com.zcdy.dsc.modules.settle.vo.EquipWatermeterVo;
import com.zcdy.dsc.modules.settle.vo.ExportCutomerVo;
import com.zcdy.dsc.modules.settle.vo.CustomerMeterInfoVo;

/**
 * @author： Roberto
 * 创建时间：2020年1月2日 下午2:07:12
 * 描述: <p></p>
 */
public interface ISettleService {


	/**
	 * 增加客户
	 * @param settleCustomer
	 */
	void addCustomerInfo(SettleCustomer settleCustomer);
	
	/**
	 * 分页查询列表
	 * @param page
	 * @param customerSn
	 * @param customerName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public IPage<CustomerVo>  getList(IPage<CustomerVo> page,String customerSn,String customerName,
			   String startTime,String endTime);

	/**
	 *  修改客户信息
	 * @param customer
	 */
	void update(SettleCustomer customer);

    /**
     * 回显客户详情信息
     * @param id
     * @return
     */
	public CustomerVo getDetail(String id);
	
	/**
	 * 查询导出数据
	 * @param customerSn
	 * @param customerName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<ExportCutomerVo> exportCutomerList(String customerSn,String customerName,
			   String startTime,String endTime);
	
	/**
	 * 客户页面查询所有水表信息
	 * @param page
	 * @param equipmentSn
	 * @param equipmentName
	 * @return
	 */
	public IPage<EquipWatermeterVo> getWatermeterList(IPage<EquipWatermeterVo> page,EquipmentMeterPageParam param);
	
	/**
	 * 查询水表是否已经被绑定
	 * @param equipmentId
	 * @return
	 */
	public Integer getBindEquipStatus(String equipmentId) ;
	
	/**
	 * 用户解绑水表
	 * @param id  水表绑定合同表主键id
	 * @param now
	 */
	public void unBindCustomerEquip(String id, String now);
	
	/**
	 * 通过客户id查询已关联水表相关信息
	 * @param customerId
	 * @return
	 */
	public List<CustomerWaterInfoVo> getBindWateInfoList(String customerId) ;

	
	/**
	 * 分页查询合同列表
	 * @param page
	 * @param contractSn
	 * @param contractName
	 * @param customer
	 * @return
	 */
	public IPage<ContractVo> queryContractList(Page<ContractVo> page, String contractSn, String contractName,String customer);

	/**
	 * 删除合同
	 * @param id
	 */
	void deleteContract(String id);

	/**
	 *  查询合同详情信息
	 * @param id
	 * @return
	 */
	ContractVo queryDetailById(String id);

	/**
	 * 查询客户名称列表
	 * @param customerName
	 * @return
	 */
	List<CustomerDropdown> queryCustomerNames(String customerName);

	/**
	 * 检验客户是否存在
	 * @param constomerId
	 * @return
	 */
	Integer checkCustomeExsit(String constomerId);

	/**
	 * 水表管理-分页列表查询
	 * @param page
	 * @param waterName
	 * @param waterSn
	 * @return
	 */
	IPage<CustomerMeterInfoVo> queryWaterPageData(Page<CustomerMeterInfoVo> page, SettleWaterParam param);


/**
 * 检验合同编号是否重复 
 * @param id
 * @param contractSn
 * @return
 */
	Boolean checkSnExist(String id, String contractSn);

	/**
	 * 修改合同内容
	 * @param contractParamVo
	 * @return
	 */
	String editContract(ContractParam contractParamVo);

	/**
	 * 校验合同是否绑定水表
	 * @param id 水表绑定合同表主键id
	 * @return
	 */
    Integer checkMeterBindContract(String id);
	
	
	
}
