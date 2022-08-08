package com.zcdy.dsc.common.system.service;

import java.io.FileNotFoundException;

import org.springframework.web.multipart.MultipartFile;

import com.zcdy.dsc.common.system.entity.UploadResult;

/**
 * @author： Roberto
 * 创建时间：2020年1月3日 下午1:32:46
 * 描述: <p></p>
 */
public interface FileUploadService {

	/**
	 * 描述: 通用文件上传
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月27日 上午11:25:16 
	 * 版本号: V1.0
	 */
	public UploadResult doUpload(MultipartFile file) throws FileNotFoundException;
	
	/**
	 * 描述: 图片上传需要同时生成缩略图
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月25日 下午5:58:39 
	 * 版本号: V1.0
	 */
	public UploadResult doUploadPic(MultipartFile file) throws FileNotFoundException;

	/**
	 * @author：Roberto
	 * @throws FileNotFoundException 
	 * @create:2020年4月8日 下午5:02:07
	 * 描述:<p></p>
	 */
	public UploadResult doUpload(MultipartFile file, String folder) throws FileNotFoundException;
	
}
