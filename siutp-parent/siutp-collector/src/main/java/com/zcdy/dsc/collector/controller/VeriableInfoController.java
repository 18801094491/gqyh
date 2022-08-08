package com.zcdy.dsc.collector.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ioserver.bean.Struct_TagProperty;
import com.ioserver.dll.IOServerAPICilent;
import com.sun.jna.WString;
import com.zcdy.dsc.collector.entity.IOServerTag;
import com.zcdy.dsc.collector.ioserver.IOServerClientBean;
import com.zcdy.dsc.collector.util.Result;

/**
 * @author: Roberto
 * @CreateTime:2020年4月7日 上午9:46:36
 * @Description: <p></p>
 */
@RequestMapping("iot/var")
@RestController
public class VeriableInfoController {

	@Resource
	private IOServerClientBean ioserverClientBean;
	
	/**
	 * @author:Roberto
	 * @create:2020年4月7日 上午9:51:03
	 * @Description:<p>同步变量</p>
	 */
	@GetMapping("vars")
	public Result<List<IOServerTag>> getAllVarsInfo(){
		Result<List<IOServerTag>> result = null;
		IOServerAPICilent client = ioserverClientBean.getIoServer();
		if(null==client){
			result = Result.fail("获取IOServer连接失败");
		}else{
			//获取所有的IOServer变量信息
			Struct_TagProperty[] tags = client.BrowserCollectTags(client.getHandle(), new WString(""));
			List<IOServerTag> vars = new ArrayList<IOServerTag>();
			for(Struct_TagProperty tag : tags){
				IOServerTag var = new IOServerTag();
				var.setTagName(tag.TagName.toString());
				var.setDataType(tag.TagDataType);
				var.setDescription(tag.Description.toString());
				vars.add(var);
			}
			result = Result.success(vars, "success");
		}
		return result;
	}
}
