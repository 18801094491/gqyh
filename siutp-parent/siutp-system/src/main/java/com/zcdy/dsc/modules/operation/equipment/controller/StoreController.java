package com.zcdy.dsc.modules.operation.equipment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.entity.OptStoreManager;
import com.zcdy.dsc.modules.operation.equipment.entity.Store;
import com.zcdy.dsc.modules.operation.equipment.entity.StoreTree;
import com.zcdy.dsc.modules.operation.equipment.service.IStoreService;
import com.zcdy.dsc.modules.operation.equipment.service.IToolService;
import com.zcdy.dsc.modules.operation.equipment.service.OptStoreManagerService;
import com.zcdy.dsc.modules.operation.equipment.vo.UserCodeName;
import com.zcdy.dsc.modules.operation.equipment.vo.store.StoreManagerVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.StoreParamVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.StoreUserName;
import com.zcdy.dsc.modules.operation.equipment.vo.store.StoreVo;
import com.zcdy.dsc.modules.system.model.TreeModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 /**
 * 描述: 仓库管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
@Api(tags="仓库管理")
@RestController
@RequestMapping("/equipment/store")
public class StoreController extends BaseController<Store, IStoreService> {
	@Autowired
	private IStoreService storeService;
	
	@Autowired
	private IToolService toolService;
	
	@Autowired
	private OptStoreManagerService storeManager; 
	
	/**
	 * 分页列表查询
	 *
	 * @param store
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiImplicitParams({
	 	@ApiImplicitParam(name="pageNo",value="页码",paramType="query"),
	    @ApiImplicitParam(name="pageSize",value="每页显示条数",paramType="query"),
	    @ApiImplicitParam(name="storeName",value="仓库名称",paramType="query"),
	 	@ApiImplicitParam(name="storeSn",value="仓库编码",paramType="query")
	})
	@AutoLog(value = "仓库管理-分页列表查询")
	@ApiOperation(value="仓库管理-分页列表查询", notes="仓库管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StoreVo>> queryPageList( @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String storeName,String storeSn) {
		Result<IPage<StoreVo>> result=new Result<>();
		Page<StoreVo> page=new Page<>(pageNo, pageSize);
		IPage<StoreVo> pageList = toolService.queryStorePageList(page, storeName, storeSn);
		result.setCode(CommonConstant.SC_OK_200);
		result.setResult(pageList);
		result.setSuccess(true);
		return result;
	}
	
	/**
	 * 添加
	 *
	 * @param store
	 * @return
	 */
	@AutoLog(value = "仓库管理-添加")
	@ApiOperation(value="仓库管理-添加", notes="仓库管理-添加")
	@PostMapping(value = "/add")
	public Result<Object> add(@RequestBody StoreParamVo storeParamVo) {
		Boolean exist= toolService.checkSoreNameExist(storeParamVo.getId(),storeParamVo.getStoreName());
		if(exist){
			return Result.error("仓库名称已经存在");
		}
		Store store=new Store();
		store.setStoreName(storeParamVo.getStoreName());
		store.setStoreStatus(storeParamVo.getStoreStatus());
		store.setStorePosition(storeParamVo.getStorePosition());
		store.setDelFlag(DelFlagConstant.NORMAL);
		toolService.addStore(store);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param store
	 * @return
	 */
	@AutoLog(value = "仓库管理-编辑")
	@ApiOperation(value="仓库管理-编辑", notes="仓库管理-编辑")
	@PostMapping(value = "/edit")
	public Result<Object> edit(@RequestBody StoreParamVo storeParamVo) {
		Boolean exist= toolService.checkSoreNameExist(storeParamVo.getId(),storeParamVo.getStoreName());
		if(exist){
			return Result.error("仓库名称已经存在");
		}
		Store store=new Store();
		store.setId(storeParamVo.getId());
		store.setStoreStatus(storeParamVo.getStoreStatus());
		store.setStoreName(storeParamVo.getStoreName());
		store.setStorePosition(storeParamVo.getStorePosition());
		storeService.updateById(store);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "仓库管理-通过id查询")
	@ApiOperation(value="仓库管理-通过id查询", notes="仓库管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Store> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Store> result=new Result<>();
		Store store = storeService.getById(id);
		result.setCode(CommonConstant.SC_OK_200);
		result.setMessage("查询成功");
		result.setSuccess(true);
		result.setResult(store);
		return result;
	}

	
	/**
	 * 描述: 更改仓库起停用状态
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月19日 下午3:31:47
	 * 版本号: V1.0
	 */
	@ApiOperation(value="更改仓库起停用状态", notes="更改仓库起停用状态")
	@GetMapping("/editStatus")
	public Result<Object> editStoreStatus(String id) {
		toolService.editStoreStatus(id);
		return Result.ok("更改成功");
	}
	
	
	/**
	 * 描述: 用户下拉列表-(id-value)
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月19日 下午4:07:31
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({
	 	@ApiImplicitParam(name="name",value="用户名称",paramType="query")
	})
	@ApiOperation(value="用户下拉列表(id-value)", notes="用户下拉列表(id-value)")
	@GetMapping("/userList")
	public Result<List<StoreUserName>> userList(String name) {
		Result<List<StoreUserName>> result=new Result<>();
		List<StoreUserName> userList= toolService.getUserList(name);
		result.setResult(userList);
		return result.success("查询用户成功");
	}
	
	/**
	 * 描述: 用户下拉列表-(code-value)
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月19日 下午4:07:31
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({
	 	@ApiImplicitParam(name="name",value="用户名称",paramType="query")
	})
	@ApiOperation(value="用户下拉列表(code-value)", notes="用户下拉列表(code-value)")
	@GetMapping("/userCodeList")
	public Result<List<UserCodeName>> userCodeList(String name) {
		Result<List<UserCodeName>> result=new Result<>();
		List<UserCodeName> userList= toolService.getUserCodeNameList(name);
		result.setResult(userList);
		return result.success("查询用户成功");
	}
	
	
	/**
	 * 描述:  设置仓库管理员
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月7日 下午3:30:40 
	 * 版本号: V1.0
	 */
	@ApiOperation(value="设置仓库管理员", notes="设置仓库管理员")
	@PostMapping("/setManager")
	public Result<Object> userCodeList(@RequestBody @Valid StoreManagerVo storeManagerVo) {
		//如果传入的仓库为空,就只 删除原有关系,其他情况新增关联关系
		toolService.deleteStoreManager(storeManagerVo.getUserId());
		if(!StringUtils.isEmpty(storeManagerVo.getStoreIds())){
			String[] split = storeManagerVo.getStoreIds().split(",");
			List<OptStoreManager> entityList=new ArrayList<OptStoreManager>();
			for (String string : split) {
				OptStoreManager manager=new OptStoreManager();
				manager.setManage(storeManagerVo.getUserId());
				manager.setStoreId(string);
				entityList.add(manager);
			}		
			storeManager.saveBatch(entityList);
		}
		return Result.ok("设置成功");
	}
	
	/**
	 * 描述:  查询管理员管理的仓库
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月7日 下午3:30:40 
	 * 版本号: V1.0
	 */
	@ApiOperation(value="查询管理员管理的仓库", notes="查询管理员管理的仓库")
	@GetMapping("/getStore")
	public Result<Object> getStoreByUser(String manager) {
		if(StringUtils.isEmpty(manager)){
			return Result.error("请选择用户");
		}
		List<String> storeIdList= toolService.queryStoreIdList(manager);
		return Result.ok(storeIdList);
	}
	
	/**
	 * 描述: 查询树状仓库列表
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月4日 下午3:14:07 
	 * 版本号: V1.0
	 */
	@ApiOperation(value="查询树状仓库列表",notes="查询树状仓库列表")
	@GetMapping("/treeStore")
	public Result<Map<String,Object>> getTreeStore(){
		Result<Map<String,Object>> result = new Result<>();
		//全部用户ids
		List<String> ids = new ArrayList<>();
		List<StoreTree> list = toolService.getTreeStore();
		for(StoreTree userInfo : list) {
			ids.add(userInfo.getTreeId());
		}
		List<TreeModel> treeList = new ArrayList<>();
		getTreeModelList(treeList, list, null);
		Map<String,Object> resMap = new HashMap<String,Object>(2);
		//全部树节点数据
		resMap.put("treeList", treeList); 
		//全部树ids
		resMap.put("ids", ids);
		result.setResult(resMap);
		result.success("successs");
		return result;
	}
	
	
	private void getTreeModelList(List<TreeModel> treeList,List<StoreTree> metaList,TreeModel temp) {
		for (StoreTree storeTree : metaList) {
			String tempPid = storeTree.getPid();
			TreeModel tree = new TreeModel(storeTree.getTreeId(), tempPid, storeTree.getTreeName(),null, storeTree.isLeaf());
			if(temp == null && ConvertUtils.isEmpty(tempPid)){
				treeList.add(tree);
				if(!tree.getIsLeaf()) {
					getTreeModelList(treeList, metaList, tree);
				}
			}else if(temp!=null && tempPid!=null && tempPid.equals(temp.getKey())){
				temp.getChildren().add(tree);
				if(!tree.getIsLeaf()) {
					getTreeModelList(treeList, metaList, tree);
				}
			}
			
		}
	}
	
	
}
