package com.jubotech.business.web.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jubotech.business.web.dao.DeviceGroupDao;
import com.jubotech.business.web.domain.DeviceGroup;
import com.jubotech.business.web.query.DeviceGroupQuery;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.common.ServiceException;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class DeviceGroupService {

	@Autowired
	private DeviceGroupDao deviceGroupDao;

	public PageInfo<DeviceGroup> pageList(DeviceGroupQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());
		return new PageInfo<>(deviceGroupDao.pageList(query));
	}

	public ResultInfo deleteByIds(String ids) {
		ResultInfo res = new ResultInfo();
		try {
			if (StringUtils.isBlank(ids)) {
				throw new ServiceException("invalid param");
			}
			String[] idArray = StringUtils.split(ids, ",");
			Set<String> userIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
			Example example = new Example(DeviceGroup.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andIn("id", userIds);

			deviceGroupDao.deleteByExample(example);
		} catch (Exception e) {
			return ResultInfo.fail("删除失败");
		}
		return res;
	}

	public List<DeviceGroup> getAllByCid(Integer cid) {
		List<DeviceGroup> list = deviceGroupDao.getAllByCid(cid);
		return list;
	}

	public DeviceGroup getCommontermInfoByid(Integer id) {
		DeviceGroup user = deviceGroupDao.findByid(id);
		return user;
	}

	public ResultInfo insert(DeviceGroup info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			deviceGroupDao.insert(info);
		} catch (Exception e) {
			return ResultInfo.fail("添加失败");
		}
		return res;
	}

	public ResultInfo update(DeviceGroup info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			deviceGroupDao.update(info);
		} catch (Exception e) {
			return ResultInfo.fail("修改失败");
		}
		return res;
	}

	public void delete(Integer id) {
		DeviceGroup info = new DeviceGroup();
		info.setId(id);
		deviceGroupDao.delete(info);
	}

}
