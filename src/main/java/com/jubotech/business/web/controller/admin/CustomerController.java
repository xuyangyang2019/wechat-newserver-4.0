package com.jubotech.business.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.CustomerInfo;
import com.jubotech.business.web.query.CustomerQuery;
import com.jubotech.business.web.service.CustomerService;
import com.jubotech.framework.common.GridPage;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/admin/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService service;
    
	@PostMapping("/pageList")
	@ResponseBody
	public ResultInfo pageList(CustomerQuery query) {
		GridPage<CustomerInfo> gridPage = new GridPage<>(service.pageList(query));
		return ResultInfo.success(gridPage);
	}
 
    /**
     * 信息修改
     * @param user
     * @return
     */
	@PostMapping("/update")
	@ResponseBody
    public ResultInfo edit(CustomerInfo user){
    	return service.update(user);
    }
    
    /**
     * 信息添加
     * @return
     */
	@PostMapping("/add")
	@ResponseBody
    public ResultInfo add(CustomerInfo user){
    	return service.insert(user);
    }
    
    /**
     * 信息删除
     * @param user
     * @param request
     * @return
     */
	@PostMapping("/deletes")
	@ResponseBody
    public ResultInfo deletes(String ids){
    	 service.deleteByIds(ids);
    	return ResultInfo.success();
    }
     
    
}
