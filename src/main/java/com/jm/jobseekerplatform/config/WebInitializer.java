/*
 * Copyright (c) 2019. by ASD
 */

package com.jm.jobseekerplatform.config;

import com.jm.jobseekerplatform.security.XSSFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WebInitializer {

    @Bean
    public FilterRegistrationBean<Filter> xssFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XSSFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }
}
