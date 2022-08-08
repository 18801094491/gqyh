package com.zcdy.dsc.modules.operation.equipment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.collection.iot.entity.IotContro;
import com.zcdy.dsc.modules.collection.iot.entity.IotVariableInfo;
import com.zcdy.dsc.modules.collection.iot.service.IIotVariableInfoService;
import com.zcdy.dsc.modules.collection.iot.service.IotControService;
import com.zcdy.dsc.modules.collection.iot.vo.IotControVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 描述: 反向控制
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-06 11:38:55
 * 版本号: V1.0
 */
@Api(tags = "反向控制")
@RestController
@RequestMapping("/equipment/iotContro")
public class IotControController extends BaseController<IotContro, IotControService> {
    @Autowired
    private IotControService iotControService;

    @Autowired
    private IIotVariableInfoService iotVariableInfoService;

    /**
     * 分页列表查询
     *
     * @param iotContro
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "反向控制-分页列表查询")
    @ApiOperation(value = "反向控制-分页列表查询", notes = "反向控制-分页列表查询")
    @GetMapping
    public Result<?> queryPageList(IotContro iotContro,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<IotContro> page = new Page<IotContro>(pageNo, pageSize);
        IPage<IotControVo> pageList = iotControService.queryPageList(page, iotContro);
        return Result.ok(pageList);
    }

    /**
     * 添加或修改
     *
     * @param iotContro
     * @return
     */
    @AutoLog(value = "反向控制-添加或修改")
    @ApiOperation(value = "反向控制-添加或修改", notes = "反向控制-添加或修改")
    @PostMapping(value = "one")
    public Result<Object> add(@RequestBody @Valid IotContro iotContro) {
        IotVariableInfo iotVariableInfo = iotVariableInfoService.getById(iotContro.getVariableId());
        iotContro.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        Result<Object> result = iotControService.checkDataFormat(iotVariableInfo.getDataType(), iotContro.getVariableValue());
        if (CommonConstant.SC_OK_200.equals(result.getCode())) {
            if (!StringUtils.isEmpty(iotContro.getId())) {
                iotControService.updateById(iotContro);
            } else {
                iotControService.save(iotContro);
            }
        }
        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "反向控制-通过id删除")
    @ApiOperation(value = "反向控制-通过id删除", notes = "反向控制-通过id删除")
    @DeleteMapping(value = "{id}")
    public Result<?> delete(@PathVariable("id") String id) {
        iotControService.removeById(id);
        return Result.ok("删除成功!");
    }


    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "反向控制-通过id查询")
    @ApiOperation(value = "反向控制-通过id查询", notes = "反向控制-通过id查询")
    @GetMapping(value = "/{id}")
    public Result<?> queryById(@PathVariable("id") String id) {
        IotContro iotContro = iotControService.getById(id);
        return Result.ok(iotContro);
    }


}