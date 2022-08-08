package com.zcdy.dsc.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.modules.system.entity.SysRole;

import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *@author : songguang.jiao
 
 * @since 2018-12-19
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 导入 excel ，检查 roleCode 的唯一性
     *
     * @param file
     * @param params
     * @return
     * @throws Exception
     */
    Result importExcelCheckRoleCode(MultipartFile file, ImportParams params) throws Exception;

    /**
     * 启停用
     * @param id id
     * @param status 状态
     */
    void startOrStop(String id, Boolean status);
}
