package com.jm.jobseekerplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Set 'com.jm.jobseekerplatform.init' property to 'true' in 'application.properties' for database init.
 * Then set to 'false' after successful start.
 */
@SpringBootApplication
public class JobseekerPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobseekerPlatformApplication.class, args);
    }

}
