package com.zcdy.dsc.modules.operation.equipment.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.equipment.entity.OptGoodsApply;
import com.zcdy.dsc.modules.operation.equipment.vo.OptGoodsApplyInfoVo;
import com.zcdy.dsc.modules.operation.equipment.vo.OptGoodsApplyParamVo;
import com.zcdy.dsc.modules.operation.equipment.vo.OptGoodsApplyVo;
import com.zcdy.dsc.modules.operation.equipment.vo.OptGoodsMyApplyVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.ToolDropdown;
import com.zcdy.dsc.modules.system.vo.SysCateDropdown;

/**
 * 描述: 货物申领申请信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-05
 * 版本号: V1.0
 */
public interface OptGoodsApplyService extends IService<OptGoodsApply> {
    //添加申请
    public void saveInfo(OptGoodsApplyParamVo optGoodsApply);

    //查询所有的申请信息
    IPage<OptGoodsApplyVo> getApplyInfo(Page<OptGoodsApplyVo> page, String createBy,String startTime ,String endTime, String verifyStatus);

    //查看自己，当前用户的申请信息
    IPage<OptGoodsMyApplyVo> getApplyHistory(Page<OptGoodsMyApplyVo> page,String startTime ,String endTime, String verifyStatus);

    //修改申请
    public void updateByOptId(OptGoodsApplyParamVo optGoodsApply);

    //查所有的申请记录
    IPage<OptGoodsMyApplyVo> applyHistoryAll(Page<OptGoodsMyApplyVo> page, String name,String startTime ,String endTime, String verifyStatus);


    /**
     * 
     * 描述:  通过id查看申请详情
     * @param applyId
     * @author：  songguang.jiao
     * 创建时间： 2020年3月12日 下午10:25:23 
     * 版本号: V1.0
     */
    OptGoodsApplyInfoVo queryByApplyId(String applyId);

    /**
     * 描述:  通过仓库查询工具列表
     * @author：  songguang.jiao
     * 创建时间： 2020年3月12日 下午9:37:14 
     * 版本号: V1.0
     */
	public List<SysCateDropdown> queryToolByStore(String storeId);

	/**
	 * 描述:  通过工具类型查询所有规格
	 * @param code 
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月12日 下午9:51:58 
	 * 版本号: V1.0
	 */
	public List<ToolDropdown> queryModelByCode(String storeId,String code);
}
