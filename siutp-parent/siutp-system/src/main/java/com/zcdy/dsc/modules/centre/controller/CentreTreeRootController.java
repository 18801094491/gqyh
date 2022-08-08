package com.zcdy.dsc.modules.centre.controller;

import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.centre.constant.CentreTypeEnum;
import com.zcdy.dsc.modules.centre.entity.CentreTree;
import com.zcdy.dsc.modules.centre.param.CentreTreeParam;
import com.zcdy.dsc.modules.centre.service.CentreTreeService;
import com.zcdy.dsc.modules.centre.vo.CentreVo;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
* @Description: 树形结构根目录维护
* @Author: 在信汇通
* @Date:   2021-02-20 11:33:45
* @Version: V1.0
*/
@Slf4j
@Api(tags="树形结构根目录维护")
@RestController
@RequestMapping("/centre/root")
public class CentreTreeRootController extends BaseController<CentreTree, CentreTreeService> {

    @Autowired
    protected CentreTreeService centreTreeService;

    /**
     * 获取中心集合
     *
     * @return
     */
    @AutoLog(value = "树形结构根目录-获取中心集合")
    @ApiOperation(value="树形结构根目录-获取中心集合", notes="树形结构根目录-获取中心集合")
    @GetMapping(value = "/centres/")
    public ResultData<List<CentreVo>> getCentreList(){
        CentreTypeEnum[] values = CentreTypeEnum.values();
        List<CentreVo> list = new ArrayList<>();
        for(CentreTypeEnum centreTypeEnum : values)
        {
            list.add(new CentreVo(centreTypeEnum));
        }
        return ResultData.ok(list);
    }

    /**
     * 分页列表查询
     *
     * @param centreTreeParam
     * @param req
     * @return
     */
    @AutoLog(value = "树形结构根目录-列表查询")
    @ApiOperation(value="树形结构根目录-列表查询", notes="树形结构根目录-列表查询")
    @GetMapping(value = "/list/")
    public ResultData<List<CentreTree>> queryTreeList(CentreTreeParam centreTreeParam, HttpServletRequest req)
    {
        CentreTree searchParam = new CentreTree(centreTreeParam);
        searchParam.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
        List<CentreTree> treeList = centreTreeService.getRootTreeList(searchParam);
        return ResultData.ok(treeList);
    }

