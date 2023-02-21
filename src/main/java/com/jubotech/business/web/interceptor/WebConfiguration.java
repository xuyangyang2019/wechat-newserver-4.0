package com.jubotech.business.web.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.jubotech.framework.common.Constants;
@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {
	
    @Value("${com.jubotech.server.file.path}")
    private String filePath;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		System.out.println("进入拦截器。。。。");
//		InterceptorRegistration ir =  registry.addInterceptor(new TokenInterceptor());
//		ir.addPathPatterns("/**");
//		ir.excludePathPatterns("/","/cms/**");
		
		registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**").excludePathPatterns("/","/user/login","/user/keyWords/**","/user/device/**","/user/devicegroup/**"
				,"/user/resources/**","/user/sensitiveWords/**","/user/circle/**","/user/data/**","/user/circleComment/**","/user/circleLike/**","/pc/**","/fileUpload");
	    super.addInterceptors(registry);
	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	 registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    	 registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    	 registry.addResourceHandler("/"+Constants.FILE_PATH+"/**").addResourceLocations(filePath + Constants.FILE_PATH + "/");
         super.addResourceHandlers(registry);
    }
	
}
