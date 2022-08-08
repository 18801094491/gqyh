package com.zcdy.dsc.modules.configcentre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.configcentre.entity.Docs;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 描述: 文档管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-04-08
 * 版本号: V1.0
 */
public interface DocsMapper extends BaseMapper<Docs> {

	/**
	 * @author：Roberto
	 * @create:2020年4月8日 下午4:46:40
	 * 描述:<p>根据文档名称模糊查询文档信息</p>
	 */
	@Select({
		"<script>",
		"SELECT t.id, t.status status,t.doc_name docName, t.doc_description docDescription, t.doc_type docType, b.`name` docTypeTitle, t.create_time createTime,t.display_order displayOrder from sys_docs t ",
		"	LEFT JOIN sys_category b on t.doc_type  = b.`code`",
		"where 1=1",
		"<if test='docName!=null and docName!=\"\"'>",
		"and t.doc_name like concat('%',#{docName},'%')",
		"</if>",
		"ORDER BY t.display_order DESC",
		"</script>",
	})
	IPage<Docs> selectPageData(Page<Docs> page, @Param("docName") String docName);

	/**
	 * @author：Roberto
	 * @create:2020年4月8日 下午5:57:16
	 * 描述:<p>根据文档分类获取所有文档信息</p>
	 */
	@Select({
		"<script>",
		"SELECT t.id, t.status status,t.doc_name docName, t.doc_description docDescription,t.doc_url docUrl, b.`name` docType, t.create_time createTime,t.display_order displayOrder from sys_docs t ",
		"	LEFT JOIN sys_category b on t.doc_type  = b.`code`",
		"where 1=1",
		" and t.status="+StatusConstant.RUN,
		"<if test='code!=null and code!=\"\"'>",
		"and b.`code`=#{code}",
		"</if>",
		"ORDER BY t.display_order DESC",
		"</script>",
	})
	List<Docs> selectData(@Param("code") String code);

	@Update("update sys_docs set status=#{status} where id=#{id}")
    void startOrStop(String id, Boolean status);
}
