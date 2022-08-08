 package com.zcdy.dsc.modules.personal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.framework.swagger.ApiVersion;
import com.zcdy.dsc.common.framework.swagger.VersionConstant;
import com.zcdy.dsc.modules.personal.vo.DepartUserVO;
import com.zcdy.dsc.modules.personal.vo.DepartmentVO;
import com.zcdy.dsc.modules.personal.vo.DepartmentWithUserVO;
import com.zcdy.dsc.modules.system.entity.SysUser;
import com.zcdy.dsc.modules.system.model.SysUserSysDepartModel;
import com.zcdy.dsc.modules.system.service.ISysUserDepartService;
import com.zcdy.dsc.modules.system.service.ISysUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
  * 通讯录功能
 * @author Roberto
 * @date 2020/05/11
 */
@RestController
@RequestMapping
@Api(tags = "通讯录接口组")
public class AddressBookController {

    @Resource
    private ISysUserService sysUserService;
    
    @Resource
    private ISysUserDepartService sysUserDepartService;
    
    /**
     * 根据 orgCode 查询用户，包括子部门下的用户 针对通讯录模块做的接口，将多个部门的用户合并成一条记录，并转成对前端友好的格式
     */
    @GetMapping("/sys/user/queryByOrgCodeForAddressList")
    public Result<?> queryByOrgCodeForAddressList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "orgCode") String orgCode, SysUser userParams) {
        IPage<SysUserSysDepartModel> page = new Page<>(pageNo, pageSize);
        IPage<SysUserSysDepartModel> pageList = sysUserService.queryUserByOrgCode(orgCode, userParams, page);
        List<SysUserSysDepartModel> list = pageList.getRecords();

        // 记录所有出现过的 user, key = userId
        Map<String, JSONObject> hasUser = new HashMap<>(list.size());

        JSONArray resultJson = new JSONArray(list.size());

        for (SysUserSysDepartModel item : list) {
            String userId = item.getSysUser().getId();
            // userId
            JSONObject getModel = hasUser.get(userId);
            // 之前已存在过该用户，直接合并数据
            if (getModel != null) {
                String departName = getModel.get("departName").toString();
                getModel.put("departName", (departName + " | " + item.getSysDepart().getDepartName()));
            } else {
                // 将用户对象转换为json格式，并将部门信息合并到 json 中
                JSONObject json = JSON.parseObject(JSON.toJSONString(item.getSysUser()));
                json.remove("id");
                json.put("userId", userId);
                json.put("departId", item.getSysDepart().getId());
                json.put("departName", item.getSysDepart().getDepartName());

                resultJson.add(json);
                hasUser.put(userId, json);
            }
        }

        IPage<JSONObject> result = new Page<>(pageNo, pageSize, pageList.getTotal());
        result.setRecords(resultJson.toJavaList(JSONObject.class));
        return Result.ok(result);
    }
    
    @ApiOperation(notes = "通讯录-分组接口", value = "查询分组信息")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @GetMapping("/sys/addressbook/depart")
    public Result<List<DepartmentVO>> getAddressData(@RequestParam(required=false) String pcode) {
        List<DepartmentVO> departs = this.sysUserDepartService.getGroupDepartData(pcode);
        return Result.success(departs, "success");
    }
    
    @ApiOperation(notes = "通讯录-用户接口", value = "查询用户信息")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @GetMapping("/sys/addressbook/depart/{orgCode}/users")
    public Result<List<DepartUserVO>> getAddressbookUser(@PathVariable String orgCode, String realname) {
        List<DepartUserVO> departUsers = this.sysUserDepartService.getDepartUsers(orgCode, realname);
        return Result.success(departUsers, "success");
    }
    
    @ApiOperation(notes = "查询分组人员信息", value = "通讯录-分组人员信息，分组信息附带人员信息")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @GetMapping("/sys/addressbook/departuser")
    public Result<DepartmentWithUserVO> getAddressWithUserData(String orgCode, String realname) {
        
        List<DepartmentVO> departs = this.sysUserDepartService.getGroupDepartData(orgCode);
        List<DepartUserVO> departUsers = this.sysUserDepartService.getDepartUsers(orgCode, realname);
        DepartmentWithUserVO departmentWithUser = new DepartmentWithUserVO();
        departmentWithUser.setDepartments(departs);
        departmentWithUser.setDepartmentUsers(departUsers);
        return Result.success(departmentWithUser, "success");
    }
}
