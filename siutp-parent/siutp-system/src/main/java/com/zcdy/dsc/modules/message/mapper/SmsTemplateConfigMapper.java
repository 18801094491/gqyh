package com.zcdy.dsc.modules.message.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.constant.WorkingStatus;
import com.zcdy.dsc.modules.message.entity.SmsTemplateConfig;
import com.zcdy.dsc.modules.message.param.SmsTemplateConfigPageParam;
import com.zcdy.dsc.modules.message.vo.SmsTemplateConfigVo;
import com.zcdy.dsc.modules.message.vo.SmsTemplateDropdown;

/**
 * @Description: 短信模板配置
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-13
 * @Version: V1.0
 */
public interface SmsTemplateConfigMapper extends BaseMapper<SmsTemplateConfig> {

	/**
	 * 描述: 分页查询
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月13日 上午10:22:22
	 * 版本: V1.0
	 */
	@Select({
		" <script>",
		"SELECT ",
		" t.id,",
		" t.module_id moduleId,",
		" t.template_name templateName,",
		" t.template_code templateCode,",
		" t.sign_name signName,",
		" ( SELECT GROUP_CONCAT( user_id ) FROM sms_users WHERE t.module_id = module_id) usersId,",
		" (SELECT GROUP_CONCAT(t3.realname) from sms_users t2 LEFT JOIN sys_user t3 on t2.user_id=t3.id where t.module_id = module_id) usersName,",
		" t.template_status templateStatus,",
		" (SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'working_status' ) AND item_value = t.template_status ) templateStatusName,",
		" t.template_content templateContent",
		" FROM",
		" sys_sms_template_config t",
		" where t.del_flag=0",
		" <if test='param.templateName!=null and param.templateName!=\"\"'>",
		"  and t.template_name like concat('%',#{param.templateName},'%')",
		" </if>",
		" <if test='param.templateCode!=null and param.templateCode!=\"\"'>",
		"  and t.template_code like concat('%',#{param.templateCode},'%')",
		" </if>",
		" <if test='param.templateConfig!=null and param.templateConfig!=\"\"'>",
		"  and t.template_status =#{param.templateConfig}",
		" </if>",
		" order by t.template_status desc",
		" </script>",
	})
	IPage<SmsTemplateConfigVo> selectPageData(Page<SmsTemplateConfigVo> page,@Param("param") SmsTemplateConfigPageParam param);

	/**
	 * 描述: 导出
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月13日 下午3:50:06
	 * 版本: V1.0
	 */
	@Select({
		" <script>",
		"SELECT ",
		" t.id,",
		" t.module_id moduleId,",
		" t.template_name templateName,",
		" t.template_code templateCode,",
		" t.sign_name signName,",
		" ( SELECT GROUP_CONCAT( user_id ) FROM sms_users WHERE t.module_id = module_id) usersId,",
		" (SELECT GROUP_CONCAT(t3.realname) from sms_users t2 LEFT JOIN sys_user t3 on t2.user_id=t3.id where t.module_id = module_id) usersName,",
		" t.template_status templateStatus,",
		" (SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'working_status' ) AND item_value = t.template_status ) templateStatusName,",
		" t.template_content templateContent",
		" FROM",
		" sys_sms_template_config t",
		" where t.del_flag=0",
		" <if test='param.templateName!=null and param.templateName!=\"\"'>",
		"  and t.template_name like concat('%',#{param.templateName},'%')",
		" </if>",
		" <if test='param.templateCode!=null and param.templateCode!=\"\"'>",
		"  and t.template_code like concat('%',#{param.templateCode},'%')",
		" </if>",
		" <if test='param.templateConfig!=null and param.templateConfig!=\"\"'>",
		"  and t.template_status =#{param.templateConfig}",
		" </if>",
		" order by t.template_status desc",
		" </script>",
	})
	List<SmsTemplateConfigVo> export(@Param("param") SmsTemplateConfigPageParam param);

	/**
	 * 描述: 下拉选
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月14日 上午10:03:28
	 * 版本: V1.0
	 */
	@Select({
		" <script>",
		" SELECT t.id,t.template_name templateName from sys_sms_template_config t where t.del_flag='0' and t.template_status="+WorkingStatus.WORKING,
		" <if test='templateName!=null and templateName!=\"\"'>",
		" and t.template_name like concat('%',#{templateName},'%')",
		" </if>",
		" order by t.create_time",
		" </script>",
	})
	List<SmsTemplateDropdown> queryDropdown(@Param("templateName") String templateName);

}
