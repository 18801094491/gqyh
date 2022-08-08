package com.zcdy.dsc.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.message.entity.SysMessage;
import com.zcdy.dsc.modules.message.vo.SendMessageHelperVo;
import org.apache.ibatis.annotations.Select;
import java.util.ArrayList;
import java.util.List;

/**
 * 2 * @Author: 王海东
 * 3 * @Date: 2020/4/13 13:57
 * 4
 */
public interface SendMessageHelperMapper extends BaseMapper<SysMessage> {

    @Select("select id as id,template_code as templateCode,sign_name as signName,template_content as templateContent,module_id as moduleId from sys_sms_template_config where id=#{id} and template_status='1'")
    List<SendMessageHelperVo> querySendMessageHelperVoById(String id);
    @Select("select sys.phone  from  sms_users sms left join sys_user sys on sms.user_id=sys.id where sms.module_id=#{moduleId}")
    ArrayList<String> queryPhonesByMoudleId(String moduleId);
}
