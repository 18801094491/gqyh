package com.zcdy.dsc.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.message.entity.SysMessageTemplate;

import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 描述: 消息模板
 @author : songguang.jiao
 
 * 版本号: V1.0
 */
public interface SysMessageTemplateMapper extends BaseMapper<SysMessageTemplate> {

    /**
     * 通过code查询模板列表
     * @param code code值
     * @return
     */
    @Select("SELECT * FROM SYS_SMS_TEMPLATE WHERE TEMPLATE_CODE = #{code} limit 1")
    List<SysMessageTemplate> selectByCode(String code);
}
