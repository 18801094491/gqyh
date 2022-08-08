package com.zcdy.dsc.modules.operation.equipment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.modules.operation.equipment.constant.GoodsItemStatus;
import com.zcdy.dsc.modules.operation.equipment.entity.OptTool;
import com.zcdy.dsc.modules.operation.equipment.service.IOperatToolService;
import com.zcdy.dsc.modules.operation.equipment.service.IToolService;
import com.zcdy.dsc.modules.operation.equipment.vo.store.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

 /**
 * 描述: 工具管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
@Api(tags="工具管理")
@RestController
@RequestMapping("/equipment/operatTool")
public class OperatToolController extends BaseController<OptTool, IOperatToolService> {
	
	@Autowired
	private IToolService toolService;
	
	/**
	 * 分页列表查询
	 *
	 * @param operatTool
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiImplicitParams({
	 	@ApiImplicitParam(name="pageNo",value="页码",paramType="query"),
	    @ApiImplicitParam(name="pageSize",value="每页显示条数",paramType="query"),
	    @ApiImplicitParam(name="toolName",value="工具名称",paramType="query"),
	    @ApiImplicitParam(name="toolModel",value="规格",paramType="query"),
	    @ApiImplicitParam(name="storeId",value="仓库id",paramType="query")
	})
	@AutoLog(value = "工具管理-分页列表查询")
	@ApiOperation(value="工具管理-分页列表查询", notes="工具管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ToolVo>> queryPageList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String toolName,String storeId,String toolModel) {
		Result<IPage<ToolVo>> result=new Result<>();
		Page<ToolVo> page=new Page<>(pageNo,pageSize);
		IPage<ToolVo> toolList = toolService.queryToolList(page, toolName, storeId, toolModel);
		result.setResult(toolList);
		result.success("查询成功");
		return result;
	}
	

	/**
	 * 描述: 查询仓库地址下拉选
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月9日 下午5:58:35
	 * 版本号: V1.0
	 */
	@AutoLog(value = "查询仓库地址下拉选")
	@ApiOperation(value="查询仓库下拉选", notes="查询仓库地址下拉选")
	@GetMapping(value = "/queryStoreList")
	public Result<List<StoreDropdownVo>> queryStoreList(){
		Result<List<StoreDropdownVo>> result=new Result<>();
		List<StoreDropdownVo> storeList = toolService.queryStoreList();
		result.setResult(storeList);
		result.success("success");
		return result;
	}
	
	
	/**
	 * 描述: 入库
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月6日 下午4:04:11 
	 * 版本号: V1.0
	 */
	@AutoLog(value = "入库")
	@ApiOperation(value="入库", notes="入库")
	@PostMapping(value = "/inToolStore")
	public Result<Object> addToolStore(@RequestBody @Valid ToolBillParamVo optToolBillVo) {
		 toolService.addToolStore(optToolBillVo);
		return Result.ok("入库成功");
	}

	/**
	 * 描述: 运维工具导出
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 上午9:53:17 
	 * 版本号: V1.0
	 */
	@AutoLog(value = "运维工具导出")
	@ApiOperation(value="运维工具导出", notes="运维工具导出")
	@GetMapping("/export")
	public ModelAndView export( String toolName,String storeId,String toolModel){
		List<ToolExportVo> pageList = toolService.exportCutomerList(toolName, storeId,toolModel);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime=sdf.format(new Date());
		String secondTitle="导出人:" + sysUser.getRealname()+"  导出时间："+nowTime;
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "运维工具管理"); 
        mv.addObject(NormalExcelConstants.CLASS, ToolExportVo.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("运维工具管理" + "报表", secondTitle, "运维工具管理"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
	}
	
	
	/**
	 * 描述: 提货按钮--查询运维工具借用详情
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 下午1:45:02 
	 * 版本号: V1.0
	 */
	@AutoLog(value = " 提货按钮--查询运维工具借用详情")
	@ApiOperation(value=" 提货按钮--查询运维工具借用详情", notes=" 提货按钮--查询运维工具借用详情")
	@GetMapping("/borrowDetail")
	@ApiImplicitParams({
	    @ApiImplicitParam(name="toolid",value="工具id",paramType="query"),
	 	@ApiImplicitParam(name="itemSn",value="工具编码",paramType="query"),
	 	 @ApiImplicitParam(name="batchSn",value="批次号",paramType="query"),
	 	  @ApiImplicitParam(name="pageNo",value="页码",paramType="query"),
		    @ApiImplicitParam(name="pageSize",value="每页显示条数",paramType="query"),
	})
	public Result<IPage<GoodsBorrowVo>> queryGoodsBorrowDetail(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			@RequestParam(required=true,value="toolid")String toolid,String itemSn,String batchSn){
		Result<IPage<GoodsBorrowVo>> result=new Result<IPage<GoodsBorrowVo>>();
		Page<GoodsBorrowVo> page=new Page<>(pageNo, pageSize);
		IPage<GoodsBorrowVo> list= toolService.queryGoodsBorrowDetail(page, toolid, itemSn, batchSn);
		result.setResult(list);
		result.success("查询成功");
		return result;
	}
	
	/**
	 * 描述: 货物出库
	 * 	货物申请表增加处理字段 dealFlag	(选择申请人,带出未处理的申请单,选择申请单记录借用历史)
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 下午5:45:20 
	 * 版本号: V1.0
	 */
	@ApiOperation(value="货物出库", notes="货物出库")
	@PostMapping("/goodsOut")
	public Result<Object> goodsOut(@RequestBody @Valid GoodsItemOutVo goodsItemOutVo){
		String[] splitIds = goodsItemOutVo.getIds().split(",");
		Integer count = toolService.checkOtherGoodsStatus(splitIds);
		if(count>0){
			return Result.error("只能选择状态为在库的货品");
		}
		toolService.changeGoodsStatus(splitIds,goodsItemOutVo.getUserId(),goodsItemOutVo.getApplySn());
		return Result.ok("出货成功");
	}
	
	/**
	 * 描述: 货物维护
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 下午9:58:18 
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({
	    @ApiImplicitParam(name="id",value="货物id",paramType="query"),
	 	@ApiImplicitParam(name="itemStatus",value="货物状态",paramType="query")
	})
	@ApiOperation(value="货物维护", notes="货物维护")
	@GetMapping("/editStatus")
	public Result<Object> editGoodsStatus(String id,String itemStatus){
		if(StringUtils.isEmpty(itemStatus) || StringUtils.isEmpty(id))	{
			return Result.error("请选择货物状态");
		}
		toolService.editGoodsStatus(id,itemStatus);
		return Result.ok("修改成功");
	}
	
	
	/**
	 * 描述: 货物归还
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 下午9:58:18 
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({
	    @ApiImplicitParam(name="id",value="货物id",paramType="query"),
	 	@ApiImplicitParam(name="itemStatus",value="货物状态",paramType="query"),
	    @ApiImplicitParam(name="description",value="备注",paramType="query")
	})
	@ApiOperation(value="货物归还", notes="货物归还")
	@GetMapping("/back")
	public Result<Object> backGoodsItem(String id,String itemStatus,String description){
		toolService.backGoodsItem(id,GoodsItemStatus.NORMAL,description);
		return Result.ok("归还成功");
	}
	
	/**
	 * 描述: 借用历史
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月8日 下午3:31:06 
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({
	    @ApiImplicitParam(name="userName",value="借用人姓名",paramType="query"),
	    @ApiImplicitParam(name="pageNo",value="页码",paramType="query"),
	    @ApiImplicitParam(name="pageSize",value="每页显示条数",paramType="query"),
	    @ApiImplicitParam(name="goodsId",value="货品id",paramType="query"),
	    @ApiImplicitParam(name="startTime",value="开始日期",paramType="query"),
	    @ApiImplicitParam(name="endTime",value="截至日期",paramType="query")
	})
	@ApiOperation(value="借用历史",notes="借用历史")
	@GetMapping("/history")
	public Result<IPage<BorrowHistoryVo>> queryBorrowHistory(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,String userName,String goodsId,
			   String startTime,String endTime){
		Result<IPage<BorrowHistoryVo>> result=new Result<>();
		Page<BorrowHistoryVo> page=new Page<>(pageNo, pageSize);
		IPage<BorrowHistoryVo> borrowHistory = toolService.queryBorrowHistory(page, userName,goodsId,startTime,endTime);
		result.setResult(borrowHistory);
		result.success("查询成功");
		return result;
	}
	
	/**
	 * 描述: 查询借用人的未处理的审核通过的运维工具申请单
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月8日 下午8:03:11 
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({
	    @ApiImplicitParam(name="userId",value="借用人id",paramType="query")
	})
	@ApiOperation(value="查询借用人申请订单",notes="查询借用人申请订单")
	@GetMapping("/goodsApply")
	public Result<List<GoodsApplyListVo>> queryGoodsApply(String userId){
		Result<List<GoodsApplyListVo>> result=new Result<>();
		if(StringUtils.isEmpty(userId)){
			result.error500("请选择借用人");
			return result;
		}
		List<GoodsApplyListVo> listVos=toolService.queryGoodsApply(userId);
		result.setResult(listVos);
		result.success("查询成功");
		return result;
	}
}
