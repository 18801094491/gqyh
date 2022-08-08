package com.zcdy.dsc.modules.collection.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.collection.iot.entity.IotVariableInfo;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.collection.iot.vo.IotVariableInfoVo;
import com.zcdy.dsc.modules.system.entity.SysDictItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 描述: 采集变量信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-03
 * 版本号: V1.0
 */
public interface IotVariableInfoMapper extends BaseMapper<IotVariableInfo> {
    //分页查询
    IPage<IotVariableInfoVo> selectIotVariable(IPage<IotVariableInfoVo> page, @Param("variableName") String variableName,@Param("variableTitle") String variableTitle,@Param("variableType")String variableType);
    //查看启用停用结果集
    List<SysDictItem> selectWorking();
    //导出excel
    List<IotVariableInfoVo> selectExportXls(@Param("variableName") String variableName,@Param("variableTitle")String variableTitle,@Param("variableType")String variableType);

    List<IotVariableInfoVo> getIotVariableInfoVoByName(@Param("id")String id,@Param("variableName") String variableName);

    void editWorkingStatus(@Param("workStatus") String workStatus, @Param("id") String id);
    
    /**
     * 
     * @Description: 
     * @Author:  songguang.jiao
     * @Date:  2020年4月9日 下午8:34:09
     * @Version: V1.0
     */
    @Select({
    	"<script>",
    	" select ",
    	" t.variable_name varName, ",
    	" t.scale scale,",
    	" t.variable_unit united",
    	" FROM",
    	" iot_variable_info t",
    	" WHERE",
    	" t.variable_name in ",
    	" <foreach collection='list' item='item' index='index'  separator=',' open='('  close=')'>",
		" #{item}",
		" </foreach>",
    	"</script>",
    })
	List<VariableInfo> selectVariInfos(@Param("list")List<String> list);
}
