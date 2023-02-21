package com.jubotech.business.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.PhoneListReq;
import com.jubotech.business.web.domain.PhoneNumberInfo;
import com.jubotech.business.web.query.PhoneNumberQuery;
import com.jubotech.business.web.service.PhoneNumberService;
import com.jubotech.framework.common.GridPage;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/user/phoneNumber")
public class PhoneNumberController {
	@InitBinder
    public void initListBinder(WebDataBinder binder){
        // 设置需要包裹的元素个数，默认为256
		binder.setAutoGrowNestedPaths(true);  
        binder.setAutoGrowCollectionLimit(6000);
    }
	
	@Autowired
	private PhoneNumberService service;
	  
	@PostMapping("/pageList")
	@ResponseBody
	public ResultInfo pageList(PhoneNumberQuery query) {
		GridPage<PhoneNumberInfo> gridPage = new GridPage<>(service.pageList(query));
		return ResultInfo.success(gridPage);
	}
	
    /**
     * 查询单条数据
     * @param id
     * @return
     */
	@PostMapping(value = "/queryById")
    @ResponseBody
    public ResultInfo queryById(@RequestParam(value = "id") Integer id){
		ResultInfo res = ResultInfo.success();
		PhoneNumberInfo tag = service.getByid(id);
    	res.setData(tag);
    	return res;
    }

	@PostMapping("/add")
	@ResponseBody
	public ResultInfo add(PhoneNumberInfo info) {
		return service.insert(info);
	}
	
	@PostMapping("/addPhoneList")
	@ResponseBody
	public ResultInfo addPhoneList(PhoneListReq req) {
		Integer errorSize=0;
		Integer successSize=0;
		  
		if(null != req.getList() && req.getList().size()>0){
			for(String phone:req.getList()){
				PhoneNumberInfo info = new PhoneNumberInfo();
				info.setPhonenumber(phone);
				info.setState(1);
				info.setCid(req.getCid());
				info.setTask_result("unknown");
				try {
					service.insert(info);
					successSize = successSize+1;
				} catch (Exception e) {
					errorSize = errorSize+1;
				}
			}
		}
		 
		return ResultInfo.success("导入成功"+successSize+"个，失败"+errorSize+"个");
	}

	@PostMapping("/update")
	@ResponseBody
	public ResultInfo update(PhoneNumberInfo info) {
		PhoneNumberInfo phone = service.getByid(info.getId());
		if(null != phone){
			phone.setPhonenumber(info.getPhonenumber());
			phone.setState(info.getState());
		}
		return service.update(phone);
	}

	@PostMapping("/deletes")
	@ResponseBody
	public ResultInfo deletes(String ids) {
		service.deleteByIds(ids);
		return ResultInfo.success();
	}

}
