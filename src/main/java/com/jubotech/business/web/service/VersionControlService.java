package com.jubotech.business.web.service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jubotech.business.web.dao.VersionControlDao;
import com.jubotech.business.web.dao.WxAccountDao;
import com.jubotech.business.web.domain.VersionControl;
import com.jubotech.business.web.domain.WxAccountInfo;
import com.jubotech.business.web.query.VersionControlQuery;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.common.ServiceException;
import com.jubotech.framework.netty.utils.MessageUtil;
import com.jubotech.framework.netty.utils.NettyConnectionUtil;

import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import Jubo.JuLiao.IM.Wx.Proto.UpgradeDeviceAppNotice.DeviceAppUpgradeMessage;
import Jubo.JuLiao.IM.Wx.Proto.UpgradeDeviceAppNotice.UpgradeDeviceAppNoticeMessage;
import io.netty.channel.ChannelHandlerContext;
import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class VersionControlService {

	@Autowired
	private VersionControlDao versionControlDao;
	
	@Autowired
	private WxAccountDao wxAccountDao;
	
	 
	@Autowired
	private NettyConnectionUtil nettyConnectionUtil;


	public PageInfo<VersionControl> pageList(VersionControlQuery query) {
        PageHelper.startPage(query.getPage(), query.getRows());
        
        Example example = new Example(VersionControl.class);
		Example.Criteria criteria = example.createCriteria();
		  
		if (null != query.getCid()) {
			criteria.andEqualTo("cid", query.getCid());
		}
		if (StringUtils.isNotBlank(query.getVersion())) {
			criteria.andEqualTo("version", query.getVersion());
		}
		if (null != query.getVernumber()) {
			criteria.andEqualTo("vernumber", query.getVernumber());
		}
		if (StringUtils.isNotBlank(query.getPackagename())) {
			criteria.andLike("packagename", "%" + query.getPackagename() + "%");
		}
		if (null != query.getFlag()) {
			criteria.andEqualTo("flag", query.getFlag());
		}

		example.orderBy("id").desc();

		return new PageInfo<>(versionControlDao.selectByExample(example));
         
    }
	
	
	public ResultInfo deleteByIds(String ids) {
		ResultInfo res = new ResultInfo();
		try {
			if (StringUtils.isBlank(ids)) {
				throw new ServiceException("invalid param");
			}
			String[] idArray = StringUtils.split(ids, ",");
			Set<String> userIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
			Example example = new Example(VersionControl.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andIn("id", userIds);

			versionControlDao.deleteByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
		    return ResultInfo.fail("删除失败");
		}
		return  res;
	}
 
	public VersionControl queryVersionControlById(Integer id){
		return versionControlDao.queryVersionControlById(id);
	}
	
 
	
	public ResultInfo insert(VersionControl info) {
		ResultInfo res = new ResultInfo();
		try {
			versionControlDao.insert(info);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultInfo.fail("添加失败");
		}
		return res;
	}

	public ResultInfo update(VersionControl info) {
		ResultInfo res = new ResultInfo();
		try {
			versionControlDao.update(info);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultInfo.fail("修改失败");
		}
		return res;
	}
	
	 

	public void delete(Integer id) {
		VersionControl info = new VersionControl();
		info.setId(id);
		versionControlDao.delete(info);
	}
	
	public void pushAppUpdate(Integer id){
		VersionControl  info = versionControlDao.queryVersionControlById(id);
		if(null != info){
			info.setFlag(0);
			versionControlDao.update(info);
			
			DeviceAppUpgradeMessage.Builder bd = DeviceAppUpgradeMessage.newBuilder();
			bd.setVersion(info.getVersion());
			bd.setVerNumber(info.getVernumber());
			bd.setPackageName(info.getPackagename());
			bd.setPackageUrl(info.getPackageurl());
			DeviceAppUpgradeMessage req = bd.build();
			  
			List<WxAccountInfo>  list = wxAccountDao.findAccountWechatInfoByCid(info.getCid());
			if(null != list && list.size()>0){
				for(int i=0;i<list.size();i++){
					WxAccountInfo wxinfo = list.get(i);
					if(null != wxinfo && !StringUtils.isEmpty(wxinfo.getWechatid())){
						UpgradeDeviceAppNoticeMessage.Builder ubd =  UpgradeDeviceAppNoticeMessage.newBuilder();
						ubd.setWeChatId(wxinfo.getWechatid());
						ubd.setIMEI(wxinfo.getDeviceid());
						ubd.addAppInfos(req);
						UpgradeDeviceAppNoticeMessage updateMessage = ubd.build();
						
						ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(wxinfo.getWechatid());
						if (null != chx) {
							// 发给手机端
							MessageUtil.sendMsg(chx, EnumMsgType.UpgradeDeviceAppNotice, null, null, updateMessage);
						}
					}
					
				}
			}
			
			
		}
		
	}
	 
	
}
