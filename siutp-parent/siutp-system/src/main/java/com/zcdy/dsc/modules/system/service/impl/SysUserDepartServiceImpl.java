package com.zcdy.dsc.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.personal.vo.DepartUserVO;
import com.zcdy.dsc.modules.personal.vo.DepartmentVO;
import com.zcdy.dsc.modules.system.entity.SysDepart;
import com.zcdy.dsc.modules.system.entity.SysUser;
import com.zcdy.dsc.modules.system.entity.SysUserDepart;
import com.zcdy.dsc.modules.system.mapper.SysUserDepartMapper;
import com.zcdy.dsc.modules.system.model.DepartIdModel;
import com.zcdy.dsc.modules.system.service.ISysDepartService;
import com.zcdy.dsc.modules.system.service.ISysUserDepartService;
import com.zcdy.dsc.modules.system.service.ISysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <P>
 * 用户部门表实现类
 * <p/>
 * @author : songguang.jiao
 * @since 2019-02-22
 */
@Service
public class SysUserDepartServiceImpl extends ServiceImpl<SysUserDepartMapper, SysUserDepart>
    implements ISysUserDepartService {
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysUserService sysUserService;

    @Resource
    private SysUserDepartMapper sysUserDepartMapper;

    /**
     * 根据用户id查询部门信息
     */
    @Override
    public List<DepartIdModel> queryDepartIdsOfUser(String userId) {
        LambdaQueryWrapper<SysUserDepart> queryWrapper = new LambdaQueryWrapper<SysUserDepart>();
        LambdaQueryWrapper<SysDepart> queryDep = new LambdaQueryWrapper<SysDepart>();
        try {
            queryWrapper.eq(SysUserDepart::getUserId, userId);
            List<String> depIdList = new ArrayList<>();
            List<DepartIdModel> depIdModelList = new ArrayList<>();
            List<SysUserDepart> userDepList = this.list(queryWrapper);
            if (userDepList != null && userDepList.size() > 0) {
                for (SysUserDepart userDepart : userDepList) {
                    depIdList.add(userDepart.getDepId());
                }
                queryDep.in(SysDepart::getId, depIdList);
                List<SysDepart> depList = sysDepartService.list(queryDep);
                if (depList != null && depList.size() > 0) {
                    for (SysDepart depart : depList) {
                        depIdModelList.add(new DepartIdModel().convertByUserDepart(depart));
                    }
                }
                return depIdModelList;
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return null;

    }

    /**
     * 根据部门id查询用户信息
     */
    @Override
    public List<SysUser> queryUserByDepId(String depId) {
        LambdaQueryWrapper<SysUserDepart> wrapper = new LambdaQueryWrapper<SysUserDepart>();
        wrapper.eq(SysUserDepart::getDepId, depId);
        List<String> userIdList = new ArrayList<>();
        List<SysUserDepart> uDepList = this.list(wrapper);
        if (uDepList != null && uDepList.size() > 0) {
            for (SysUserDepart uDep : uDepList) {
                userIdList.add(uDep.getUserId());
            }
            List<SysUser> userList = (List<SysUser>)sysUserService.listByIds(userIdList);
            // update-begin-author:taoyan date:201905047 for:接口调用查询返回结果不能返回密码相关信息
            for (SysUser sysUser : userList) {
                sysUser.setSalt("");
                sysUser.setPassword("");
            }
            // update-end-author:taoyan date:201905047 for:接口调用查询返回结果不能返回密码相关信息
            return userList;
        }
        return new ArrayList<SysUser>();
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.modules.system.service.ISysUserDepartService#getGroupDepartData()
     */
    @Override
    public List<DepartmentVO> getGroupDepartData(String pcode) {
        String pid = null;
        if (!StringUtils.isEmpty(pcode)) {
            QueryWrapper<SysDepart> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysDepart::getOrgCode, pcode);
            SysDepart depart = this.sysDepartService.getOne(queryWrapper);
            pid = depart.getId();
        }
        return this.sysUserDepartMapper.selectDepartDataByGroup(pid);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.modules.system.service.ISysUserDepartService#getDepartUsers(java.lang.String, java.lang.String)
     */
    @Override
    public List<DepartUserVO> getDepartUsers(String orgCode, String realname) {
        return this.sysUserDepartMapper.selectDepartUsers(orgCode, realname);
    }
}
