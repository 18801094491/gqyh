package com.zcdy.dsc.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.system.entity.SysAnnouncementSend;
import com.zcdy.dsc.modules.system.model.AnnouncementSendModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述: 用户通告阅读标记表
 @author : songguang.jiao
 
 * 版本号: V1.0
 */
public interface SysAnnouncementSendMapper extends BaseMapper<SysAnnouncementSend> {

	public List<String> queryByUserId(@Param("userId") String userId);

	/**
	 * @功能：获取我的消息
	 * @param announcementSendModel
	 * @param pageSize 
	 * @param pageNo 
	 * @return
	 */
	public List<AnnouncementSendModel> getMyAnnouncementSendList(Page<AnnouncementSendModel> page,@Param("announcementSendModel") AnnouncementSendModel announcementSendModel);

}
