package com.zcdy.dsc.modules.collection.gis.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.common.util.UUIDGenerator;
import com.zcdy.dsc.constant.ShowConstant;
import com.zcdy.dsc.modules.collection.gis.entity.GisModel;
import com.zcdy.dsc.modules.collection.gis.entity.GisModelNav;
import com.zcdy.dsc.modules.collection.gis.entity.GisModelRes;
import com.zcdy.dsc.modules.collection.gis.entity.GisModelResItem;
import com.zcdy.dsc.modules.collection.gis.mapper.GisDao;
import com.zcdy.dsc.modules.collection.gis.mapper.GisModelResMapper;
import com.zcdy.dsc.modules.collection.gis.service.GisService;
import com.zcdy.dsc.modules.collection.gis.vo.*;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : songguang.jiao
 */
@Service
public class GisServiceImpl implements GisService {

	@Resource
	private GisModelResMapper gisModelResMapper;

	@Resource
	private GisDao gisdao;

	@Override
	@Transactional(rollbackFor=Exception.class)
	public Result<Object> edit(GisModelResParamVo paramVo) {
		//删除原有图片,重新插入
		gisdao.deleteGisModelItem(paramVo.getId());
		List<GisModelResItem> items = this.insertGisModelResItem(paramVo, null);
		if (items.size() > 0 && !items.isEmpty()) {
			gisdao.insertGisModelItem(items);
		}
		return Result.ok("保存成功");
		
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public Result<Object> save(GisModelResParamVo paramVo) {
		GisModelRes gisModelRes = new GisModelRes();
		gisModelRes.setModelTypeCode(paramVo.getModelTypeCode());
		//默认不展示
		gisModelRes.setLegendShow(ShowConstant.NOT_SHOW);
		gisModelRes.setDelFlag(DelFlagConstant.NORMAL);
		gisModelResMapper.insert(gisModelRes);

		List<GisModelResItem> items = this.insertGisModelResItem(paramVo, gisModelRes);
		if (items.size() > 0 && !items.isEmpty()) {
			gisdao.insertGisModelItem(items);
		}
		return Result.ok("保存成功");
	}

	// 循环批量插入图片
	private List<GisModelResItem> insertGisModelResItem(GisModelResParamVo paramVo, GisModelRes gisModelRes) {
		List<GisModelResImgVo> list = paramVo.getList();
		List<GisModelResItem> imgsList = new ArrayList<GisModelResItem>();
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		Date date = new Date();
		if (list.size() > 0 && !list.isEmpty()) {
			for (GisModelResImgVo imgVo : list) {
				GisModelResItem item = new GisModelResItem();
				item.setCreateBy(loginUser.getUsername());
				item.setCreateTime(date);
				item.setDelFlag(DelFlagConstant.NORMAL);
				item.setHeight(imgVo.getHeight());
				item.setId(UUIDGenerator.generate());
				item.setImageType(imgVo.getImageType());
				item.setImgUrl(imgVo.getImgUrl());
				if(gisModelRes!=null){
					item.setResId(gisModelRes.getId());
				}else{
					item.setResId(paramVo.getId());
				}
				item.setWidth(imgVo.getWidth());
				imgsList.add(item);
			}
		}
		return imgsList;
	}

	@Override
	public IPage<GisModelResVo> queryGisResPageData(Page<GisModelResVo> page, String modelTypeCode) {
		return gisdao.queryGisResData(page,modelTypeCode);
	}

	@Override
	public List<ResSnVo> queryResSn(String modelType) {
		return gisdao.queryResSn(modelType);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void editImage(GisImageVo gisImageVo) {
		//获取图片类型地址
		List<GisModelResImgVo> resItem = gisdao.queryResItem(gisImageVo.getResId());
		String modelImg="";
		String modelWaringImg="";
		String modelOnImg="";
		String modelOffImg="";
		for (GisModelResImgVo gisModelResImgVo : resItem) {
			if(gisModelResImgVo.getImageType().endsWith("0")){
				modelImg=gisModelResImgVo.getImgUrl();
			}
			if(gisModelResImgVo.getImageType().endsWith("1")){
				modelWaringImg=gisModelResImgVo.getImgUrl();
			}
			if(gisModelResImgVo.getImageType().endsWith("2")){
				modelOnImg=gisModelResImgVo.getImgUrl();
			}
			if(gisModelResImgVo.getImageType().endsWith("3")){
				modelOffImg=gisModelResImgVo.getImgUrl();
			}
		}
		//判断类型,通过ids更新图片地址
		if("1".endsWith(gisImageVo.getType())){
			String[] split = gisImageVo.getIds().split(",");
			List<GisModel> gisModels=new ArrayList<>();
			for (int i = 0; i < split.length; i++) {
				GisModel model=new GisModel();
				model.setId(split[i]);
				model.setModelImg(modelImg);
				model.setModelWaringImg(modelWaringImg);
				model.setModelOnImg(modelOnImg);
				model.setModelOffImg(modelOffImg);
				model.setHeight(gisImageVo.getHeight());
				model.setWidth(gisImageVo.getWidth());
				gisModels.add(model);
			}
			if(gisModels.size()>0 && !gisModels.isEmpty()){
				gisdao.updateGisModelList(gisModels);
			}
		}else{
			//更新一类图片地址
			GisModel model=new GisModel();
			model.setModelImg(modelImg);
			model.setModelWaringImg(modelWaringImg);
			model.setModelOnImg(modelOnImg);
			model.setModelOffImg(modelOffImg);
			model.setWidth(gisImageVo.getWidth());
			model.setHeight(gisImageVo.getHeight());
			gisdao.updateGisModelOne(model,gisImageVo.getResId());
			
		}
		//将模型库图片置为图例已展示
		GisModelRes gisModelRes=new GisModelRes();
		gisModelRes.setLegendShow(ShowConstant.SHOW);
		UpdateWrapper<GisModelRes> updateWrapper=new UpdateWrapper<>();
		updateWrapper.lambda().eq(GisModelRes::getId, gisImageVo.getResId());
		gisModelResMapper.update(gisModelRes, updateWrapper);
		
	}

	@Override
	public List<GisModelNav> queryAllGisNav() {
		return gisdao.queryAllGisNav();
	}

}
