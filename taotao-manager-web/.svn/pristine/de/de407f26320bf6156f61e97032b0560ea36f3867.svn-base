package com.taotao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.common.json.JSONObject;
import com.taotao.common.utils.JsonUtils;
import com.taotao.utils.FastDFSClient;

@Controller
public class PictureController {
	
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL ;
	
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
	@ResponseBody
	public String fileUpload(MultipartFile uploadFile) {
		try {
			//取文件扩展名
			String fileName = uploadFile.getOriginalFilename() ;
			String extName = fileName.substring(fileName.lastIndexOf(".") + 1) ;
			//创建一个fastDFS对象
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:resource/fast_dfs.conf") ;
			String path = fastDFSClient.uploadFile(uploadFile.getBytes(), extName) ;
			//拼接完整的URL路径
			String url = IMAGE_SERVER_URL + path ;
			//返回数据
			Map<String, Object> result = new HashMap<>() ;
			result.put("error", 0) ;
			result.put("url", url) ;
			return JsonUtils.objectToJson(result) ;
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> result = new HashMap<>() ;
			result.put("error", 1) ;
			result.put("message", "图片上传失败") ;
			return JsonUtils.objectToJson(result) ;
		}
		
	}
}
