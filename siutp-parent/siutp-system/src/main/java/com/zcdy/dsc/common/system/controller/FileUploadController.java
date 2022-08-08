package com.zcdy.dsc.common.system.controller;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.system.base.controller.AbstractBaseController;
import com.zcdy.dsc.common.system.entity.UploadImgResult;
import com.zcdy.dsc.common.system.entity.UploadResult;
import com.zcdy.dsc.common.system.service.FileUploadService;
import com.zcdy.dsc.modules.operation.equipment.entity.KnowlegeAttach;
import com.zcdy.dsc.modules.operation.equipment.service.IKnowlegeAttachService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author： Roberto
 * 创建时间：2020年1月3日 下午1:23:21
 * 描述: <p>文件上传处理</p>
 */
@RestController
@RequestMapping("/sys/file")
public class FileUploadController extends AbstractBaseController{

	@Resource
	private FileUploadService fileUploadService;
	@Autowired
	private IKnowlegeAttachService knowlegeAttachService;
	
	/**
	 * @author：Roberto
	 * @create:2020年1月3日 下午1:25:57
	 * 描述:<p>文件上传通用处理器</p>
	 */
	@PostMapping("/upload")
	public Result<UploadResult> upload(MultipartFile file) {
		Result<UploadResult> result = new Result<>();
		try {
			UploadResult uploadResult = this.fileUploadService.doUpload(file);
			result.setResult(uploadResult);
			result.success("success");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Result.error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, e.getMessage());
		}
		return result;
	}
	
	/**
	 * @author：Roberto
	 * @create:2020年1月3日 下午1:25:57
	 * 描述:<p>文件上传通用处理器-使用固定路径</p>
	 */
	@PostMapping("/upload/cus/{folder}")
	public Result<UploadResult> uploadWithFolder(MultipartFile file, @PathVariable String folder) {
		Result<UploadResult> result = new Result<>();
		try {
			UploadResult uploadResult = this.fileUploadService.doUpload(file, folder);
			result.setResult(uploadResult);
			result.success("success");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Result.error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 描述: 多附件上传
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月27日 上午11:17:54 
	 * 版本号: V1.0
	 */
	@PostMapping("/upload/more")
	public Result<List<UploadResult>> upload(MultipartFile[] files) {
		Result<List<UploadResult>> result = new Result<>();
		List<UploadResult> results = new ArrayList<UploadResult>();
		try {
			for(MultipartFile file : files){
				UploadResult uploadResult = this.fileUploadService.doUpload(file);
				results.add(uploadResult);
			}
			result.setResult(results);
			result.success("success");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Result.error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, e.getMessage());
		}
		return result;
	}
	//用于知识库功能
	@PostMapping("/upload/moreKnowlege")
	public Result<List<KnowlegeAttach>> upload(MultipartFile[] files,String itemId) {
		Result<List<KnowlegeAttach>> result = new Result<>();
		List<KnowlegeAttach> results = new ArrayList<KnowlegeAttach>();
		try {
			for(MultipartFile file : files){
				//在返回的时候， 同时返回itemId 和id
				UploadResult uploadResult = this.fileUploadService.doUpload(file);
				//保存附件目录
				 KnowlegeAttach knowlegeAttach=new KnowlegeAttach();
				knowlegeAttach.setItemId(itemId);
				knowlegeAttach.setAttachFile(uploadResult.getFilePath());
				knowlegeAttach.setFileName(uploadResult.getFileName());
				knowlegeAttachService.save(knowlegeAttach);
				results.add(knowlegeAttach);
			}
			result.setResult(results);
			result.success("success");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Result.error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 描述: 批量上传图片，返回原图跟缩略图全路径地址,实现预览
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月25日 上午11:45:13 
	 * 版本号: V1.0
	 */
	@PostMapping("/upload/more/pic")
	public Result<List<UploadResult>> uploadPic(MultipartFile[] files) {
		Result<List<UploadResult>> result = new Result<>();
		List<UploadResult> results = new ArrayList<UploadResult>();
		try {
			for(MultipartFile file : files){
				UploadResult uploadResult = this.fileUploadService.doUploadPic(file);
				uploadResult.setFilePath(baseStoragePath+uploadResult.getFilePath());
				uploadResult.setFileThumbPath(baseStoragePath+uploadResult.getFileThumbPath());
				results.add(uploadResult);
			}
			result.setResult(results);
			result.success("success");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Result.error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 描述: Gis地图模型库获取图片宽度高度并上传
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月27日 上午11:29:05 
	 * 版本号: V1.0
	 */
	@ApiOperation(value="获取图片宽度高度并上传",notes="获取图片宽度高度并上传")
	@PostMapping(value="/upload/image",headers="content-type=multipart/form-data")
	public Result<UploadImgResult>  uploadImage(@RequestParam(value = "file",required=true) MultipartFile file) {
		Result<UploadImgResult> result=new Result<>();
		if(file==null){
			return result.error500("图片不能为空");
		}
		UploadImgResult uploadImgResult=new UploadImgResult();
		try {
			BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
			Integer height = bufferedImage.getHeight();
			Integer width = bufferedImage.getWidth();
			//高跟宽的最大最小值
			int minValue=24,maxValue=48;
			if(minValue<24 ||maxValue >48 || minValue<24 ||maxValue >48){
				return result.error500("图片大小最大48X48，最小24X24");
			}
			UploadResult uploadResult = this.fileUploadService.doUpload(file);
			BeanUtils.copyProperties(uploadResult, uploadImgResult);
			uploadImgResult.setFilePath(baseStoragePath+uploadImgResult.getFilePath());
			uploadImgResult.setHeight(height.toString());
			uploadImgResult.setWidth(width.toString());
			result.setResult(uploadImgResult);
			result.success("success");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Result.error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Result.error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 描述: 图片上传不限制大小
	 * @author：  songguang.jiao
	 * 创建时间：  2020年4月7日 下午1:43:44
	 * 版本号: V1.0
	 */
	@ApiOperation(value="监控地图导航栏图片上传",notes="监控地图导航栏图片上传")
	@PostMapping(value="/uploadGisNav",headers="content-type=multipart/form-data")
	public Result<UploadImgResult> uploadGisNav(MultipartFile file){
		Result<UploadImgResult> result=new Result<>();
		if(file == null){
			return result.error500("图片不能为空");
		}
		String fileName = file.getOriginalFilename();
		String fileType = fileName.substring(fileName.indexOf("."));
		if (!(fileType.endsWith(".jpg") || fileType.endsWith(".jpeg") || fileType.endsWith(".png") || fileType.endsWith(".gif"))) {
			return result.error500("请上传 jpg、jpeg、png、gif 格式图片文件");
		}
		UploadImgResult uploadImgResult=new UploadImgResult();
		try {
			UploadResult uploadResult = this.fileUploadService.doUpload(file);
			BeanUtils.copyProperties(uploadResult, uploadImgResult);
			uploadImgResult.setFilePath(baseStoragePath+uploadImgResult.getFilePath());
			result.setResult(uploadImgResult);
			result.success("success");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Result.error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, e.getMessage());
		} 
		return result;
	}
	
	
}
