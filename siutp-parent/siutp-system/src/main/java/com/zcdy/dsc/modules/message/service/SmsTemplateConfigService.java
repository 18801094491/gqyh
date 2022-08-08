package com.zcdy.dsc.modules.message.service;

import com.zcdy.dsc.modules.message.entity.SmsTemplateConfig;
import com.zcdy.dsc.modules.message.param.SmsTemplateConfigPageParam;
import com.zcdy.dsc.modules.message.param.SmsTemplateConfigParam;
import com.zcdy.dsc.modules.message.vo.SmsTemplateConfigVo;
import com.zcdy.dsc.modules.message.vo.SmsTemplateDropdown;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 短信模板配置
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-13
 * @Version: V1.0
 */
public interface SmsTemplateConfigService extends IService<SmsTemplateConfig> {

	/**
	 * 描述: 分页查询列表
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月13日 上午10:19:07
	 * 版本: V1.0
	 */
	IPage<SmsTemplateConfigVo> queryPageData(Page<SmsTemplateConfigVo> page,SmsTemplateConfigPageParam param);

	/**
	 * 描述: 更改启停用状态
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月13日 上午11:20:59
	 * 版本: V1.0
	 */
	void editStatus(String id);

	/**
	 * 描述: 新增修改管理
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月13日 下午2:33:45
	 * 版本: V1.0
	 */
	void one(SmsTemplateConfigParam smsTemplateConfig);

	/**
	 * 
	 * 描述: 导出
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月13日 下午3:45:01
	 * 版本: V1.0
	 */
	List<SmsTemplateConfigVo> export(SmsTemplateConfigPageParam param);

	/**
	 * 描述: 下拉选
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月14日 上午10:02:45
	 * 版本: V1.0
	 */
	List<SmsTemplateDropdown> queryDropdown(String templateName);

}
