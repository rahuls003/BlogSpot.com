package com.niit.controller;

import javax.servlet.annotation.MultipartConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.niit.util.FileUtil;


@RestController
@MultipartConfig(fileSizeThreshold = 20971520)
public class FileUploadController {
	
	private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);
	
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void uploadFile(@RequestParam("fileName") String name, @RequestParam("uploadedFile") MultipartFile multipartFile){
		
		log.debug("Calling the method uploadFile");
		FileUtil.upload(multipartFile, name);
		log.debug("Ending the method uploadFile ");
	}
}
