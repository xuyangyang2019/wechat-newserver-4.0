package com.jubotech.business.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jubotech.framework.common.Constants;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// System.out.println("----afterCompletion在页面渲染之后被调用--好像没啥鸟用--");
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2,ModelAndView modelAndView) throws Exception {
		// System.out.println("----postHandle在controller执行之后切页面渲染之前被调用----");
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		// System.out.println("-----preHandle在controller执行之前被调用------");
		String token = request.getHeader("token");
        log.info("token : [ {} ]", token);
		if(null == token || !token.equals(Constants.TOKEN)){
			return false;
		}
		return true;
	}
}
