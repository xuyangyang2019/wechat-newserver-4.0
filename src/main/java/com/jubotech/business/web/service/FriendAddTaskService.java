package com.jubotech.business.web.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.protobuf.util.JsonFormat;
import com.jubotech.business.web.dao.FriendAddTaskDao;
import com.jubotech.business.web.dao.FriendAddTaskDetailsDao;
import com.jubotech.business.web.dao.PhoneNumberDao;
import com.jubotech.business.web.domain.AccountInfo;
import com.jubotech.business.web.domain.FriendAddTask;
import com.jubotech.business.web.domain.FriendAddTaskDetails;
import com.jubotech.business.web.domain.FriendAddTaskSetting;
import com.jubotech.business.web.domain.PhoneNumberInfo;
import com.jubotech.business.web.domain.WechatConfig;
import com.jubotech.business.web.query.FriendAddTaskQuery;
import com.jubotech.framework.util.DateUtil;

import Jubo.JuLiao.IM.Wx.Proto.AddFriendsTask.AddFriendsTaskMessage;

@Service
@Transactional // 支持事务
public class FriendAddTaskService {
	
	@Autowired
	private AccountService  accountService;
	
	@Autowired
	private FriendAddTaskDao friendAddTaskDao;
	
	@Autowired
	private FriendAddTaskDetailsDao  friendAddTaskDetailsDao;
	
	@Autowired
	private PhoneNumberDao phoneNumberDao;
 
  
	
	public PageInfo<FriendAddTask> pageList(FriendAddTaskQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());
         
//		Example example = new Example(FriendAddTask.class);
//		Example.Criteria criteria = example.createCriteria();
//		criteria.andEqualTo("cid", query.getCid());
//		if(null != query.getAccountid() && 0 != query.getAccountid()){
//			criteria.andEqualTo("accountid", query.getAccountid());
//		}
//		 
//		if(null != query.getState()) {
//			criteria.andEqualTo("state", query.getState());
//		}
//		example.orderBy("createTime").desc();
		
//		return new PageInfo<>(friendAddTaskDao.selectByExample(example));
		
