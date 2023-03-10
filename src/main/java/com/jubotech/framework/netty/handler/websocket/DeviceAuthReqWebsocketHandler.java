package com.jubotech.framework.netty.handler.websocket;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.protobuf.util.JsonFormat;
import com.jubotech.business.web.domain.AccountInfo;
import com.jubotech.business.web.domain.CustomerInfo;
import com.jubotech.business.web.service.AccountService;
import com.jubotech.business.web.service.CustomerService;
import com.jubotech.framework.netty.common.Constant;
import com.jubotech.framework.netty.handler.JsonMessageHandler;
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
public class DeviceAuthReqWebsocketHandler  implements JsonMessageHandler{
	
	@Autowired
	private NettyConnectionUtil nettyConnectionUtil;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private AccountService accountService;

	/**
	 * 设备校验 如果校验通过存储token、账号建立连接
	 * @author wechatno:tangjinjinwx
	 * @blog http://www.wlkankan.cn
	 */
	@Async
	public void handleMsg(ChannelHandlerContext ctx, TransportMessage vo, String contentJsonStr) {

		try {
			log.debug(contentJsonStr);
			DeviceAuthReqMessage.Builder bd = DeviceAuthReqMessage.newBuilder();
			JsonFormat.parser().merge(contentJsonStr, bd);
			DeviceAuthReqMessage req = bd.build();
			if (null == req) {
				MessageUtil.sendJsonErrMsg(ctx, EnumErrorCode.InvalidParam, Constant.ERROR_MSG_DECODFAIL);
				return;
			}
			// 1、校验用户信息
			if (!req.getAuthType().equals(EnumAuthType.Username)) {// 用户名密码方式（此方式Credential应传入base64(user:pwd)）
				MessageUtil.sendJsonErrMsg(ctx, EnumErrorCode.NoRight, Constant.ERROR_MSG_VERIFYWAY);
				return;
			}
			byte[] byteArray = Base64.getDecoder().decode(req.getCredential());
			String str = new String(byteArray);
			if (StringUtils.isEmpty(str)) {
				MessageUtil.sendJsonErrMsg(ctx, EnumErrorCode.InvalidParam, Constant.ERROR_MSG_DECODFAIL);
				return;
			}
			log.debug("账号密码登录：name=" + str);
			String[] strs = str.split(":");
			if (strs == null || strs.length < 1 || strs[0] == null) {
				MessageUtil.sendJsonErrMsg(ctx, EnumErrorCode.InvalidParam, Constant.ERROR_MSG_DECODFAIL);
				return;
			}
			// pc客服端
			AccountInfo user = accountService.clientlogin(strs[0], strs[1]);
			if (null == user) {
				MessageUtil.sendJsonErrMsg(ctx, EnumErrorCode.NoRight, Constant.ERROR_MSG_LOGINFAIL);
				return;
			}
								
			if (user.getState() != 1) {// 账号状态正常的时候
				MessageUtil.sendJsonErrMsg(ctx, EnumErrorCode.NoRight,Constant.ERROR_MSG_LOGINNORIGHT);
				return;
			}
			// 判断是否有登录，如果有登录直接踢下线
			ChannelHandlerContext chc = nettyConnectionUtil.getClientChannelHandlerContextByUserId(user.getAccount());
			if (null != chc) {
				MessageUtil.sendJsonErrMsg(chc, EnumErrorCode.NoRight,Constant.ERROR_MSG_ELSEWHERELOGINN);//账号已在别处登录
				chc.close();
			}

			// 生成用户token信息
			String token = nettyConnectionUtil.getNettyId(ctx);

			// 存储 用户id 和 通道信息
			nettyConnectionUtil.saveDeviceChannel(ctx, user.getAccount());
			// 存储微信全局id 与通道
			nettyConnectionUtil.registerUserid(user.getAccount(), ctx);

			CustomerInfo customer = customerService.getCustomerInfoByid(user.getCid());

			sendMsg(customer, user, token, ctx, vo);
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.sendJsonErrMsg(ctx, EnumErrorCode.InvalidParam, Constant.ERROR_MSG_DECODFAIL);
		}
    }

	private void sendMsg(CustomerInfo customer, AccountInfo user, String token, ChannelHandlerContext ctx,
			TransportMessage vo) {

		Integer supplierid = customer.getId();
		String suppliername = customer.getSuppliername();// 商家名称
		long unionid = user.getId();// 个人账号id
		String nickname = user.getNickname();// 昵称

		ExtraMessage.Builder buider = ExtraMessage.newBuilder();
		buider.setSupplierId(supplierid);
		buider.setSupplierName(suppliername+"|"+user.getLevel());
		buider.setAccountType(EnumAccountType.SubUser);// 账号类型 子账号

		if (0 != unionid && !StringUtils.isEmpty(nickname)) {
			buider.setUnionId(unionid);
			buider.setNickName(nickname);
		}

		ExtraMessage ext = buider.build();
		DeviceAuthRspMessage resp = DeviceAuthRspMessage.newBuilder().setAccessToken(token).setExtra(ext).build();

		MessageUtil.sendJsonMsg(ctx, EnumMsgType.DeviceAuthRsp, null, vo.getId(), resp);

	}

}