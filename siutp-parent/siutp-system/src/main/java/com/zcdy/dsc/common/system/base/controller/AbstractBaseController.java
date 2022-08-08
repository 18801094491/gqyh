package com.zcdy.dsc.common.system.base.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.zcdy.dsc.common.framework.spring.propertyeditor.CustomDateEditor;
import com.zcdy.dsc.common.framework.spring.propertyeditor.CustomLocalDateEditor;
import com.zcdy.dsc.common.framework.spring.propertyeditor.CustomLocalDateTimeEditor;

/**
 * @author: Roberto
 * 创建时间:2020年4月26日 上午10:31:15
 * 描述: <p>定义基类</p>
 */
public abstract class AbstractBaseController {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 默认分页，页容量
	 */
	protected final int DEFAULT_PAGESIZE = 10;
	
	/**
     * 项目访问基础路径
     */
    @Value("${com.zcdy.dsc.file.request.domain}")
    protected String baseStoragePath;
	
	@InitBinder
    protected final void initBinder(WebDataBinder binder) {
		
		binder.registerCustomEditor(Date.class, new CustomDateEditor(true));
		binder.registerCustomEditor(LocalDate.class, new CustomLocalDateEditor(DateTimeFormatter.ofPattern("yyyy-MM-dd"), true));
		binder.registerCustomEditor(LocalDateTime.class, new CustomLocalDateTimeEditor(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"), true));
    }
}
