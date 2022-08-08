package com.zcdy.dsc.modules.operation.equipment.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.equipment.entity.OptGoodsApply;
import com.zcdy.dsc.modules.operation.equipment.service.OptGoodsApplyService;
import com.zcdy.dsc.modules.operation.equipment.vo.OptGoodsApplyInfoVo;
import com.zcdy.dsc.modules.operation.equipment.vo.OptGoodsApplyParamVo;
import com.zcdy.dsc.modules.operation.equipment.vo.OptGoodsApplyVo;
import com.zcdy.dsc.modules.operation.equipment.vo.OptGoodsMyApplyVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.ToolDropdown;
import com.zcdy.dsc.modules.system.vo.SysCateDropdown;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 描述: 货物申领申请信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-05 11:21:27
 * 版本号: V1.0
 */
@Api(tags="货物申领申请信息")
@RestController
@RequestMapping("/equipment/optGoodsApply")
public class OptGoodsApplyController extends BaseController<OptGoodsApply, OptGoodsApplyService> {
	@Autowired
	private OptGoodsApplyService optGoodsApplyService;


	 /**
	  * 查看申请详情
	  *
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
	 @AutoLog(value = "申请详情")
	 @ApiOperation(value="申请详情", notes="申请详情")
	 @GetMapping(value = "/myInfo")
     @ApiImplicitParams({
             @ApiImplicitParam(name="pageNo",value="页码",paramType="query"),
             @ApiImplicitParam(name="pageSize",value="每页显示条数",paramType="query"),
             @ApiImplicitParam(name="name",value="创建人",paramType="query")
     })
	 public Result<IPage<OptGoodsApplyVo>> toApplyInfo(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								@RequestParam(name="pageSize", defaultValue="10") Integer pageSize, String name,String startTime ,String endTime, String verifyStatus) {

		 Page<OptGoodsApplyVo> page = new Page<OptGoodsApplyVo>(pageNo, pageSize);
		 Result<IPage<OptGoodsApplyVo>> result = new Result<IPage<OptGoodsApplyVo>>();
		 IPage<OptGoodsApplyVo> pageList = optGoodsApplyService.getApplyInfo(page, name,startTime,endTime,verifyStatus);
		 result.setResult(pageList);
		 result.success("success");
		 return result;
	 }

	 /**
	  * 查看我的申请详情
	  *
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
	 @AutoLog(value = "我的申请历史")
	 @ApiOperation(value="我的申请历史", notes="我的申请历史")
	 @GetMapping(value = "")
	 @ApiImplicitParams({
			 @ApiImplicitParam(name="pageNo",value="页码",paramType="query"),
			 @ApiImplicitParam(name="pageSize",value="每页显示条数",paramType="query"),
			 @ApiImplicitParam(name="name",value="当前登录者名称",paramType="query")
	 })
	 public Result<IPage<OptGoodsMyApplyVo>> applyHistory(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, String startTime ,String endTime, String verifyStatus) {
		 Page<OptGoodsMyApplyVo> page = new Page<OptGoodsMyApplyVo>(pageNo, pageSize);
		 Result<IPage<OptGoodsMyApplyVo>> result = new Result<IPage<OptGoodsMyApplyVo>>();
		 IPage<OptGoodsMyApplyVo> pageList = optGoodsApplyService.getApplyHistory(page, startTime,endTime,verifyStatus);
		 result.setResult(pageList);
		 result.success("success");
		 return result;
	 }

	/**
	 * 查看所有申请历史总
	 *
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "查看所有申请历史总")
	@ApiOperation(value="查看所有申请历史总", notes="查看所有申请历史总")
	@GetMapping(value = "/applyHistoryAll")
	@ApiImplicitParams({
			@ApiImplicitParam(name="pageNo",value="页码",paramType="query"),
			@ApiImplicitParam(name="pageSize",value="每页显示条数",paramType="query"),
			@ApiImplicitParam(name="name",value="名称查询",paramType="query")
	})
	public Result<IPage<OptGoodsMyApplyVo>> applyHistoryAll(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															String name,String startTime ,String endTime, String verifyStatus) {
		Page<OptGoodsMyApplyVo> page = new Page<OptGoodsMyApplyVo>(pageNo, pageSize);
		Result<IPage<OptGoodsMyApplyVo>> result = new Result<IPage<OptGoodsMyApplyVo>>();
		IPage<OptGoodsMyApplyVo> pageList = optGoodsApplyService.applyHistoryAll(page, name,startTime,endTime,verifyStatus);
		result.setResult(pageList);
		result.success("success");
		return result;
	}
	/**
	 * 工具申请(新增修改)
	 * @param optGoodsApply
	 * @return
	 */
	@AutoLog(value = "工具申请(新增修改)")
	@ApiOperation(value="工具申请(新增修改)", notes="工具申请(新增修改)")
	@PostMapping(value = "one")
	public Result<Object> add(@RequestBody OptGoodsApplyParamVo optGoodsApply) {
		if(!StringUtils.isEmpty(optGoodsApply.getId())){
		optGoodsApplyService.updateByOptId(optGoodsApply);
		}else{
		   optGoodsApplyService.saveInfo(optGoodsApply);
		}
		return Result.ok("保存成功！");
	}

	

	/**
	 * 描述:  通过id查看申请详情
	 * @param applyId
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月12日 下午10:24:45 
	 * 版本号: V1.0
	 */
	@AutoLog(value = "通过id查看申请详情")
	@ApiOperation(value="通过id查看申请详情", notes="通过id查看申请详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name="applyId",value="申请id",paramType="query")
	})
	@GetMapping(value = "/queryByapplyId")
		public Result<OptGoodsApplyInfoVo> queryByApplyId(@RequestParam(name="applyId") String applyId) {
		Result<OptGoodsApplyInfoVo> result = new Result<OptGoodsApplyInfoVo>();
		OptGoodsApplyInfoVo optGoodsApply = optGoodsApplyService.queryByApplyId(applyId);
		result.setResult(optGoodsApply);
		result.success("success");
		return result;
	}

	
	/**
	 * 描述:  通过仓库查询所有工具
	 * @param toolId 	
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月12日 下午9:31:30 
	 * 版本号: V1.0
	 */
	@ApiOperation(value="通过仓库查询所有工具", notes="通过仓库查询所有工具")
	@GetMapping(value = "/queryTool")
	public Result<List<SysCateDropdown>> queryToolByStore(String storeId){
		Result<List<SysCateDropdown>> result=new Result<List<SysCateDropdown>>();
		List<SysCateDropdown> dropdown= optGoodsApplyService.queryToolByStore(storeId);
		result.setResult(dropdown);
		result.success("success");
		return result;
	}
	
	/**
	 * 描述: 通过工具类型跟仓库查询所有规格
	 * @param toolId 	(查看紧急状态exigence_state)
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月12日 下午9:31:30 
	 * 版本号: V1.0
	 */
	@ApiOperation(value="通过工具类型跟仓库查询所有规格", notes="通过工具类型跟仓库查询所有规格")
	@GetMapping(value = "/queryModel")
	public Result<List<ToolDropdown>> queryModelByCode(String storeId,String code){
		Result<List<ToolDropdown>> result=new Result<List<ToolDropdown>>();
		List<ToolDropdown> dropdown= optGoodsApplyService.queryModelByCode(storeId,code);
		result.setResult(dropdown);
		result.success("success");
		return result;
	}
	
}