package com.jubotech.framework.util;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class CouponUtil {
	public static void main(String[] args) {
		System.out.println(request("/€rPlK2b6nJNC€/"));;
	}
	
 
	/**
	 * @author http://www.wlkankan.cn 咨询微信happybabby110
	 * 提供淘宝客转链商用接口
	 * 支持淘宝京东拼多多
	 */
    public static  String request(String msg) {
    	try {
    		msg = EmojiFilter.filterEmoji(msg);
    		/**去除所有特殊字符*/
    		msg=msg.replaceAll("\n|\r","").replaceAll("\"","").replaceAll(" ","");
    		 
    		if(msg.contains("jd.com") || msg.contains("pinduoduo.com")) {
    			return null;
    		}
    		
    		/**
    		 * 提供淘宝客转链商用接口,咨询微信happybabby110
    		 * 淘宝商品转链接口，支持淘链接和淘口令
    		 */
			String url="http://127.0.0.1:8000/api/privilege/getItemByUserMark?api_key=eabd&user_mark=user1&item_id="+msg;
			
			String  resp = HttpUtils.sendGet(url);
			if(!StringUtils.isEmpty(resp)) {
				JSONObject json = JSONObject.parseObject(resp);
				Integer code = json.getInteger("code");
				if(code==0) {
					JSONObject data=json.getJSONObject("data");
					String couponInfo =  data.getString("couponInfo");
					//String shorturl =  data.getString("shorturl");
					String tkl =  data.getString("tkl");
					
					if(!StringUtils.isEmpty(tkl)) {
						StringBuffer sb= new StringBuffer();
						sb.append("----查券成功----\n");
						sb.append("[天啊]原价：0圆\n");
						sb.append("[鼓掌]券后价：0圆\n");
						sb.append("[红包]优惠券："+couponInfo+"圆\n");
						sb.append(tkl+"\n");
						//sb.append(shorturl+"\n");
						sb.append("长按复制本段内容后，打开手淘购买");
						return sb.toString();
					}
					 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
}
