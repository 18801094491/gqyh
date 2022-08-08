package com.zcdy.dsc.modules.operation.equipment.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.equipment.constant.BillType;
import com.zcdy.dsc.modules.operation.equipment.entity.OptSparepart;
import com.zcdy.dsc.modules.operation.equipment.service.OptSparepartService;
import com.zcdy.dsc.modules.operation.equipment.service.StoreBillService;
import com.zcdy.dsc.modules.operation.equipment.vo.check.storebill.CheckStoreBill;
import com.zcdy.dsc.modules.operation.equipment.vo.store.SparepartBillParamVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.SparepartDropdown;
import com.zcdy.dsc.modules.operation.equipment.vo.store.SparepartVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.StoreBillVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 描述: 备品备件出入库管理
 * @author： 智能无人硬核心项目组
 * 创建时间： 2020-02-03 17:47:41
 * 版本号: V1.0
 */
@Api(tags = "备品备件出入库管理")
@RestController
@RequestMapping("/equipment/optSparepart")
public class OptSparepartController extends BaseController<OptSparepart, OptSparepartService> {
	@Autowired
	private OptSparepartService optSparepartService;
	@Autowired
	private StoreBillService storeBillService;

	/**
	 * 库存分页查询
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param sparepartName
	 * @param batchSn
	 * @return
	 */
	@AutoLog(value = "库存分页查询")
	@ApiOperation(value = "库存分页查询", notes = "库存分页查询")
	@GetMapping
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNo", value = "页码", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "每页显示条数", paramType = "query"),
			@ApiImplicitParam(name = "sparepartName", value = "备件名称", paramType = "query"),
			@ApiImplicitParam(name = "sparepartModel", value = "型号", paramType = "query"),
			@ApiImplicitParam(name = "storeId", value = "仓库id", paramType = "query"),
			@ApiImplicitParam(name = "sparepartName", value = "备件名称", paramType = "query"),
			@ApiImplicitParam(name = "sparepartSpecs", value = "规格", paramType = "query") })
	public Result<IPage<SparepartVo>> queryPageList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sparepartName,
			String sparepartModel, String sparepartSpecs,String storeId) {
		Page<SparepartVo> page = new Page<SparepartVo>(pageNo, pageSize);
		Result<IPage<SparepartVo>> result = new Result<IPage<SparepartVo>>();
		IPage<SparepartVo> pageList = storeBillService.queryPageData(page, sparepartName, sparepartModel,sparepartSpecs,storeId);
		result.setResult(pageList);
		result.success("success");
		return result;
	}

	/**
	 * 描述:入库
	 * @author： songguang.jiao
	 * 创建时间： 2020年3月11日 下午4:42:53
	 * 版本号: V1.0
	 */
	@AutoLog(value = "入库")
	@ApiOperation(value = "入库", notes = "入库")
	@PostMapping(value = "/inBill")
	public Result<Object> add(@RequestBody @Valid SparepartBillParamVo billParamVo) {
		storeBillService.addInBill(billParamVo);
		return Result.ok("保存成功！");
	}

	/**
	 * 描述:出库
	 * @author： songguang.jiao
	 * 创建时间： 2020年2月4日 上午9:46:16
	 * 版本号: V1.0
	 */
	@ApiOperation(value = "出库", notes = "出库")
	@PostMapping(value = "/outBill")
	public Result<Object> outBill(@RequestBody @Validated({ CheckStoreBill.class }) SparepartBillParamVo billParamVo) {
		Boolean checkBillStore = storeBillService.checkBillStore(billParamVo);
		if (checkBillStore) {
			return Result.error("超过最大库存!");
		}
		storeBillService.addOutBill(billParamVo);
		return Result.ok("出库成功");
	}

	/**
	 * 描述: 调整库存报警值
	 * @return
	 * @author： songguang.jiao
	 * 创建时间： 2020年3月11日 下午6:29:26
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "备品备件id", paramType = "query"),
			@ApiImplicitParam(name = "warnAmount", value = "库存报警值", paramType = "query") })
	@ApiOperation(value = "调整库存报警值", notes = "调整库存报警值")
	@GetMapping(value = "/editWarnAmount")
	public Result<Object> editWarnAmount(String id, Integer warnAmount) {
		UpdateWrapper<OptSparepart> updateWrapper = new UpdateWrapper<>();
		updateWrapper.lambda().eq(OptSparepart::getId, id);
		optSparepartService.update(new OptSparepart().setWarnAmount(warnAmount), updateWrapper);
		return Result.ok("调整成功");
	}

	/**
	 * 描述: 出库单分页查询
	 * @author： songguang.jiao
	 * 创建时间： 2020年2月3日 下午9:30:29
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNo", value = "页码", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "每页显示条数", paramType = "query"),
			@ApiImplicitParam(name = "sparepartName", value = "备件名称", paramType = "query"),
			@ApiImplicitParam(name = "batchSn", value = "批次号", paramType = "query") })
	@ApiOperation(value = "出库单分页查询", notes = "出库单分页查询")
	@GetMapping("/outAll")
	public Result<IPage<StoreBillVo>> queryOutBillPageList(
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sparepartName,
			String batchSn) {
		Result<IPage<StoreBillVo>> result = new Result<>();
		Page<StoreBillVo> page = new Page<>(pageNo, pageSize);
		IPage<StoreBillVo> outBillPageList = storeBillService.queryBillPageList(page, sparepartName, batchSn,
				BillType.OUT);
		result.setResult(outBillPageList);
		result.success("查询列表成功");
		return result;
	}

	/**
	 * 描述: 入库单分页查询
	 * @author： songguang.jiao
	 * 创建时间： 2020年2月3日 下午9:30:29
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNo", value = "页码", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "每页显示条数", paramType = "query"),
			@ApiImplicitParam(name = "sparepartName", value = "备件名称", paramType = "query"),
			@ApiImplicitParam(name = "batchSn", value = "批次号", paramType = "query") })
	@ApiOperation(value = "入库单分页查询", notes = "入库单分页查询")
	@GetMapping("/inAll")
	public Result<IPage<StoreBillVo>> queryInBillPageList(
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String sparepartName,
			String batchSn) {
		Result<IPage<StoreBillVo>> result = new Result<>();
		Page<StoreBillVo> page = new Page<>(pageNo, pageSize);
		IPage<StoreBillVo> inBillPageList = storeBillService.queryBillPageList(page, sparepartName, batchSn,
				BillType.IN);
		result.setResult(inBillPageList);
		result.success("查询列表成功");
		return result;
	}

	/**
	 * 描述: 备品备件下拉选
	 * @author： songguang.jiao
	 * 创建时间： 2020年2月4日 下午1:32:10
	 * 版本号: V1.0
	 */
	@ApiOperation(value = "备品备件下拉选", notes = "备品备件下拉选")
	@GetMapping("/dropdown")
	public Result<List<SparepartDropdown>> dropdown(String sparepartName) {
		Result<List<SparepartDropdown>> result = new Result<List<SparepartDropdown>>();
		List<SparepartDropdown> sparePartList = storeBillService.dropdown(sparepartName);
		result.setResult(sparePartList);
		result.success("查询成功");
		return result;
	}
}