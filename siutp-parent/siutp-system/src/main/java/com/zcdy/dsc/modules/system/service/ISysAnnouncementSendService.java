package com.zcdy.dsc.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.system.entity.SysAnnouncementSend;
import com.zcdy.dsc.modules.system.model.AnnouncementSendModel;

import java.util.List;

/**
 * 描述: 用户通告阅读标记表
 * 版本号: V1.0
 * @author : songguang.jiao
 */
public interface ISysAnnouncementSendService extends IService<SysAnnouncementSend> {

	public List<String> queryByUserId(String userId);
	
	/**
	 * 功能：获取我的消息
	 * @param announcementSendModel 
	 * @return
	 */
	public Page<AnnouncementSendModel> getMyAnnouncementSendPage(Page<AnnouncementSendModel> page,AnnouncementSendModel announcementSendModel);

}
