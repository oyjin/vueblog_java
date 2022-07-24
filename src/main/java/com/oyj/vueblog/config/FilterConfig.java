package com.oyj.vueblog.config;

import com.oyj.vueblog.common.exception.ExceptionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> @Title FilterConfig
 * <p> @Description 过滤器配置类
 *
 * @author a123
 * @date 2022/7/24 18:08
 */
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean exceptionFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ExceptionFilter());
        registration.setName("ExceptionFilter");
        //此处尽量小，要比其他Filter靠前
        registration.setOrder(-1);
        return registration;
    }
}

