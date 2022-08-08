package com.zcdy.dsc.modules.settle.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.settle.entity.District;
import com.zcdy.dsc.modules.settle.service.DistrictService;
import com.zcdy.dsc.modules.settle.vo.DistrictTreeVo;
import com.zcdy.dsc.modules.settle.vo.DistrictWaterCustomerInfoVo;
import com.zcdy.dsc.modules.system.entity.SysDict;
import com.zcdy.dsc.modules.system.service.ISysCategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 区域配置
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-04-20 10:45:47
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "区域配置")
@RestController
@RequestMapping("/settle/district")
public class DistrictController extends BaseController<District, DistrictService> {
    @Autowired
    private DistrictService districtService;

    /**
     * 根节点列表
     *
     * @return
     */
    @AutoLog(value = "区域配置-根节点查询")
    @ApiOperation(value = "区域配置-根节点查询", notes = "区域配置-根节点查询")
    @GetMapping(value = "/rootList")
    public Result<IPage<District>> rootList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        QueryWrapper<District> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(District::getParentId, ISysCategoryService.ROOT_PID_VALUE)
                .eq(District::getDelFlag, 0)
                .orderByAsc(District::getCreateTime);

        IPage<District> pageList = districtService.page(new Page<District>(pageNo, pageSize), wrapper);
        Result<IPage<District>> result = new Result<IPage<District>>();
        result.setSuccess(true);
        result.setCode(200);
        result.setResult(pageList);
        return result;
    }

    @GetMapping(value = "/childList")
    @AutoLog(value = "区域配置-子节点查询")
    @ApiOperation(value = "区域配置-子节点查询", notes = "区域配置-子节点查询")
    public Result<List<District>> queryPageList(@RequestParam(value = "parentId") String parentId) {
        Result<List<District>> result = new Result<List<District>>();
        QueryWrapper<District> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(District::getParentId, parentId)
                .eq(District::getDelFlag, 0)
                .orderByAsc(District::getCreateTime);
        List<District> list = districtService.list(wrapper);
        result.setSuccess(true);
        result.setResult(list);
        return result;
    }

    /**
     * 添加
     *
     * @param district
     * @return
     */
    @AutoLog(value = "区域配置-添加")
    @ApiOperation(value = "区域配置-添加", notes = "区域配置-添加")
    @PostMapping(value = "add")
    public Result<?> add(@RequestBody District district) {
        Result<District> result = new Result<>();
        try {
            districtService.addDistrict(district);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 修改
     *
     * @param district
     * @return
     */
    @AutoLog(value = "区域配置-修改")
    @ApiOperation(value = "区域配置-修改", notes = "区域配置-修改")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody District district) {
        Result<District> result = new Result<>();
        try {
            districtService.updateDistrict(district);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }


    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "区域配置-通过id删除")
    @ApiOperation(value = "区域配置-通过id删除", notes = "区域配置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam("id") String id) {
        Result<District> result = new Result<>();
        districtService.removeDistrict(id);
        return result;
    }

    /**
     * 通过ids 批量删除删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "区域配置-批量删除删除")
    @ApiOperation(value = "区域配置-批量删除删除", notes = "区域配置-批量删除删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam("ids") String ids) {
        Result<SysDict> result = new Result<SysDict>();
        if (ConvertUtils.isEmpty(ids)) {
            result.error500("参数不识别！");
        } else {
            districtService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }
    /**
     * 查询数据 查出所有区域,并以树结构数据格式响应给前端
     *
     * @return
     */
    @RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
    @AutoLog(value = "区域配置-树形结构查询")
    @ApiOperation(value = "区域配置-树形结构查询", notes = "区域配置-树形结构查询")
    public Result<List<DistrictTreeVo>> queryTreeList() {
        Result<List<DistrictTreeVo>> result = new Result<>();
        try {
        	//获取根节点和下面所有子节点
            List<DistrictTreeVo> list = districtService.getTreeById(ISysCategoryService.ROOT_PID_VALUE);
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return result;
    }

    /**
     * <p>
     * 通过区域名称关键字,模糊查询功能
     * </p>
     *
     * @param keyWord
     * @return
     */
    @RequestMapping(value = "/searchBy", method = RequestMethod.GET)
    public Result<List<DistrictTreeVo>> searchBy(@RequestParam(name = "keyWord", required = true) String keyWord) {
        Result<List<DistrictTreeVo>> result = new Result<List<DistrictTreeVo>>();
        try {
            List<DistrictTreeVo> treeList = this.districtService.searchBy(keyWord);
            if (treeList == null || treeList.size() == 0) {
                treeList = new ArrayList<>(1);
                DistrictTreeVo root = new DistrictTreeVo();
                root.setIsLeaf(false);
                root.setId("0");
                root.setPid("");
                root.setKey("0");
                root.setTitle("全部");
                root.setExpanded(false);
                treeList.add(root);
            }
            result.setSuccess(true);
            result.setResult(treeList);
            return result;
        } catch (Exception e) {
            e.fillInStackTrace();
            result.setSuccess(false);
            result.setMessage("没有符合的条件!");
            return result;
        }
    }

    /**
     * 通过区域ID查询水表客户合同等信息
     * @return
     */
    @GetMapping("/queryWaterAndCustomerInfoByDistrictId")
    @AutoLog(value = "区域客户-区域水表客户信息")
    @ApiOperation(value = "区域客户-区域水表客户信息", notes = "区域客户-区域水表客户信息")
    public Result<?>queryWaterAndCustomerInfoByDistrictId(@RequestParam("id") String districtId){
        Result<List<DistrictWaterCustomerInfoVo>> result = new Result<>();
        List<DistrictWaterCustomerInfoVo> list = this.districtService.queryWaterAndCustomerInfoByDistrictId(districtId);
        result.setResult(list);
        result.setSuccess(true);
        return result;
    }
}