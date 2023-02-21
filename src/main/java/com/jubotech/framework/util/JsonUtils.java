package com.jubotech.framework.util;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jubotech.business.web.domain.vo.MessageVo;

public class JsonUtils {
	public static boolean isJson(String str) {
		try {
			JSONObject.parseObject(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static List<MessageVo> json2Bean(String str) {
		try {
			List<MessageVo> list = JSONObject.parseArray(str, MessageVo.class);
			return list;
		} catch (Exception e) {
			return null;
		}
	}

}
