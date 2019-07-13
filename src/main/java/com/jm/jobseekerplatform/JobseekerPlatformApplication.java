package com.jm.jobseekerplatform;

import com.jm.jobseekerplatform.config.InitData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JobseekerPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobseekerPlatformApplication.class, args);
    }

    //delete "//" to enable init userroles and users to base
    // @Bean(initMethod = "initData")
    public InitData initialData() {
        return new InitData();
    }

}
