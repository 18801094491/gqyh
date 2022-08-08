package com.zcdy.dsc.modules.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.personal.vo.DepartUserVO;
import com.zcdy.dsc.modules.personal.vo.DepartmentVO;
import com.zcdy.dsc.modules.system.entity.SysUser;
import com.zcdy.dsc.modules.system.entity.SysUserDepart;
import com.zcdy.dsc.modules.system.model.DepartIdModel;

import java.util.List;

/**
 * <p>
 * SysUserDpeart用户组织机构service
 * </p>
 @author : songguang.jiao
 *
 */
public interface ISysUserDepartService extends IService<SysUserDepart> {
	

	/**
	 * 根据指定用户id查询部门信息
	 * @param userId
	 * @return
	 */
	List<DepartIdModel> queryDepartIdsOfUser(String userId);
	

	/**
	 * 根据部门id查询用户信息
	 * @param depId
	 * @return
	 */
	List<SysUser> queryUserByDepId(String depId);


    /**
     * @param pid 
     * @return 部门分组数据
     */
    List<DepartmentVO> getGroupDepartData(String pcode);


    /**
     * @param orgCode
     * @param realname
     * @return
     */
    List<DepartUserVO> getDepartUsers(String orgCode, String realname);
}
