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
import com.jubotech.business.web.dao.CircleTaskDetailsDao;
import com.jubotech.business.web.dao.TimeTaskDetailsDao;
import com.jubotech.business.web.domain.CircleComment;
import com.jubotech.business.web.domain.CircleTaskDetails;
import com.jubotech.business.web.domain.TimeTaskDetails;
import com.jubotech.business.web.domain.vo.CircleCommentVo;
import com.jubotech.business.web.query.CircleCommentQuery;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.common.ServiceException;
import com.jubotech.framework.netty.utils.JsonToProtoConverterUtil;
import com.jubotech.framework.netty.utils.MessageUtil;
import com.jubotech.framework.netty.utils.MsgIdBuilder;
import com.jubotech.framework.netty.utils.NettyConnectionUtil;
import com.jubotech.framework.util.DateUtil;

import Jubo.JuLiao.IM.Wx.Proto.CircleCommentDeleteTask.CircleCommentDeleteTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.CircleCommentReplyTask.CircleCommentReplyTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import io.netty.channel.ChannelHandlerContext;
import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class CircleCommentService {
	 
	@Autowired
	private CircleCommentDao circleCommentDao;
	
	@Autowired
	private TimeTaskDetailsDao timeTaskDetailsDao;
	
	@Autowired
	private CircleTaskDetailsDao circleTaskDetailsDao;
	
	@Autowired
	private NettyConnectionUtil nettyConnectionUtil;

	public PageInfo<CircleComment> pageList(CircleCommentQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());
		return new PageInfo<>(circleCommentDao.pageList(query));
	}
	
	public PageInfo<CircleComment> pageList1(CircleCommentQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());
		return new PageInfo<>(circleCommentDao.pageList1(query));
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

	public CircleComment getByCircleIdWechatId(String circleid ,String wechatid) {
		Example example = new Example(CircleComment.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("wechatid",wechatid);
		criteria.andEqualTo("circleid",circleid);
		CircleComment detail = circleCommentDao.selectOneByExample(example);
		return detail;
	}
	
	public CircleComment getByCircleIdCommentId(String circleid ,String commentid) {
		Example example = new Example(CircleComment.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("circleid",circleid);
		criteria.andEqualTo("commentid",commentid);
		CircleComment detail = circleCommentDao.selectOneByExample(example);
		return detail;
	}

	public CircleComment getByid(Integer id) {
		CircleComment user = circleCommentDao.selectByPrimaryKey(id);
		return user;
	}
	
	public void delete(CircleComment info) {
		circleCommentDao.delete(info);
	}	
	
	public void delete(Integer id) {
		CircleComment info = getByid(id);
		
		if(null == info) {
			return;
		}

		boolean flag = JsonToProtoConverterUtil.deleteCircleCommentMessage(info.getCircleid(),info.getCommentid(), info.getCircle_wechatid());
		if (!flag) {
			try {
				CircleCommentDeleteTaskMessage.Builder bd = CircleCommentDeleteTaskMessage.newBuilder();
				bd.setCircleId(Long.valueOf(info.getCircleid()));
				bd.setCommentId(Long.valueOf(info.getCommentid()));
				bd.setWeChatId(info.getWechatid());
				bd.setTaskId(MsgIdBuilder.getId());
				CircleCommentDeleteTaskMessage req = bd.build();

				String json = null;
				if (null != req) {
					json = JsonFormat.printer().print(req);
				}

				TimeTaskDetails timeTaskDetails = new TimeTaskDetails();
				timeTaskDetails.setExecute_time(DateUtil.convertDate2String(new Date(), DateUtil.DATE_FORMAT_2));
				timeTaskDetails.setJson_content(json);
				timeTaskDetails.setState(1);
				timeTaskDetails.setTid(-1);
				timeTaskDetailsDao.insert(timeTaskDetails);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		circleCommentDao.delete(info);
	}
	
	public void insert(CircleComment circleComment) {
		circleCommentDao.insert(circleComment);
	}
	
	public ResultInfo followComment(CircleComment comment) {
		 
		Example example = new Example(CircleTaskDetails.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("wechatid", comment.getWechatid());
		criteria.andEqualTo("msgid", comment.getCircleid());
		CircleTaskDetails detail = circleTaskDetailsDao.selectOneByExample(example);
		if(null != detail) {
			Example example1 = new Example(CircleTaskDetails.class);
			Example.Criteria criteria1 = example1.createCriteria();
			criteria1.andEqualTo("tid", detail.getTid());
			List<CircleTaskDetails> list = circleTaskDetailsDao.selectByExample(example1);
			if(null != list && list.size()>0) {
				for(CircleTaskDetails circle:list) {
					  
					CircleCommentReplyTaskMessage.Builder bd = CircleCommentReplyTaskMessage.newBuilder();
					bd.setCircleId(Long.valueOf(circle.getMsgid()));
					bd.setContent(comment.getComment());
					bd.setWeChatId(circle.getWechatid());
					bd.setTaskId(MsgIdBuilder.getId());
					CircleCommentReplyTaskMessage  req = bd.build();
					 
					ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(req.getWeChatId());
					if (null != chx) {
						// 发给手机端
						MessageUtil.sendMsg(chx, EnumMsgType.CircleCommentReplyTask, null, null, req);
					}
				}
				
				Example example2 = new Example(CircleComment.class);
				Example.Criteria criteria2 = example2.createCriteria();
				criteria2.andEqualTo("id", comment.getId());
				CircleComment detail2 = circleCommentDao.selectOneByExample(example2);
				if(null != detail2) {
					detail2.setFlag("true");
					circleCommentDao.updateByPrimaryKey(detail2);
				}
				
			}
		}
		 
		return ResultInfo.success();
	}
	
	
	public ResultInfo followMoreComment(CircleCommentVo comment) {
		 
		if(null == comment || null == comment.getId() || null == comment.getComments() || comment.getComments().size()==0 
				|| null == comment.getWechats() || comment.getWechats().size() == 0) {
			return ResultInfo.fail("参数传入错误");
		}
		 
		Example example1 = new Example(CircleTaskDetails.class);
		Example.Criteria criteria1 = example1.createCriteria();
		criteria1.andEqualTo("tid", comment.getId());
		List<CircleTaskDetails> list = circleTaskDetailsDao.selectByExample(example1);
		if(null != list && list.size()>0) {
			for(CircleTaskDetails circle:list) {
				if(comment.getWechats().contains(circle.getWechatid())) {
					for(String ct :comment.getComments()) {
						CircleCommentReplyTaskMessage.Builder bd = CircleCommentReplyTaskMessage.newBuilder();
						bd.setCircleId(Long.valueOf(circle.getMsgid()));
						bd.setContent(ct);
						bd.setWeChatId(circle.getWechatid());
						bd.setTaskId(MsgIdBuilder.getId());
						CircleCommentReplyTaskMessage  req = bd.build();
						 
						ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(req.getWeChatId());
						if (null != chx) {
							// 发给手机端
							MessageUtil.sendMsg(chx, EnumMsgType.CircleCommentReplyTask, null, null, req);
						}
					}
				}
			}
			
			if(null != comment.getIds() && comment.getIds().size()>0) {
				for(Integer id:comment.getIds()) {
					Example example2 = new Example(CircleComment.class);
					Example.Criteria criteria2 = example2.createCriteria();
					criteria2.andEqualTo("id", id);
					CircleComment detail2 = circleCommentDao.selectOneByExample(example2);
					if(null != detail2) {
						detail2.setFlag("true");
						circleCommentDao.updateByPrimaryKey(detail2);
					}
				}
			}
			 
		}
		 
		 
		return ResultInfo.success();
	}

}
