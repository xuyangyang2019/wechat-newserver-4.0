package com.jubotech.business.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.VersionControl;
import com.jubotech.business.web.query.VersionControlQuery;
import com.jubotech.business.web.service.VersionControlService;
import com.jubotech.framework.common.GridPage;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/user/version")
public class VersionController {
	 
	@Autowired
	private VersionControlService service;
	   
    @PostMapping("/pageList")
	@ResponseBody
	public ResultInfo pageList(VersionControlQuery query) {
		GridPage<VersionControl> gridPage = new GridPage<>(service.pageList(query));
		return ResultInfo.success(gridPage);
	}
      
    /**
     * 信息修改
     * @param info
     * @return
     */
    @ResponseBody
    @PostMapping("/update" )
    public ResultInfo update(VersionControl info){
    	info.setFlag(-1);
    	return service.update(info);
    }
    
    /**
     * 信息添加
     * @param info
     * @return
     */
    @ResponseBody
    @PostMapping("/add")
    public ResultInfo add(VersionControl info){
    	info.setFlag(-1);
    	return service.insert(info);
    }
    
    /**
     * 信息删除
     * @param ids
     * @return
     */
    @PostMapping("/deletes")
    @ResponseBody
    public ResultInfo deletes(String ids){
    	return service.deleteByIds(ids);
    }
    
    /**
     * 推送更新
     * @param id
     * @return
     */
    @PostMapping("/push")
    @ResponseBody
    public ResultInfo push(Integer id){
		service.pushAppUpdate(id);
		return ResultInfo.success();
    }
      
    
}