		return new PageInfo<>(friendAddTaskDao.pageList(query));
		 
	}
	
	
	public void taskStateUpdate(FriendAddTask info) {
		FriendAddTask task = findFriendAddTaskByid(info.getId());
		if(null != task){
			//状态  -1暂停     0已完成    1开启中     2取消
			List<FriendAddTaskDetails>  list  = friendAddTaskDetailsDao.findFriendAddTaskDetailsByTid(info.getId(),1);//查询开启中的
			if(null != list && list.size()>0){
				stateUpdate(info, list);
			}
			 
			List<FriendAddTaskDetails>  list1  = friendAddTaskDetailsDao.findFriendAddTaskDetailsByTid(info.getId(),-1);//查询暂停的
			if(null != list1 && list1.size()>0){
				stateUpdate(info, list1);
			}
			
			
			List<FriendAddTaskDetails>  list2  = friendAddTaskDetailsDao.findFriendAddTaskDetailsByTid(info.getId(),2);//查询取消的
			if(null != list2 && list2.size()>0){
				stateUpdate(info, list2);
			}
			
			
			task.setState(info.getState());
			
			try {
				if(info.getState().equals("1")){//开启中
					String dataStr = DateUtil.convertDate2String(new Date(), DateUtil.DATE_FORMAT_4);
					task.setExecute_time(dataStr);
				}
				
				friendAddTaskDao.update(task);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		 
	}

	private void stateUpdate(FriendAddTask info, List<FriendAddTaskDetails> list) {
		for(FriendAddTaskDetails detail:list){
			detail.setState(info.getState());
			try {
				
				if(info.getState().equals("1")){//开启中
					String str = detail.getExecute_time();
				    String [] strs = str.split(" ");
				    if(strs.length>1){
				    	
				        String secondStr = strs[1];
				        String dataStr = DateUtil.convertDate2String(new Date(), DateUtil.DATE_FORMAT_1);
				        String newTimeStr = dataStr+" "+secondStr;
				        
				        detail.setExecute_time(newTimeStr);
				    }
				}
				
				friendAddTaskDetailsDao.updateState(detail);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	

	public FriendAddTask findFriendAddTaskByid(Integer id) {
		return friendAddTaskDao.findFriendAddTaskByid(id);
	}
 

	public void delete(Integer id) {
		try {
			//先删除子任务
			friendAddTaskDetailsDao.deleteByTid(id);
			//再删除主任务
			FriendAddTask user = new FriendAddTask();
			user.setId(id);
			friendAddTaskDao.delete(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void delete(FriendAddTaskDetails info) {
		try {
			friendAddTaskDetailsDao.deleteByPrimaryKey(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update(FriendAddTask info){
		friendAddTaskDao.update(info);
	}
	 
 
	public List<FriendAddTask> findFriendAddTaskByTime(String execute_time){
		return friendAddTaskDao.findFriendAddTaskByTime(execute_time);
	}
	
	public List<FriendAddTaskDetails> findTimeTaskDetailsByTid(Integer tid){
		return friendAddTaskDetailsDao.findFriendAddTaskDetailsByTid(tid,1);
	}
	
	public List<FriendAddTaskDetails> findFriendAddTaskDetailsByTime(String execute_time){
		return friendAddTaskDetailsDao.findFriendAddTaskDetailsByTime(execute_time);
	}
	
	
	 
	public void updateDetailState(FriendAddTaskDetails info){
		friendAddTaskDetailsDao.updateState(info);
	}
	
	private static void saveAddFriendTaskDetail(FriendAddTask info ,FriendAddTaskDetailsDao  friendAddTaskDetailsDao,String extcute_time,String phoneNumber){
			try {
				String wechatId = info.getWechatid();
				FriendAddTaskDetails detail = new FriendAddTaskDetails();
				detail.setTid(info.getId());
				detail.setExecute_time(extcute_time);
				detail.setWechatid(wechatId);
				detail.setState(1);
				detail.setPhonenumber(phoneNumber);
				friendAddTaskDetailsDao.insert(detail);
				 
				if(null != detail.getId()){
					//按微信号生成需要发的消息内容
					AddFriendsTaskMessage.Builder buider = AddFriendsTaskMessage.newBuilder();
					buider.addPhones(phoneNumber);
					buider.setTaskId(detail.getId());
					buider.setWeChatId(wechatId);
					buider.setMessage(info.getMessage());
					buider.setRemark(info.getRemarks());
				 
					AddFriendsTaskMessage msg = buider.build();
					String json = null;
					if (null != msg) {
						try {
							json = JsonFormat.printer().print(msg);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(!StringUtils.isEmpty(json)){
						FriendAddTaskDetails tds =  new FriendAddTaskDetails();
						tds.setId(detail.getId());
						tds.setJson_content(json);
						friendAddTaskDetailsDao.updateJsonContent(tds);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	
	public synchronized boolean createTaskDetails(FriendAddTask info){
		 
		try {
			Integer betweenTime =  info.getBetween_time();
			String extcute_time = info.getExecute_time();
			Date extcuteTime = DateUtil.convertString2Date(extcute_time, DateUtil.DATE_FORMAT_4);
			Long longTime = extcuteTime.getTime();
			
			List<PhoneNumberInfo>  phones = phoneNumberDao.queryPhonesByCid(info.getCid(),info.getTotalsize());
			if(null != phones && phones.size()>0){
				for(PhoneNumberInfo phone :phones){
					extcute_time = DateUtil.convertDate2String(new Date(longTime), DateUtil.DATE_FORMAT_4);
					saveAddFriendTaskDetail(info,friendAddTaskDetailsDao,extcute_time,phone.getPhonenumber());
					try {
						phone.setState(0);
						phone.setWechatid(info.getWechatid());
						phoneNumberDao.update(phone);
						//phoneNumberDao.delete(phone);
					} catch (Exception e) {
						e.printStackTrace();
					}
					//修改时间
					longTime = longTime+(betweenTime * 60 * 1000);
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
    
    /**
     * pc端添加定时任务
     * @param info
     * @return
     */
    public synchronized String savePcTask(FriendAddTaskSetting info) {
		String res = "success";
		try {
			
			FriendAddTask task = new FriendAddTask();
			task.setAccountid(info.getAccountid());
			task.setCreateTime(new Date());
			task.setMessage(info.getMessage());
			if(null != info.getCid() && info.getCid()>0) {
				task.setCid(info.getCid());
			}else {
				AccountInfo  account = accountService.findAccountInfoByid(info.getAccountid());
	    		if(null != account){
	    			task.setCid(account.getCid());
	    		}
			}
			
    		
    		List<WechatConfig>  wechatList = info.getWechatConfig();
    		int phoneCountAll=0;
			for(WechatConfig config :wechatList){
				phoneCountAll = phoneCountAll + config.getAdd_count();
			}	
    		 
			for(WechatConfig config :wechatList){
				Integer count = config.getAdd_count();//每个微信加多少
				if(count >0 ){
					task.setWechatid(config.getWechatid());
					//主任务存储数据库
					task.setState(1);//1开启中 0已完成
					task.setBetween_time(info.getBetween_time());
					task.setDoingsize(0);
					task.setTotalsize(count);
					task.setSuccesssize(0);
					task.setRemarks(info.getRemarks());
					task.setExecute_time(info.getExecute_time());
					friendAddTaskDao.insert(task);
					
				}
				
			    
			}
			     
		} catch (Exception e) {
			res = "fail";
			e.printStackTrace();
		}

		return res;
	}
    

}
