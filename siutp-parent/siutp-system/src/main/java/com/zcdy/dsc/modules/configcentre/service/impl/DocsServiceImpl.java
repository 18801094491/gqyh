package com.zcdy.dsc.modules.configcentre.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.configcentre.entity.Docs;
import com.zcdy.dsc.modules.configcentre.mapper.DocsMapper;
import com.zcdy.dsc.modules.configcentre.service.DocsService;

/**
 * 描述: 文档管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-04-08
 * 版本号: V1.0
 */
@Service
public class DocsServiceImpl extends ServiceImpl<DocsMapper, Docs> implements DocsService {

	@Resource
	private DocsMapper docsMapper;
	
	/*
	 * @see com.zcdy.dsc.modules.configcentre.service.DocsService#pageData(com.baomidou.mybatisplus.extension.plugins.pagination.Page, java.lang.String)
	 */
	@Override
	public IPage<Docs> pageData(Page<Docs> page, String docName) {
		return docsMapper.selectPageData(page, docName);
	}

	/*
	 * @see com.zcdy.dsc.modules.configcentre.service.DocsService#getDocData(java.lang.String)
	 */
	@Override
	public List<Docs> getDocData(String code) {
		return docsMapper.selectData(code);
	}

	@Override
	public void startOrStop(String id, Boolean status) {
		docsMapper.startOrStop(id,status);
	}
}
