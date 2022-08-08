package com.zcdy.dsc.common.system.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.zcdy.dsc.common.system.entity.UploadResult;
import com.zcdy.dsc.common.system.service.FileUploadService;

import net.coobird.thumbnailator.Thumbnails;

/**
 * @author： Roberto
 * 创建时间：2020年1月3日 下午1:43:13
 * 描述: <p>本地存储的文件上传实现</p>
 */
@Service
public class LocalStorgeFileUploadServiceImpl implements FileUploadService{

	@Value("${com.zcdy.dsc.file.upload.path}")
	private String baseStoragePath;
	
	@Override
	public UploadResult doUpload(MultipartFile file) throws FileNotFoundException {
		if(null==file){
			throw new FileNotFoundException("无效的文件");
		}
		InputStream inputStream;
		try {
			inputStream = file.getInputStream();
		} catch (IOException e) {
			throw new FileNotFoundException("无效的文件内容");
		}
		String originName = file.getOriginalFilename();
		LocalDate localDate = LocalDate.now();
		String parent = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		File localFolder = new File(baseStoragePath, parent);
		if(!localFolder.exists()){
			localFolder.mkdirs();
		}
		String fileName = UUID.randomUUID().toString().concat(originName.substring(originName.indexOf(".")));
		File localFile = new File(localFolder, fileName);
		OutputStream outputStream = new FileOutputStream(localFile);
		try {
			FileCopyUtils.copy(inputStream, outputStream);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileNotFoundException("文件存储失败……");
		}
		UploadResult result = new UploadResult();
		result.setFileName(originName);
		result.setFilePath("/res/file/"+parent+"/"+fileName);
		return result;
	}

	@Override
	public UploadResult doUploadPic(MultipartFile file) throws FileNotFoundException {
		if(null==file){
			throw new FileNotFoundException("无效的文件");
		}
		InputStream inputStream;
		try {
			inputStream = file.getInputStream();
		} catch (IOException e) {
			throw new FileNotFoundException("无效的文件内容");
		}
		String originName = file.getOriginalFilename();
		LocalDate localDate = LocalDate.now();
		String parent = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		File localFolder = new File(baseStoragePath, parent);
		if(!localFolder.exists()){
			localFolder.mkdirs();
		}
		String fileName = UUID.randomUUID().toString().concat(originName.substring(originName.indexOf(".")));
		File localFile = new File(localFolder, fileName);
		File thumbFile=new File(localFolder,"thumb-"+fileName);
		OutputStream outputStream = new FileOutputStream(localFile);
		try {
			FileCopyUtils.copy(inputStream, outputStream);
			Thumbnails.of(localFile).scale(0.25f).toFile(thumbFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileNotFoundException("文件存储失败……");
		}
		UploadResult result = new UploadResult();
		result.setFileName(originName);
		result.setFilePath("/res/file/"+parent+"/"+fileName);
		result.setFileThumbPath("/res/file/"+parent+"/thumb-"+fileName);
		return result;
	}

	/*
	 * @see com.zcdy.dsc.common.system.service.FileUploadService#doUpload(org.springframework.web.multipart.MultipartFile, java.lang.String)
	 */
	@Override
	public UploadResult doUpload(MultipartFile file, String folder) throws FileNotFoundException {
		if(null==file){
			throw new FileNotFoundException("无效的文件");
		}
		InputStream inputStream;
		try {
			inputStream = file.getInputStream();
		} catch (IOException e) {
			throw new FileNotFoundException("无效的文件内容");
		}
		String originName = file.getOriginalFilename();
		String parent = folder;
		File localFolder = new File(baseStoragePath, parent);
		if(!localFolder.exists()){
			localFolder.mkdirs();
		}
		String fileName = UUID.randomUUID().toString().concat(originName.substring(originName.indexOf(".")));
		File localFile = new File(localFolder, fileName);
		OutputStream outputStream = new FileOutputStream(localFile);
		try {
			FileCopyUtils.copy(inputStream, outputStream);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileNotFoundException("文件存储失败……");
		}
		UploadResult result = new UploadResult();
		result.setFileName(originName);
		result.setFilePath("/res/file/"+parent+"/"+fileName);
		return result;
	}

}
