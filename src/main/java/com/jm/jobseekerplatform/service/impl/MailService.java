package com.jm.jobseekerplatform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("mailService")
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String address, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(address);
        msg.setSubject(subject);
        msg.setText(text);
        javaMailSender.send(msg);
    }
}
