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

    public void sendVerificationEmail(String address, String token){
        String subject = "Confirm your E-mail address and complete registration";
        String text = "Вы зарегистрировались на платформе JobSekeer. Для подтверждения адреса электронной почты пройдите по ссылке (действительна в течении суток):\n"
                +"http://localhost:7070/confirm_reg/" + token;
        sendEmail(address, subject, text);
    }
}
