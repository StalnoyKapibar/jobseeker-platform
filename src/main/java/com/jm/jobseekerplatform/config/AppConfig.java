package com.jm.jobseekerplatform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({
        "classpath:config/datasource.properties",
        "classpath:config/jpa.properties",
        "classpath:config/mail.properties",
        "classpath:config/cache.properties"
})
public class AppConfig {
}
