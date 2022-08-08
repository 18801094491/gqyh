package com.zcdy.dsc.modules.system.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.modules.system.entity.SysRole;
import com.zcdy.dsc.modules.system.mapper.SysRoleMapper;
import com.zcdy.dsc.modules.system.service.ISysRoleService;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *@author : songguang.jiao
 
 * @since 2018-12-19
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    SysRoleMapper sysRoleMapper;
    
    @Value("${com.zcdy.dsc.file.upload.path}")
    private String uploadPath;
    
    @Override
    public Result importExcelCheckRoleCode(MultipartFile file, ImportParams params) throws Exception {
        List<SysRole> listSysRoles = ExcelImportUtil.importExcel(file.getInputStream(), SysRole.class, params);

        int totalCount = listSysRoles.size();

        List<String> errorStrs = new ArrayList<>();

        // 去除 listSysRoles 中重复的数据
        for (int i = 0; i < listSysRoles.size(); i++) {
            String roleCodeI = listSysRoles.get(i).getRoleCode();

            for (int j = i + 1; j < listSysRoles.size(); j++) {
                String roleCodeJ = listSysRoles.get(j).getRoleCode();
                // 发现重复数据
                if (roleCodeI.equals(roleCodeJ)) {
                    errorStrs.add("第 " + (j + 1) + " 行的 roleCode 值：" + roleCodeI + " 已存在，忽略导入");
                    listSysRoles.remove(j);
                    break;
                }
            }
        }

        // 去掉 sql 中的重复数据
        for (int i = 0; i < listSysRoles.size(); i++) {
            SysRole sysRoleExcel = listSysRoles.get(i);
            try {
                super.save(sysRoleExcel);
            } catch (org.springframework.dao.DuplicateKeyException e) {
                errorStrs.add("第 " + (i + 1) + " 行的 roleCode 值：" + sysRoleExcel.getRoleCode() + " 已存在，忽略导入");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (errorStrs.size() == 0) {
            return Result.ok("文件导入成功！总导入行数：" + totalCount);
        }
        JSONObject result = new JSONObject(5);
        result.put("totalCount", totalCount);
        result.put("errorCount", errorStrs.size());
        result.put("successCount", (errorStrs.size() - totalCount));
        result.put("msg", "总上传行数：" + totalCount + "，已导入行数：" + (errorStrs.size() - totalCount) + "，错误行数：" + errorStrs.size());
        String fileUrl = this.saveErrorTxtByList(errorStrs, "roleImportExcelErrorLog");
        int lastIndex = fileUrl.lastIndexOf(File.separator);
        String fileName = fileUrl.substring(lastIndex + 1);
        result.put("fileUrl", "/sys/common/download/" + fileUrl);
        result.put("fileName", fileName);
        Result res = Result.ok(result);

        res.setCode(201);
        res.setMessage("文件导入成功，但有错误。");
        return res;
    }

    @Override
    public void startOrStop(String id, Boolean status) {
        sysRoleMapper.startOrStop(id,status);
    }
    
    private String saveErrorTxtByList(List<String> msg, String name) {
        Date d = new Date();
        SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat formatStr = new SimpleDateFormat("yyyyMMdd");
        String saveDir = "logs" + File.separator + formatStr.format(d) + File.separator;
        String saveFullDir = uploadPath + File.separator + saveDir;

        File saveFile = new File(saveFullDir);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        name += yyyymmddhhmmss.format(d) + Math.round(Math.random() * 10000);
        String saveFilePath = saveFullDir + name + ".txt";

        try {
            //封装目的地
            BufferedWriter bw = new BufferedWriter(new FileWriter(saveFilePath));
            //遍历集合
            for (String s : msg) {
                //写数据
                if (s.indexOf("_") > 0) {
                    String[] arr = s.split("_");
                    bw.write("第" + arr[0] + "行:" + arr[1]);
                } else {
                    bw.write(s);
                }
                bw.write("\r\n");
            }
            //释放资源
            bw.flush();
            bw.close();
        } catch (Exception e) {
            log.error("excel导入生成错误日志文件异常:" + e.getMessage());
        }
        return saveDir + name + ".txt";
    }
}
