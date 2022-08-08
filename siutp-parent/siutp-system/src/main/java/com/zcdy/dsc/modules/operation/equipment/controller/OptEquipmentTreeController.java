package com.zcdy.dsc.modules.operation.equipment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.modules.operation.equipment.entity.OptEquipment;
import com.zcdy.dsc.modules.operation.equipment.param.KnowlegePageParam;
import com.zcdy.dsc.modules.operation.equipment.service.IKnowlegeService;
import com.zcdy.dsc.modules.operation.equipment.service.IOptEquipmentService;
import com.zcdy.dsc.modules.operation.equipment.service.OptEquipmentTreeService;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentTreeQueryParam;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentTreeVo;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tangchao
 * 设备台账树
 * @date 2020/5/28
 */
@Api(tags = "设备树")
@RestController
@RequestMapping("/equipment/optEquipment")
public class OptEquipmentTreeController {

    @Resource
    private OptEquipmentTreeService optEquipmentTreeService;

    @Resource
    private IKnowlegeService knowlegeService;

    @Resource
    private IOptEquipmentService equipmentService;

    @GetMapping
    public Result<IPage<OptEquipmentTreeVo>> index(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            OptEquipmentTreeQueryParam queryParam) {
        Result<IPage<OptEquipmentTreeVo>> result = new Result<>();
        IPage<OptEquipmentTreeVo> list = this.optEquipmentTreeService.queryEquipmentInfoBylabelId(new Page(pageNo, pageSize), queryParam);
        result.setResult(list);
        result.setSuccess(true);
        return result;
    }


    /**
     * 获取详情
     *
     * @return
     */
    @ApiOperation("获取详情")
    @GetMapping("/detail/{id}")
    public Result<List<KnowlegeDataVo>> detail(@PathVariable("id") String id) {
        Result<List<KnowlegeDataVo>> result = new Result<>();
        //查询设备对应参数
        OptEquipment one = equipmentService.getById(id);
        if(one==null){
            return result.error500("设备信息错误,id不存在");
        }
        KnowlegePageParam param = new KnowlegePageParam();
        String equipmentMode = one.getEquipmentMode();
        param.setEquipmentModel(one.getEquipmentMode());
        param.setEquipmentSpecs(one.getEquipmentSpecs());
        param.setEquipmentType(one.getEquipmentType());
        param.setSupplier(one.getEquipmentSupplier());
        List<KnowlegeDataVo> data = knowlegeService.getData(param);
        data.forEach(item->{
            item.setEquipmentName(one.getEquipmentName());
        });
        result.setResult(data);
        return result.success();
    }

}
