package com.zcdy.dsc.modules.configcentre.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.configcentre.entity.Docs;

/**
 * 描述: 文档管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-04-08
 * 版本号: V1.0
 */
public interface DocsService extends IService<Docs> {

	/**
	 * @author：Roberto
	 * @create:2020年4月8日 下午4:45:12
	 * 描述:<p></p>
	 */
	IPage<Docs> pageData(Page<Docs> page, String docName);

	/**
	 * @author：Roberto
	 * @create:2020年4月8日 下午5:56:47
	 * 描述:<p></p>
	 */
	List<Docs> getDocData(String code);

    void startOrStop(String id, Boolean status);
}
