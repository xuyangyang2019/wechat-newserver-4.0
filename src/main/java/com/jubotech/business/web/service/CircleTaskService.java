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
import com.google.protobuf.util.JsonFormat;
import com.jubotech.business.web.dao.CircleCommentDao;
import com.jubotech.business.web.dao.CircleLikeDao;
import com.jubotech.business.web.dao.CircleTaskDao;
import com.jubotech.business.web.dao.CircleTaskDetailsDao;
import com.jubotech.business.web.dao.TimeTaskDetailsDao;
import com.jubotech.business.web.domain.CircleComment;
import com.jubotech.business.web.domain.CircleLike;
import com.jubotech.business.web.domain.CircleTask;
import com.jubotech.business.web.domain.CircleTaskDetails;
import com.jubotech.business.web.domain.TimeTaskDetails;
import com.jubotech.business.web.domain.WxAccountInfo;
import com.jubotech.business.web.query.CircleTaskQuery;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.common.ServiceException;
import com.jubotech.framework.netty.utils.JsonToProtoConverterUtil;
import com.jubotech.framework.netty.utils.MsgIdBuilder;
import com.jubotech.framework.util.DateUtil;
import com.jubotech.framework.util.StringUtil;

import Jubo.JuLiao.IM.Wx.Proto.DeleteSNSNewsTask.DeleteSNSNewsTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.PostSNSNewsTask.PostSNSNewsTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.PostSNSNewsTask.PostSNSNewsTaskMessage.AttachmentMessage;
import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class CircleTaskService {
	
	@Autowired
	private WxAccountService wxAccountService;

	@Autowired
	private TimeTaskDetailsDao timeTaskDetailsDao;

	@Autowired
	private CircleTaskDao circleTaskDao;

	@Autowired
	private CircleTaskDetailsDao circleTaskDetailsDao;

	@Autowired
	private CircleLikeDao circleLikeDao;

	@Autowired
	private CircleCommentDao circleCommentDao;
	
	
	public Integer queryCircleSize(Integer cid) {
		Example example = new Example(CircleTask.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("cid", cid);
		return circleTaskDao.selectCountByExample(example);
		 
	}
	
	public Integer queryCircleCommentSize(Integer cid) {
		List<CircleTask> list=  circleTaskDao.findLikeCommentsize(cid);
		Integer count =0;
		if(null != list) {
			for(CircleTask circle:list) {
				count = count+circle.getCommentsize();
			}
		}
		 return count;
	}
	
	public Integer queryCircleLikeSize(Integer cid) {
		List<CircleTask> list=  circleTaskDao.findLikeCommentsize(cid);
		Integer count =0;
		if(null != list) {
			for(CircleTask circle:list) {
				count = count+circle.getLikesize();
			}
		}
		 return count;
	}
	 
	public ResultInfo resendCircle(Integer id) {
		 
		try {
			Example example = new Example(CircleTaskDetails.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("id", id);
			CircleTaskDetails detail =  circleTaskDetailsDao.selectOneByExample(example);
			if(null != detail) {
				boolean flag = JsonToProtoConverterUtil.sendPostSNSNewsTaskMessage(detail.getJson_content());
				if(flag) {
					return ResultInfo.success();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultInfo.fail();
	}
	
	
	public ResultInfo findWxAccountByTaskId(Integer id){
		CircleTask task = getByid(id);
		if(null != task) {
			String wechats = task.getWechats();
			if(!StringUtils.isEmpty(wechats)) {
				List<String> wechatList = StringUtil.stringToList(wechats);
				if(null != wechatList) {
					List<WxAccountInfo> wxAccount = wxAccountService.queryByWechatlist(wechatList);
					return ResultInfo.success(wxAccount);
				}
			}
		}
		return  ResultInfo.success();
	}
	
	public List<CircleTask> findTaskTimeByTime(String execute_time){
		return circleTaskDao.findTaskTimeByTime(execute_time);
	}
	
	public List<CircleTaskDetails> findTimeTaskDetailsByTid(Integer tid,String date){
		return circleTaskDetailsDao.findTimeTaskDetailsByTidDate(tid,1,date);
	}
	
	public List<CircleTaskDetails> findByTid(Integer tid){
		Example example = new Example(CircleTaskDetails.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("tid", tid);
		return circleTaskDetailsDao.selectByExample(example);
	}
	
	public List<CircleTaskDetails> findTimeTaskDetailsByTidState(Integer tid,Integer state){
		Example example = new Example(CircleTaskDetails.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("tid", tid);
		criteria.andEqualTo("state", state);
		return circleTaskDetailsDao.selectByExample(example);
	}
	
	public CircleTaskDetails findByWechatIdMsgId(String wechatid,String msgid){
		Example example = new Example(CircleTaskDetails.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("wechatid", wechatid);
		criteria.andEqualTo("msgid", msgid);
		return circleTaskDetailsDao.selectOneByExample(example);
	}
	
	public CircleTaskDetails findByWechatIdMsgId(String msgid){
		Example example = new Example(CircleTaskDetails.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("msgid", msgid);
		return circleTaskDetailsDao.selectOneByExample(example);
	}

	public PageInfo<CircleTask> pageList(CircleTaskQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());

//		Example example = new Example(CircleTask.class);
//		Example.Criteria criteria = example.createCriteria();
//		if (null != query.getCid()) {
//			criteria.andEqualTo("cid", query.getCid());
//		}
//		if (null != query.getState()) {
//			criteria.andEqualTo("state", query.getState());
//		}
//
//		if (null != query.getRestype()) {
//			criteria.andEqualTo("restype", query.getRestype());
//		}
//		if (StringUtils.isNotBlank(query.getContent())) {
//			criteria.andLike("content", "%" + query.getContent() + "%");
//		}
//
//		example.orderBy("id").desc();

		return new PageInfo<>(circleTaskDao.pageList(query));
	}

	public ResultInfo deleteByIds(String ids) {
		ResultInfo res = new ResultInfo();
		try {
			if (StringUtils.isBlank(ids)) {
				throw new ServiceException("invalid param");
			}
			String[] idArray = StringUtils.split(ids, ",");
			Set<String> userIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
			for (String id : userIds) {
				delete(Integer.valueOf(id));
			}

		} catch (Exception e) {
			return ResultInfo.fail("删除失败");
		}
		return res;
	}

	public CircleTask getByid(Integer id) {
		CircleTask user = circleTaskDao.selectByPrimaryKey(id);
		return user;
	}

	public ResultInfo insert(CircleTask info) {
		ResultInfo res = new ResultInfo();
		try {
			if (!StringUtils.isEmpty(info.getWechats())) {
				String[] wechats = info.getWechats().split(",");
				info.setCreateTime(new Date());
				info.setLikesize(0);
				info.setCommentsize(0);
				info.setTotalsize(wechats.length);;
				info.setState(1);
				info.setDeleted("false");
				circleTaskDao.insert(info);
				
				Date newDate = DateUtil.convertString2Date(info.getExecute_time(), DateUtil.DATE_FORMAT_4);
				Integer betweenTime = 1;
				if (info.getBetween_time() != null) {
					betweenTime = info.getBetween_time();
				}
				
				for (String wechatid : wechats) {

					if (null != info.getId()) {
						// 设置附件
						AttachmentMessage.Builder attachment = AttachmentMessage.newBuilder();
						if(null != info.getRestype()) {
							attachment.setTypeValue(info.getRestype());
						}
						if(!StringUtils.isEmpty(info.getAttachtcontent())) {
							attachment.addAllContent(StringUtil.stringToList(info.getAttachtcontent()));
						}
						
						Long taskid = MsgIdBuilder.getId();
						// 按微信号生成需要发的消息内容
						PostSNSNewsTaskMessage.Builder buider = PostSNSNewsTaskMessage.newBuilder();
						buider.setContent(info.getContent());
						buider.setAttachment(attachment);
						buider.setTaskId(taskid);
						buider.setWeChatId(wechatid);
						buider.setComment(info.getComment());
						PostSNSNewsTaskMessage msg = buider.build();
						String json = null;
						if (null != msg) {
							try {
								json = JsonFormat.printer().print(msg);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (!StringUtils.isEmpty(json)) {
							CircleTaskDetails details = new CircleTaskDetails();
							details.setCreateTime(new Date());
							details.setExecute_time(DateUtil.convertDate2String(newDate, DateUtil.DATE_FORMAT_2));
							details.setTid(info.getId());
							details.setState(1);
							details.setWechatid(wechatid);
							details.setJson_content(json);
							details.setMsgid(String.valueOf(taskid));
							circleTaskDetailsDao.insert(details);
						}
					}
				}

				newDate = DateUtil.offsetMinute(newDate, betweenTime);
			}

		} catch (Exception e) {
			return ResultInfo.fail("添加失败");
		}
		return res;
	}

	public ResultInfo update(CircleTask info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			circleTaskDao.updateByPrimaryKey(info);
		} catch (Exception e) {
			return ResultInfo.fail("修改失败");
		}
		return res;
	}
	
	public ResultInfo updateDetail(CircleTaskDetails info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			circleTaskDetailsDao.updateByPrimaryKey(info);
		} catch (Exception e) {
			return ResultInfo.fail("修改失败");
		}
		return res;
	}

	public void delete(Integer id) {
		 
		Example example = new Example(CircleTaskDetails.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("tid", id);
		List<CircleTaskDetails> list = circleTaskDetailsDao.selectByExample(example);
		if (null != list && list.size() > 0) {
			for (CircleTaskDetails detail : list) {
				String wechatid = detail.getWechatid();
				String circleid = detail.getMsgid();
				
				if(!circleid.startsWith("-")) {
					continue;
				}
				 
				boolean flag = JsonToProtoConverterUtil.deleteSNSNewsTaskMessage(circleid, wechatid);
				if (!flag) {
					try {
						DeleteSNSNewsTaskMessage.Builder bd = DeleteSNSNewsTaskMessage.newBuilder();
						bd.setCircleId(Long.valueOf(circleid));
						bd.setWeChatId(wechatid);
						bd.setTaskId(MsgIdBuilder.getId());
						DeleteSNSNewsTaskMessage req = bd.build();

						String json = null;
						if (null != req) {
							json = JsonFormat.printer().print(req);
						}

						TimeTaskDetails timeTaskDetails = new TimeTaskDetails();
						timeTaskDetails.setExecute_time(DateUtil.convertDate2String(new Date(), DateUtil.DATE_FORMAT_2));
						timeTaskDetails.setJson_content(json);
						timeTaskDetails.setState(1);
						timeTaskDetails.setTid(0);
						timeTaskDetailsDao.insert(timeTaskDetails);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				 

				 
				Example example1 = new Example(CircleLike.class);
				Example.Criteria criteria1 = example1.createCriteria();
				criteria1.andEqualTo("wechatid", wechatid);
				criteria1.andEqualTo("circleid", circleid);
				List<CircleLike> list1 = circleLikeDao.selectByExample(example1);
				if (null != list1 && list1.size() > 0) {
					for (CircleLike circleLike : list1) {
						circleLikeDao.delete(circleLike);
					}
				}

				Example example2 = new Example(CircleComment.class);
				Example.Criteria criteria2 = example2.createCriteria();
				criteria2.andEqualTo("wechatid", wechatid);
				criteria2.andEqualTo("circleid", circleid);
				List<CircleComment> list2 = circleCommentDao.selectByExample(example2);
				if (null != list2 && list2.size() > 0) {
					for (CircleComment circleComment : list2) {
						circleCommentDao.delete(circleComment);
					}
				}

			}
		}
		
		CircleTask info = getByid(id);
		info.setDeleted("true");
		circleTaskDao.updateByPrimaryKey(info);
	}

}