    /**
     * 添加
     * @param centreTree
     * @return
     */
    @AutoLog(value = "树形结构根目录-添加")
    @ApiOperation(value="树形结构根目录-添加", notes="树形结构根目录-添加")
    @PostMapping(value = "/add")
    public ResultData<?> add(@RequestBody CentreTree centreTree, HttpServletRequest req) {
        ResultData<CentreTree> resultData = new ResultData<CentreTree>();
        try {
            Integer rootNum = centreTreeService.getRootNumByCentreObjId(centreTree);
            if(rootNum > 0)
            {
                return ResultData.error("该中心下已存在相同对象的根目录");
            }
            String username = JwtUtil.getUserNameByToken(req);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            centreTree.setId(uuid);
            centreTree.setCreateTime(new Date());
            centreTree.setCreateBy(username);
            boolean ok = centreTreeService.addRoot(centreTree);
            if(ok){
                resultData.success("保存成功！");
            }
            else{
                resultData.error500("操作失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
        }
        return resultData;
    }

    /**
     * 编辑
     * @param centreTree
     * @return
     */
    @AutoLog(value = "树形结构根目录-编辑")
    @ApiOperation(value="树形结构根目录-编辑", notes="树形结构根目录-编辑")
    @PostMapping(value = "/edit")
    public ResultData<?> edit(@RequestBody CentreTree centreTree, HttpServletRequest req) {
        ResultData<CentreTree> resultData = new ResultData<CentreTree>();
        try {
            if (centreTree == null || centreTree.getId() == null) {
                resultData.error500("id不能为空");
                return resultData;
            }
            String username = JwtUtil.getUserNameByToken(req);
            centreTree.setUpdateTime(new Date());
            centreTree.setUpdateBy(username);
            if(!checkBeforUpdateAndDel(centreTree.getId()))
            {
                //有关联数据，只更新名称
                int i = centreTreeService.updateNameById(centreTree);
                if(i == 1)
                {
                    return ResultData.ok("数据已被关联，名称已修改，其他信息不能变更");
                }
                else
                {
                    return ResultData.error("操作失败");
                }
            }
            else
            {
                boolean ok = centreTreeService.updateById(centreTree);
                if (ok) {
                    resultData.success("编辑成功！");
                } else {
                    resultData.error500("操作失败");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
        }
        return resultData;
    }

    /**
     * 修改删除前验证是否允许操作
     * 允许true；不允许false
     * @param id
     * @return
     */
    private boolean checkBeforUpdateAndDel(String id)
    {
        Integer num = centreTreeService.getObjNum4UseTreeId(id);
        if(num > 0)
        {
            //已被对象引用
            return false;
        }
        else
        {
            //未被引用
            return true;
        }
    }

    /**
      * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "树形结构根目录-通过id删除")
    @ApiOperation(value="树形结构根目录-通过id删除", notes="树形结构根目录-通过id删除")
    @DeleteMapping(value = "/delete")
    public ResultData<?> deleteById(@RequestParam("id") String id, HttpServletRequest req) {
        ResultData<CentreTree> resultData = new ResultData<CentreTree>();
        if (id == null) {
            return ResultData.error("id不能为空");
        }
        if(!checkBeforUpdateAndDel(id))
        {
            return ResultData.error("数据正在使用中，不能删除");
        }
        CentreTree centreTree = new CentreTree();
        centreTree.setId(id);
        centreTree.setDelFlag(DelFlagConstant.DELETED);//删除标记
        String username = JwtUtil.getUserNameByToken(req);
        centreTree.setUpdateTime(new Date());
        centreTree.setUpdateBy(username);
        boolean ok = centreTreeService.updateById(centreTree);//逻辑删除
        if (ok) {
            resultData.success("删除成功!");
        } else {
            resultData.error500("删除失败!");
        }
        return resultData;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "树形结构根目录-批量删除")
    @ApiOperation(value="树形结构根目录-批量删除", notes="树形结构根目录-批量删除")
    @DeleteMapping(value = "/deletes")
    public ResultData<?> deleteBatch(@RequestParam(name="ids",required=true) String ids, HttpServletRequest req) {
        ResultData<CentreTree> resultData = new ResultData<CentreTree>();
        if(ConvertUtils.isEmpty(ids)) {
            resultData.error500("参数不识别！");
        }else {
            List<String> list = Arrays.asList(ids.split(","));
            List<CentreTree> entityList = new ArrayList<>();
            boolean isAllDel = true;
            for(String s : list)
            {
                if(!checkBeforUpdateAndDel(s))
                {
                    isAllDel = false;
                    continue;
                }
                CentreTree ct = new CentreTree();
                ct.setId(s);
                ct.setDelFlag(DelFlagConstant.DELETED);//删除标记
                ct.setUpdateTime(new Date());
                String username = JwtUtil.getUserNameByToken(req);
                ct.setUpdateBy(username);
                entityList.add(ct);
            }
            if(entityList.size() == 0)
            {
                return ResultData.error("数据均在使用中，不能删除");
            }
            boolean ok = centreTreeService.updateBatchById(entityList);//批量逻辑删除
            if(ok){
                if(isAllDel)
                {
                    resultData.success("批量删除成功!");
                }
                else
                {
                    resultData.success("部分数据正在使用中，不能删除，其余对象已删除！");
                }
            }
            else {
                resultData.error500("操作失败");
            }
        }
        return resultData;
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @AutoLog(value = "树形结构根目录-通过id查询")
    @ApiOperation(value="树形结构根目录-通过id查询", notes="树形结构根目录-通过id查询")
    @GetMapping(value = "/query")
    public ResultData<?> queryById(String id) {
        ResultData<CentreTree> resultData = new ResultData<CentreTree>();
        try {
            if(ConvertUtils.isEmpty(id)) {
                resultData.error500("id不能为空");
                return resultData;
            }
            CentreTree centreTree = new CentreTree();
            centreTree.setId(id);
            centreTree.setDelFlag(DelFlagConstant.NORMAL);
            centreTree = centreTreeService.getById(centreTree);
            resultData.setData(centreTree);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }
}