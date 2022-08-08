package com.zcdy.dsc.modules.message.service;

import com.zcdy.dsc.modules.message.entity.SmsEvent;
import com.zcdy.dsc.modules.message.param.SmsEventPageParam;
import com.zcdy.dsc.modules.message.vo.SmsEventVo;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 事件管理
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-14
 * @Version: V1.0
 */
public interface SmsEventService extends IService<SmsEvent> {

	/**
	 * 描述: 分页查询
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月15日 上午10:40:14
	 * 版本: V1.0
	 */
	IPage<SmsEventVo> queryPageData(Page<SmsEventVo> page, SmsEventPageParam param);

	/**
	 * 描述: 导出excel
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月15日 下午2:19:39
	 * 版本: V1.0
	 */
	List<SmsEventVo> selectExprotData(SmsEventPageParam param);

}
