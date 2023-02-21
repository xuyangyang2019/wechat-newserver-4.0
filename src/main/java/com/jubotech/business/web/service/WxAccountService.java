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
import com.jubotech.business.web.dao.CustomerDao;
import com.jubotech.business.web.dao.WxAccountDao;
import com.jubotech.business.web.domain.WxAccountInfo;
import com.jubotech.business.web.query.DeviceQuery;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.common.ServiceException;
import com.jubotech.framework.netty.common.Constant;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class WxAccountService {

	@Autowired
	private WxAccountDao weChatAccountDao;

	@Autowired
	private CustomerDao customerDao;

	public PageInfo<WxAccountInfo> pageList(DeviceQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());
		return new PageInfo<>(weChatAccountDao.pageList(query));
	}
	
	public List<WxAccountInfo> queryByWechatlist(List<String> wechats) {
		Example example = new Example(WxAccountInfo.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("wechatid", wechats);
		return weChatAccountDao.selectByExample(example);
	}
	
	public List<WxAccountInfo> list(DeviceQuery query) {
		return weChatAccountDao.findWeChatAccountInfo(query.getCid(), query.getAccountid());
	}

	public ResultInfo queryBygroupid(String groupids) {
		try {

			if (StringUtils.isBlank(groupids) || null == groupids) {
				return ResultInfo.fail("参数传入错误");
			}
			String[] idArray = StringUtils.split(groupids, ",");
			Set<String> gIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
			Example example = new Example(WxAccountInfo.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andIn("groupid", gIds);

			List<WxAccountInfo> list = weChatAccountDao.selectByExample(example);

			return ResultInfo.success(list);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultInfo.fail("查询失败");
		}
	}

	public ResultInfo group(String deviceids, Integer groupid) {
		ResultInfo res = new ResultInfo();
		try {

			if (StringUtils.isBlank(deviceids) || null == groupid) {
				return ResultInfo.fail("参数传入错误");
			}
			String[] idArray = StringUtils.split(deviceids, ",");
			Set<String> userIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
			Example example = new Example(WxAccountInfo.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andIn("id", userIds);

			List<WxAccountInfo> list = weChatAccountDao.selectByExample(example);
			for (WxAccountInfo account : list) {
				account.setGroupid(groupid);
				weChatAccountDao.update(account);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResultInfo.fail("修改失败");
		}
		return res;
	}

	public void deleteByIds(String ids) {
		if (StringUtils.isBlank(ids)) {
			throw new ServiceException("invalid param");
		}
		String[] idArray = StringUtils.split(ids, ",");
		Set<String> userIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
		Example example = new Example(WxAccountInfo.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", userIds);

		weChatAccountDao.deleteByExample(example);

		refreshCache();// 刷新设备缓存

	}

	public void delete(Integer id) {
		if (id != null) {
			WxAccountInfo info = new WxAccountInfo();
			info.setId(id);
			weChatAccountDao.delete(info);

			refreshCache();// 刷新设备缓存
		}
	}

	public ResultInfo insert(WxAccountInfo info) {
		ResultInfo res = new ResultInfo();
		Integer count = customerDao.getCustomerPhoneCount(info.getCid());// 该客户端的的设备数
		Integer pcount = weChatAccountDao.getPhoneAllCountByCid(info.getCid());// 已经绑定的设备数
		if (pcount >= count) {
			return ResultInfo.fail("超过账号个数");
		} else {
			try {
				String deviceid = info.getDeviceid().trim();
				info.setDeviceid(deviceid);
				weChatAccountDao.insert(info);
			} catch (Exception e) {
				return ResultInfo.fail("添加失败");
			}
		}

		refreshCache();// 刷新设备缓存
		return res;
	}

	public ResultInfo update(WxAccountInfo info) {
		ResultInfo res = new ResultInfo();
		try {
			weChatAccountDao.update(info);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultInfo.fail("修改失败");
		}
		return res;
	}

	public ResultInfo updateDevice(WxAccountInfo info) {
		ResultInfo res = new ResultInfo();
		try {

			WxAccountInfo newInfo = weChatAccountDao.findWeChatAccountInfoByid(info.getId());

			newInfo.setDeviceid(info.getDeviceid().trim());
			newInfo.setAccountid(info.getAccountid());
			newInfo.setGroupid(info.getGroupid());
			newInfo.setDevnickname(info.getDevnickname());
			newInfo.setBrand(info.getBrand());
			newInfo.setModule(info.getModule());
			newInfo.setSnumber(info.getSnumber());

			weChatAccountDao.update(newInfo);

			refreshCache();// 刷新设备缓存
		} catch (Exception e) {
			e.printStackTrace();
			return ResultInfo.fail("修改失败");
		}
		return res;
	}

	public void updateLoginTime(String deviceid) {
		weChatAccountDao.updateLoginTime(deviceid);
	}

	public WxAccountInfo getWeChatAccountByid(Integer id) {
		WxAccountInfo user = weChatAccountDao.findWeChatAccountInfoByid(id);
		return user;
	}

	public WxAccountInfo findWeChatAccountInfoByDeviceid(String deviceid) {
		WxAccountInfo user = weChatAccountDao.findWeChatAccountInfoByDeviceid(deviceid);
		return user;
	}

	public WxAccountInfo findWeChatAccountInfoByWeChatId(String weChatId) {
		WxAccountInfo user = weChatAccountDao.findWeChatAccountInfoByWeChatId(weChatId);
		return user;
	}

	public List<WxAccountInfo> findWeChatAccountInfo(Integer cid, Integer accountid) {
		return weChatAccountDao.findWeChatAccountInfo(cid, accountid);
	}

	public List<WxAccountInfo> findAllWeChatAccountInfo(Integer isonline) {
		return weChatAccountDao.findAllWeChatAccountInfo(isonline);
	}

	public List<WxAccountInfo> findWeChatAccountInfoByWeChatIdNotEqualsDeviceid(String wechatid, String deviceid) {
		return weChatAccountDao.findWeChatAccountInfoByWeChatIdNotEqualsDeviceid(wechatid, deviceid);
	}

	public Integer findAllWeChatAccount(Integer cid) {
		return weChatAccountDao.findAllWeChatAccount(cid);
	}

	public Integer findOnLineWeChatAccount(Integer cid) {
		return weChatAccountDao.findOnLineWeChatAccount(cid);
	}
	
	 
	public List<WxAccountInfo> findAllAccountWechatInfo() {
		List<WxAccountInfo> accList = weChatAccountDao.findAllAccountWechatInfo();
		return accList;
	}

	/**
	 * 刷新缓存
	 */
	public List<WxAccountInfo> refreshCache() {
		List<WxAccountInfo> accList = weChatAccountDao.findAllAccountWechatInfo();
		if (null != accList && accList.size() > 0) {
			Constant.accountInfoList = accList;
		}
		return accList;
	}

}
