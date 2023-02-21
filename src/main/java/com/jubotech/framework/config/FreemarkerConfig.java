package com.jubotech.framework.config;
 
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import freemarker.template.TemplateModelException;


//@Configuration
public class FreemarkerConfig {

    @Autowired
    private freemarker.template.Configuration configuration;

    @PostConstruct
    public void setSharedVariable() throws TemplateModelException {
        // 取消默认数字格式化
        configuration.setNumberFormat("#");
    }

}
