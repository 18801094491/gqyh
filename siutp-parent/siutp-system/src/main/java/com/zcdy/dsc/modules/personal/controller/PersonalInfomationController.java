 package com.zcdy.dsc.modules.personal.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.modules.system.entity.SysUser;
import com.zcdy.dsc.modules.system.model.AnnouncementSendModel;
import com.zcdy.dsc.modules.system.service.ISysAnnouncementSendService;
import com.zcdy.dsc.modules.system.service.ISysUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用户个人数据控制器
 * @author Roberto
 * @date 2020/05/11
 */
@RestController
@RequestMapping
@Api(tags = "用户个人数据接口组")
public class PersonalInfomationController {

    @Autowired
    private ISysUserService sysUserService;
    
    @Autowired
    private ISysAnnouncementSendService sysAnnouncementSendService;    
    
    @ApiOperation(value = "获取个人信息", notes = "获取个人信息")
    @GetMapping(value = "/sys/user/my")
    public Result<SysUser> getUserInfo() {
        Result<SysUser> result = new Result<>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SysUser sysUser = sysUserService.getUserByName(loginUser.getUsername());
        result.setResult(sysUser);
        result.success("获取成功");
        return result;
    }
    
    /**
     * 获取我的消息
     * @param announcementSendModel
     * @param pageNo
     * @param pageSize
     * @return 我的消息分页数据
     */
    @ApiOperation(value = "获取个人的消息", notes = "获取个人信息")
    @GetMapping(value = "/sys/sysAnnouncementSend/getMyAnnouncementSend")
    public Result<IPage<AnnouncementSendModel>> getMyAnnouncementSend(AnnouncementSendModel announcementSendModel,
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        Result<IPage<AnnouncementSendModel>> result = new Result<IPage<AnnouncementSendModel>>();
        LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        announcementSendModel.setUserId(userId);
        announcementSendModel.setPageNo((pageNo-1)*pageSize);
        announcementSendModel.setPageSize(pageSize);
        Page<AnnouncementSendModel> pageList = new Page<AnnouncementSendModel>(pageNo,pageSize);
        pageList = sysAnnouncementSendService.getMyAnnouncementSendPage(pageList, announcementSendModel);
        result.setResult(pageList);
        result.setSuccess(true);
        return result;
    }
}
