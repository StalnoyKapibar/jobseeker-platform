package com.jm.jobseekerplatform.config;

import com.jm.jobseekerplatform.annotation.processor.AccessCheckAnnotationBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({
        "classpath:config/datasource.properties",
        "classpath:config/jpa.properties",
        "classpath:config/mail.properties",
        "classpath:config/cache.properties"
})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AppConfig {

    /**
     * Set 'com.jm.jobseekerplatform.init' property to 'true' in 'application.properties' for database init.
     * Then set to 'false' after successful start.
     */
    @Bean
    @ConditionalOnProperty(value = "com.jm.jobseekerplatform.init", havingValue = "true")
    public InitData initData() {
        return new InitData();
    }

    @Bean
    @ConditionalOnProperty(value = "com.jm.jobseekerplatform.init", havingValue = "false")
    public AccessCheckAnnotationBeanPostProcessor accessCheckAnnotationBeanPostProcessor() {
        return new AccessCheckAnnotationBeanPostProcessor();
    }

}
