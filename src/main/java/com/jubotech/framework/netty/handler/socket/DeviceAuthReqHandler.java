package com.jubotech.framework.netty.handler.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.protobuf.util.JsonFormat;
import com.jubotech.business.web.domain.WxAccountInfo;
import com.jubotech.business.web.service.WxAccountService;
import com.jubotech.framework.netty.common.Constant;
import com.jubotech.framework.netty.handler.MessageHandler;
import com.jubotech.framework.netty.utils.MessageUtil;
import com.jubotech.framework.netty.utils.NettyConnectionUtil;

import Jubo.JuLiao.IM.Wx.Proto.DeviceAuthReq.DeviceAuthReqMessage;
import Jubo.JuLiao.IM.Wx.Proto.DeviceAuthReq.DeviceAuthReqMessage.EnumAuthType;
import Jubo.JuLiao.IM.Wx.Proto.DeviceAuthRsp.DeviceAuthRspMessage;
import Jubo.JuLiao.IM.Wx.Proto.DeviceAuthRsp.DeviceAuthRspMessage.ExtraMessage;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumAccountType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumErrorCode;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.TransportMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeviceAuthReqHandler implements MessageHandler{
	@Autowired
	private NettyConnectionUtil nettyConnectionUtil;
	  
	@Autowired
	private WxAccountService weChatAccountService;
	
	/**
	 * 设备校验，通过存储token、imei建立连接
	 * @author wechatno:tangjinjinwx
     * @blog http://www.wlkankan.cn
	 */
    public  void handleMsg(ChannelHandlerContext ctx, TransportMessage vo) {
        try {
        	DeviceAuthReqMessage req = vo.getContent().unpack(DeviceAuthReqMessage.class);
        	log.debug(JsonFormat.printer().print(req));
        	if(null == req){
        		MessageUtil.sendErrMsg(ctx, EnumErrorCode.InvalidParam,vo.getId(), Constant.ERROR_MSG_DECODFAIL);
        		return;
        	}
        	if(!req.getAuthType().equals(EnumAuthType.DeviceCode)){
        		MessageUtil.sendErrMsg(ctx, EnumErrorCode.InvalidParam,vo.getId(), Constant.ERROR_MSG_VERIFYWAY);
        		return;
        	}
        	if(StringUtils.isEmpty(req.getCredential())){
        		MessageUtil.sendErrMsg(ctx, EnumErrorCode.InvalidParam,vo.getId(), Constant.ERROR_MSG_PARAMERROR);
        		return;
        	}
        	//设备码（用于手机客户端，此方式Credential应传入手机IMEI）
    		log.debug("手机端登录imei："+req.getCredential());
    		
    		WxAccountInfo  accountInfo =  weChatAccountService.findWeChatAccountInfoByDeviceid(req.getCredential());
        	if(null == accountInfo){
        		MessageUtil.sendErrMsg(ctx, EnumErrorCode.NoRight,vo.getId(), Constant.ERROR_MSG_DATABASENOTBIND);
        		return ;
        	}

    		//3、生成用户token信息
        	String token = nettyConnectionUtil.getNettyId(ctx);
        	log.info("token : "+token);
        	
        	//存储 设备id 和 通道信息
        	nettyConnectionUtil.saveDeviceChannel(ctx, req.getCredential());
        	  
        	//修改登录时间
        	weChatAccountService.updateLoginTime(req.getCredential());
        	
        	//回复消息  
        	sendMsg(null,null, token, ctx, vo);
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendErrMsg(ctx, EnumErrorCode.InvalidParam,vo.getId(), Constant.ERROR_MSG_DECODFAIL);
        }
        
    }
    
   
    
    private void sendMsg(Long unionid,String nickname,String token,ChannelHandlerContext ctx, TransportMessage vo){
     
    	ExtraMessage.Builder buider = ExtraMessage.newBuilder();
    	buider.setSupplierId(1688);
    	buider.setSupplierName("微信客服系统");
    	buider.setAccountType(EnumAccountType.SubUser);//账号类型  子账号 
    	
    	if(null != unionid && !StringUtils.isEmpty(nickname)){
    		buider.setUnionId(unionid);
    		buider.setNickName(nickname);
    	}
    	
    	ExtraMessage  ext = buider.build();
		DeviceAuthRspMessage resp = DeviceAuthRspMessage.newBuilder()
				.setAccessToken(token)
				.setExtra(ext)
				.build();
		
		
		MessageUtil.sendMsg(ctx, EnumMsgType.DeviceAuthRsp, null, vo.getId(), resp);
    	
    }
  
 
}