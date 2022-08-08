package com.zcdy.dsc.modules.system.controller;

import com.alibaba.fastjson.JSON;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.framework.swagger.ApiVersion;
import com.zcdy.dsc.common.framework.swagger.VersionConstant;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.modules.system.entity.SysUser;
import com.zcdy.dsc.modules.system.service.ISysUserService;
import com.zcdy.dsc.modules.system.vo.AppRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 小程序权限控制
 * @Author: 在信汇通
 * @Date:   2021-04-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags="我的工单")
@RestController
@RequestMapping("/sys/role/")
public class AppRoleController
{
    @Value("${app.roles.permission}")
    private String roleJson;

    @Autowired
    private ISysUserService userService;

    /**
     * 根据用户token查询其所属app端用户组，并将其权限返回
     * @return
     */
    @ApiOperation(value="根据用户token查询其所属app端用户组，并将其权限返回")
    @GetMapping(value="/app/getRole")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public Result<?> getRole(HttpServletRequest req) {
        String username = JwtUtil.getUserNameByToken(req);
        SysUser user = userService.getUserByName(username);
        if(user == null)
        {
            return Result.error("未查询到用户信息，请重新登录");
        }
        String userAppRole = user.getAppRole();
        if(StringUtils.isBlank(userAppRole))
        {
            return getDefaultRole();
        }
        else
        {
            return getAppRoleByName(userAppRole);
        }
    }

    /**
     * 返回所有app角色集合
     * @return
     */
    @ApiOperation(value="返回所有app角色集合")
    @GetMapping(value="/getAppRoleList")
    public Result<List<AppRole>> getAppRoleList()
    {
        List<AppRole> appRoleList = JSON.parseArray(roleJson, AppRole.class);
        return Result.ok(appRoleList);
    }

    /**
     * 获取默认的移动端权限
     * @return
     */
    private Result<?> getDefaultRole()
    {
        List<AppRole> appRoleList = JSON.parseArray(roleJson, AppRole.class);
        for(AppRole appRole : appRoleList)
        {
            if ("1".equals(appRole.getIsDefault())) {
                return Result.ok(appRole);
            }
        }
        //没有默认返回null
        return Result.error("未找到默认的移动端权限配置，请联系管理员");
    }

    /**
     * 根据移动端角色名称返回权限
     * @param name
     * @return
     */
    private Result<?> getAppRoleByName(String name)
    {
        if(name == null)
        {
            return getDefaultRole();
        }
        List<AppRole> appRoleList = JSON.parseArray(roleJson, AppRole.class);
        for(AppRole appRole : appRoleList)
        {
            if (name.equals(appRole.getName()))
            {
                return Result.ok(appRole);
            }
        }
        //没找到返回null
        return Result.error("未找到对应的移动端权限配置，请联系管理员");
    }
}
