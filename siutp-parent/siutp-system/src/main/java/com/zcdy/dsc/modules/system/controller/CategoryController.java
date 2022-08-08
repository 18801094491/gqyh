package com.zcdy.dsc.modules.system.controller;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.modules.system.service.ISysCategoryService;
import com.zcdy.dsc.modules.system.vo.SysCateDropdown;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author： Roberto
 * 创建时间：2020年1月16日 上午11:52:08
 * 描述: <p>分类字典接口</p>
 */
@RestController
@RequestMapping("sys/cate")
public class CategoryController {

    @Resource
    private ISysCategoryService sysCategoryService;

    /**
     * @author：Roberto
     * @create:2020年1月16日 下午12:50:07
     * 描述:<p>分类字典查询接口</p>
     */
    @GetMapping("/{pcate}")
    public Result<List<SysCateDropdown>> getCategoryInfo(@PathVariable String pcate) {
        Result<List<SysCateDropdown>> result = new Result<>();
        List<SysCateDropdown> data = this.sysCategoryService.queryKeyValueByParentCode(pcate);
        result.success("success");
        result.setResult(data);
        return result;
    }
}
