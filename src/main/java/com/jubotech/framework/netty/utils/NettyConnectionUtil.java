package com.jubotech.framework.netty.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jubotech.business.web.domain.WxAccountInfo;
import com.jubotech.business.web.service.WxAccountService;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NettyConnectionUtil {
	  
	@Autowired
	private WxAccountService wechatservice;
	
	 
	//////////////////////////////////////// 以下用来存通道///////////////////////////////////////////
	/**
	 * key 为deviceid 
	 * value 为channel 用于存贮通道
	 */
	public static Map<String, ChannelHandlerContext> deviceid_nettyChannel = new ConcurrentHashMap<String, ChannelHandlerContext>();

	//////////////////////////////////////////// 以下用来绑定通道////////////////////////////////////////////
	/**
	 * key userId 微信唯一id 或者 pc端账号名 
	 * value ChannelHandlerContext
	 */
	public static Map<String, ChannelHandlerContext> userId_nettyChannel = new ConcurrentHashMap<String, ChannelHandlerContext>();

	 
	/**
	 * 存储通道
	 * 
	 * @param cx
	 * @param deviceid
	 */
	public  synchronized void saveDeviceChannel(ChannelHandlerContext cx, String deviceid) {
		ChannelHandlerContext cc = getNettyChannelByDeviceid(deviceid);
		if (null != cc) {
			deviceid_nettyChannel.remove(deviceid);
		}
		deviceid_nettyChannel.put(deviceid, cx);
	}

	 
	/**
	 * 通过deviceid获取通道
	 * 
	 * @return
	 */
	public static synchronized ChannelHandlerContext getNettyChannelByDeviceid(String deviceid) {
		ChannelHandlerContext ctx = deviceid_nettyChannel.get(deviceid);
		if (null != ctx) {
			return ctx;
		} else {
			return null;
		}
	}

	/**
	 * 删除通道
	 */
	public  synchronized void removeChannel(ChannelHandlerContext cx) {
		// 清除设备通道
		try {
			String nettyid = getNettyId(cx);
			if (!StringUtils.isEmpty(nettyid)) {
				String deviceid = getDeviceidByNettyid(nettyid);
				if (null != deviceid && !"".equals(deviceid)) {
					deviceid_nettyChannel.remove(deviceid);
					if (null != wechatservice) {
						// 手机端下线
						WxAccountInfo wechat = wechatservice.findWeChatAccountInfoByDeviceid(deviceid);
						if (null != wechat) {// 手机设备断线，微信也下线
							wechat.setIsonline(1);
							wechatservice.update(wechat);
						}
					}
				}

				// 清除微信通道
				String userid = getUserIdByNettyId(nettyid);
				if (!StringUtils.isEmpty(userid)) {
					userId_nettyChannel.remove(userid);
				}
 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 客户端退出时调用，清理数据
	 * 
	 * @param ctx
	 */
	public  synchronized void exit(ChannelHandlerContext ctx) {
		try {
			log.info("exit:当前netty通道连接数"+deviceid_nettyChannel.size()+"   有效通道连接数"+userId_nettyChannel.size());
			removeChannel(ctx);
			ctx.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 注册真实用户，客户端以imei为准、pc端以账号用户名为准
	 * 
	 * @param userId
	 * @param cx
	 */
	public synchronized void registerUserid(String userId, ChannelHandlerContext cx) {
		log.info("register:当前netty通道连接数"+deviceid_nettyChannel.size()+"   有效通道连接数"+userId_nettyChannel.size());
		ChannelHandlerContext chc = userId_nettyChannel.get(userId);
		if (null != chc) {
			userId_nettyChannel.remove(userId);
		}
		userId_nettyChannel.put(userId, cx);
	}
	
	
	/**
	 * 通过userId获取nettyId
	 * @param nettyid
	 * @return
	 */
	public  synchronized String  getUserIdByNettyId(String nettyid){
		return getKey(userId_nettyChannel, nettyid);
	}
	
	/**
	 * 通过nettyid获取deviceid
	 * 
	 * @param nettyid
	 * @return
	 */
	public  synchronized String getDeviceidByNettyid(String nettyId) {
		return getKey(deviceid_nettyChannel, nettyId);
	}
	
	public  synchronized ChannelHandlerContext getClientChannelHandlerContextByUserId(String userId) {
		ChannelHandlerContext chc = userId_nettyChannel.get(userId);
		if (null != chc) {
			return chc;
		} else {
			return null;
		}
	}

	public  synchronized String getNettyId(ChannelHandlerContext cx) {
		return cx.channel().id().asShortText();
	}
	
	
	/**
	 * 根据map的value获取map的key  
	 * @param map
	 * @param value
	 * @return
	 */
    private  synchronized String getKey(Map<String, ChannelHandlerContext> map,String value){  
        String key = null;  
        for (Map.Entry<String, ChannelHandlerContext> entry : map.entrySet()) {  
            if(value.equals(getNettyId(entry.getValue()))){  
                key=entry.getKey();  
            }  
        }  
        return key;  
    } 
 
}