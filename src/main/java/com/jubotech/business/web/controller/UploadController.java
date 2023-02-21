package com.jubotech.business.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jubotech.framework.domain.base.BaseResp;
import com.jubotech.framework.util.ActionResult;
import com.jubotech.framework.util.FileUtil;
import com.jubotech.framework.util.PropertyUtils;

import lombok.extern.slf4j.Slf4j;
 

/**
 * @author admin 公共上传方法
 */

@Controller
@Slf4j
public class UploadController {
	
	@Autowired
	private Environment env;

	/**
	 * 后台管理 上传文件通用接口
	 */
	@ResponseBody
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public BaseResp fileUpload(@RequestParam("myfile") MultipartFile file,@RequestParam(name = "isMp3ToAmr" ,defaultValue="false") String isMp3ToAmr ,@RequestParam(name = "isAmrToMp3",defaultValue="true" )String isAmrToMp3, HttpServletResponse response) throws Exception {
		BaseResp resp = new BaseResp();
		try {
			Map<String, Object> data = FileUtil.saveFile(file,isMp3ToAmr,isAmrToMp3,PropertyUtils.getServerIp(env),PropertyUtils.getHttpPort(env),PropertyUtils.getServerFilePath(env));
			resp.setData(data);
		} catch (Exception e) {
			log.error("上传文件出错：{}", ExceptionUtils.getStackTrace(e));
			resp.setMsg("文件上传失败");
			resp.setBizCode(ActionResult.BIZCODE_ERROR);
		}
		return resp;
	}
 
}
