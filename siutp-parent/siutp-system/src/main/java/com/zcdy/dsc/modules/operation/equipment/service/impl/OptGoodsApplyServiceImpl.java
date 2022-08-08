package com.zcdy.dsc.modules.operation.equipment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.system.api.ISysBaseApi;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.modules.operation.equipment.constant.DealStatusConstant;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.constant.GoodsApplyStatus;
import com.zcdy.dsc.modules.operation.equipment.constant.GoodsItemType;
import com.zcdy.dsc.modules.operation.equipment.entity.OptGoodsApply;
import com.zcdy.dsc.modules.operation.equipment.entity.OptGoodsApplyItem;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptGoodsApplyItemMapper;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptGoodsApplyMapper;
import com.zcdy.dsc.modules.operation.equipment.mapper.ToolDao;
import com.zcdy.dsc.modules.operation.equipment.service.OptGoodsApplyService;
import com.zcdy.dsc.modules.operation.equipment.vo.*;
import com.zcdy.dsc.modules.operation.equipment.vo.store.ToolDropdown;
import com.zcdy.dsc.modules.system.vo.SysCateDropdown;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * 描述: 货物申领申请信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-05
 * 版本号: V1.0
 */
@Service
public class OptGoodsApplyServiceImpl extends ServiceImpl<OptGoodsApplyMapper, OptGoodsApply> implements OptGoodsApplyService {

    @Autowired
    private OptGoodsApplyItemMapper optItemMapper;
    @Autowired
    private  OptGoodsApplyMapper optGoodsApplyMapper;
    @Autowired
    private ISysBaseApi sysBaseApi;
    @Autowired
    private ToolDao toolDao; 

    //添加申请
    @Override
    @Transactional(rollbackFor=Exception.class)
    public void saveInfo(OptGoodsApplyParamVo optGoodsApply) {
        //申请 运维工具 先存主表,再存子表
        OptGoodsApply goodsApply = new OptGoodsApply();
        BeanUtils.copyProperties(optGoodsApply,goodsApply);
        goodsApply.setVerifyStatus(GoodsApplyStatus.AUDIT);
        goodsApply.setDataType(GoodsItemType.TOOLS);
        goodsApply.setDealStatus(DealStatusConstant.UNDEAL);
        goodsApply.setDelFlag(DelFlagConstant.NORMAL);
        goodsApply.setApplySn(DateUtil.getTimeStamp());
        this.save(goodsApply);
        
        //获取所有申请工具 存入子表
        List<OptApplyItemVo> applyItem = optGoodsApply.getApplyItem();
        List<OptGoodsApplyItem> listInfo =new LinkedList<OptGoodsApplyItem>();
        for (OptApplyItemVo optApplyItemVo:applyItem) {
            OptGoodsApplyItem item = new OptGoodsApplyItem();
            item.setDataId(optApplyItemVo.getDataId());
            item.setStoreId(optApplyItemVo.getStoreId());
            item.setAmount(Integer.valueOf(optApplyItemVo.getAmount()));
            item.setGoodsApplyid(goodsApply.getId());
            listInfo.add(item);
            //通过获取到仓库id,找到对应的仓库管理员
            String userIds = toolDao.queryManagerIdList(optApplyItemVo.getStoreId());
            if(!StringUtils.isEmpty(userIds)){
            	sysBaseApi.sendSysAnnouncement("admin", userIds, "申请工具", "有新提交的申请工单，请查看");
            }
        }
        optItemMapper.insertInfo(listInfo);
    }

    //查询所有的申请信息
    @Override
    public IPage<OptGoodsApplyVo> getApplyInfo(Page<OptGoodsApplyVo> page, String createBy,String startTime ,String endTime, String verifyStatus) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //查询当前用户所有的仓库
        List<String> storeIdList = toolDao.queryStoreIdList(sysUser.getId());
        if(storeIdList.size()==0 ||storeIdList.isEmpty()){
            //当前用户没有管理仓库的权限，不查询
            return null;
        }
        //查看 货物申领申请信息 表  目前还缺少一个 借用工具字段 正好用来区别是哪个仓库的
        //查 货物申请项详情 opt_goods_apply_item   根据仓库id,查询出当前仓库有多少条申请记录
        //然后让货物申领申请信息 表的id 和 opt_goods_apply_item的申请id对应上
       return optGoodsApplyMapper.selectApplyInfo(page, createBy,startTime,endTime, verifyStatus,storeIdList);
    }

    //查看自己，当前用户的申请信息
    @Override
    public IPage<OptGoodsMyApplyVo> getApplyHistory(Page<OptGoodsMyApplyVo> page,String startTime,String endTime,String verifyStatus) {
        //获取到当前登录者用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return optGoodsApplyMapper.selectApplyHistory(page, sysUser.getUsername(), startTime, endTime, verifyStatus);
    }

    //修改申请
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void updateByOptId(OptGoodsApplyParamVo optGoodsApply) {
		OptGoodsApply goodsApply = new OptGoodsApply();
		BeanUtils.copyProperties(optGoodsApply, goodsApply);
		goodsApply.setVerifyStatus(GoodsApplyStatus.AUDIT);
		this.updateById(goodsApply);
		// 修改申请项具体内容,获取所有申请工具
		List<OptApplyItemVo> applyItem = optGoodsApply.getApplyItem();
		// 删除掉原先的数据
		optItemMapper.deleteByApplyId(optGoodsApply.getId());
		List<OptGoodsApplyItem> listInfo = new LinkedList<OptGoodsApplyItem>();
		for (OptApplyItemVo optApplyItemVo : applyItem) {
			OptGoodsApplyItem item = new OptGoodsApplyItem();
			item.setDataId(optApplyItemVo.getDataId());
			item.setStoreId(optApplyItemVo.getStoreId());
			item.setAmount(Integer.valueOf(optApplyItemVo.getAmount()));
			item.setGoodsApplyid(goodsApply.getId());
			listInfo.add(item);
			// 通过获取到仓库id,找到对应的仓库管理员
			String userIds = toolDao.queryManagerIdList(optApplyItemVo.getStoreId());
			if (!StringUtils.isEmpty(userIds)) {
				sysBaseApi.sendSysAnnouncement("admin", userIds, "申请工具", "有新提交的申请工单，请查看");
			}

		}
		optItemMapper.insertInfo(listInfo);

	}

    //查所有的申请记录
    @Override
    public IPage<OptGoodsMyApplyVo> applyHistoryAll(Page<OptGoodsMyApplyVo> page, String name,String startTime ,String endTime, String verifyStatus) {
        return optGoodsApplyMapper.applyHistoryAll(page, name,startTime,endTime,verifyStatus);
    }

    @Override
    public OptGoodsApplyInfoVo queryByApplyId(String applyId) {
        return  optGoodsApplyMapper.queryByApplyId(applyId);
    }

	@Override
	public List<SysCateDropdown> queryToolByStore(String storeId) {
		return optGoodsApplyMapper.queryToolByStore(storeId);
	}

	@Override
	public List<ToolDropdown> queryModelByCode(String storeId,String code) {
		return optGoodsApplyMapper.queryModelByCode(storeId,code);
	}
}
