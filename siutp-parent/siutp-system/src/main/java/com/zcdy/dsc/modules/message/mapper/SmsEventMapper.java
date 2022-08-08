package com.zcdy.dsc.modules.message.mapper;

import com.zcdy.dsc.modules.message.entity.SmsEvent;
import com.zcdy.dsc.modules.message.param.SmsEventPageParam;
import com.zcdy.dsc.modules.message.vo.SmsEventVo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: 事件管理
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-14
 * @Version: V1.0
 */
public interface SmsEventMapper extends BaseMapper<SmsEvent> {

	/**
	 * 描述: 分页查询
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月15日 上午10:41:29
	 * 版本: V1.0
	 */
	@Select({
		" <script>",
		" SELECT",
		" t.id,",
		" t.event_name eventName,",
		" t.event_code eventCode,",
		" t.event_status eventStatus,",
		" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'working_status' ) AND item_value = t.event_status ) eventStatusName, ",
		" t.template_id templateId,",
		" t2.template_name templateName",
		" FROM",
		" sys_sms_event t",
		" LEFT JOIN sys_sms_template_config t2 on t.template_id=t2.id",
		" where t.del_flag='0'",
		" <if test='param.eventName!=null and param.eventName!=\"\"'>",
		"  and t.event_name like concat('%',#{param.eventName},'%')",
		" </if>",
		" <if test='param.eventCode!=null and param.eventCode!=\"\"'>",
		"  and t.event_code like concat('%',#{param.eventCode},'%')",
		" </if>",
		" order by t.event_status desc",
		" </script>",
	})
	IPage<SmsEventVo> selectPageData(Page<SmsEventVo> page,@Param("param")SmsEventPageParam param);

	/**
	 * 描述:导出 
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月15日 下午2:32:19
	 * 版本: V1.0
	 */
	@Select({
		" <script>",
		" SELECT",
		" t.id,",
		" t.event_name eventName,",
		" t.event_code eventCode,",
		" t.event_status eventStatus,",
		" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'working_status' ) AND item_value = t.event_status ) eventStatusName, ",
		" t.template_id templateId,",
		" t2.template_name templateName",
		" FROM",
		" sys_sms_event t",
		" LEFT JOIN sys_sms_template_config t2 on t.template_id=t2.id",
		" where t.del_flag='0'",
		" <if test='param.eventName!=null and param.eventName!=\"\"'>",
		"  and t.event_name like concat('%',#{param.eventName},'%')",
		" </if>",
		" <if test='param.eventCode!=null and param.eventCode!=\"\"'>",
		"  and t.event_code like concat('%',#{param.eventCode},'%')",
		" </if>",
		" order by t.event_status desc",
		" </script>",
	})
	List<SmsEventVo> selectExprotData(@Param("param")SmsEventPageParam param);

}
