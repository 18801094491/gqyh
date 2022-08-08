package com.zcdy.dsc.modules.configcentre.mapper;

import com.zcdy.dsc.modules.configcentre.entity.SystemConfig;
import com.zcdy.dsc.modules.configcentre.vo.SystemConfigVo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: 系统参数配置管理
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-10
 * @Version: V1.0
 */
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {

	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param configName 配置名称
	 * @param configStatus 配置状态
	 * @return
	 */
	@Select({
		" <script>",
		" SELECT",
		" t.id,",
		" t.config_description configDescription,",
		" t.config_key configKey,",
		" t.config_name configName,",
		" t.config_status configStatus,",
		" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'working_status' ) AND item_value = t.config_status ) configStatusName,",
		" t.config_value configValue,",
		" t.access_values accessValues",
		" FROM",
		" system_config t where 1=1",
		" <if test='configName!=null and configName!=\"\"'>",
		" and t.config_name like concat('%',#{configName},'%')",
		" </if>",
		" <if test='configStatus!=null and configStatus!=\"\"'>",
		" and t.config_status=#{configStatus}",
		" </if>",
		" order by t.config_value desc",
		" </script>",
	})
	IPage<SystemConfigVo> queryPageData(Page<SystemConfigVo> page,@Param("configName") String configName,@Param("configStatus") String configStatus);

}
