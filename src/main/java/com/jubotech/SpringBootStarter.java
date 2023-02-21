package com.jubotech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

import tk.mybatis.spring.annotation.MapperScan;

@EnableAsync //开启异步调用
@EnableCaching//开启缓存
@SpringBootApplication
//Spring Boot项目的核心注解，主要目的是开启自动配置，加了这个注解，后springboot会扫描本包及子包的所有javabean；
@MapperScan(basePackages = "com.jubotech.business.web.dao")
public class SpringBootStarter {
	public static void main(String[] args) {
		 //SpringBoot项目入口，该项目依赖微信开发SDK，咨询微信happybabby110
		 //个人微信聚合客服SCRM系统，http://www.wlkankan.cn/cate41/301.html
		 SpringApplication.run(SpringBootStarter.class, args);
	}
	

}
