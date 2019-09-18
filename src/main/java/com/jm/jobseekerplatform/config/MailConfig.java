package com.jm.jobseekerplatform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:config/mail.properties")
public class MailConfig {

    @Autowired
    private Environment env;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getRequiredProperty("spring.mail.host"));
        mailSender.setPort(587);

        mailSender.setUsername(env.getRequiredProperty("spring.mail.username"));
        mailSender.setPassword(env.getRequiredProperty("spring.mail.password"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", env.getRequiredProperty("spring.mail.properties.mail.transport.protocol"));
        props.put("mail.smtp.auth", env.getRequiredProperty("spring.mail.properties.mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", env.getRequiredProperty("spring.mail.properties.mail.smtp.starttls.enable"));
        props.put("mail.smtp.ssl.trust", env.getRequiredProperty("spring.mail.properties.mail.smtp.ssl.trust"));
        props.put("mail.smtp.connectiontimeout", env.getRequiredProperty("spring.mail.properties.mail.smtp.connectiontimeout"));
        props.put("mail.smtp.timeout", env.getRequiredProperty("spring.mail.properties.mail.smtp.timeout"));
        props.put("mail.smtp.writetimeout", env.getRequiredProperty("spring.mail.properties.mail.smtp.writetimeout"));
        props.put("mail.debug", "true");

        return mailSender;
    }
}
