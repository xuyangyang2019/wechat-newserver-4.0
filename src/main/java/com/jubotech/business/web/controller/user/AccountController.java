package com.jubotech.business.web.controller.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.AccountInfo;
import com.jubotech.business.web.query.AccountQuery;
import com.jubotech.business.web.service.AccountService;
import com.jubotech.framework.common.GridPage;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/user/account")
public class AccountController {
	
	@Autowired
	private AccountService service;
  
	@PostMapping("/pageList")
	@ResponseBody
	public ResultInfo pageList(HttpServletRequest request, AccountQuery query) {
		query.setType(1);
		GridPage<AccountInfo> gridPage = new GridPage<>(service.pageList(query));
		return ResultInfo.success(gridPage);
	}
    
	
    /**
     * @param cid
     * @return
     */
	@PostMapping(value = "/queryByCid")
    @ResponseBody
    public ResultInfo queryByCid(@RequestParam(value = "cid") Integer cid){
		ResultInfo res = ResultInfo.success();
		List<AccountInfo>  list = service.getAllAccountInfoByCid(cid);
    	res.setData(list);
    	return res;
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
		AccountInfo user = service.findAccountInfoByid(id);
    	res.setData(user);
    	return res;
    }
   
    /**
     * 信息修改
     * @param user
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/update")
    public ResultInfo edit(AccountInfo user){
    	user.setType(1);
        service.update(user);
    	return ResultInfo.success();
    }
    
    /**
     * 信息添加
     * @param user
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/add")
    public ResultInfo add(AccountInfo user){
    	user.setType(1);
        service.insert(user);
    	return ResultInfo.success();
    }
    
    /**
     * 信息删除
     * @param id
     * @return
     */
    @PostMapping("/deletes")
	@ResponseBody
	public ResultInfo delete(String ids) {
		service.deleteByIds(ids);
		return ResultInfo.success();
	}
     
    
}
