package com.zcdy.dsc.modules.operation.equipment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.equipment.entity.OptGoodsApply;
import com.zcdy.dsc.modules.operation.equipment.vo.OptGoodsApplyInfoVo;
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
public interface OptGoodsApplyMapper extends BaseMapper<OptGoodsApply> {

	
	/**
	 * 
	 * 描述:  查询所有的申请信息
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月7日 下午11:55:38 
	 * 版本号: V1.0
	 */
    IPage<OptGoodsApplyVo> selectApplyInfo(Page<OptGoodsApplyVo> page,
                                           @Param("createBy") String createBy,
                                           @Param("startTime") String startTime,
                                           @Param("endTime") String endTime,
                                           @Param("verifyStatus") String verifyStatus,
                                           @Param("storeList")  List<String>storeList );


    /**
     * 描述:  查询当前登录人的工具申请历史
     * @author：  songguang.jiao
     * 创建时间： 2020年3月7日 下午10:17:34 
     * 版本号: V1.0
     */
    IPage<OptGoodsMyApplyVo> selectApplyHistory(Page<OptGoodsMyApplyVo> page, @Param("name") String name,
                                                @Param("startTime") String startTime,
                                                @Param("endTime") String endTime,
                                                @Param("verifyStatus") String verifyStatus);

    IPage<OptGoodsMyApplyVo> applyHistoryAll(Page<OptGoodsMyApplyVo> page,
                                             @Param("name") String name,
                                             @Param("startTime") String startTime,
                                             @Param("endTime") String endTime,
                                             @Param("verifyStatus") String verifyStatus);

    /**
     * 描述:  通过id查看申请详情
     * @author：  songguang.jiao
     * 创建时间： 2020年3月12日 下午10:26:13 
     * 版本号: V1.0
     */
    OptGoodsApplyInfoVo queryByApplyId(@Param("applyId")String applyId);


    /**
     * 描述:  通过仓库id查询工具
     * @param toolId
     * @return
     * @author：  songguang.jiao
     * 创建时间： 2020年3月12日 下午9:38:42 
     * 版本号: V1.0
     */
    @Select({
    	" SELECT",
    	" tool_name code,",
    	" (SELECT name FROM sys_category where code=tool_name) as title",
    	" FROM",
    	" opt_tool",
    	" WHERE",
    	" store_id=#{storeId}",
    })
	List<SysCateDropdown> queryToolByStore(@Param("storeId") String storeId);


    /**
     * 描述:  通过工具类型查询所有规格
     * @param code
     * @return
     * @author：  songguang.jiao
     * 创建时间： 2020年3月12日 下午9:53:09 
     * 版本号: V1.0
     */
    @Select({
    	" SELECT",
    	" t.id id,",
    	" (SELECT name from sys_category where code= t.tool_model) toolModel",
    	" FROM",
    	" opt_tool t",
    	" where t.tool_name=#{code} and store_id=#{storeId}",
    })
	List<ToolDropdown> queryModelByCode(@Param("storeId")String storeId,@Param("code") String code);
}
