package com.jm.jobseekerplatform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;

@Service("mailService")
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${server.port}")
    String port;

    public void sendEmail(String address, String subject, String text) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(address);
            helper.setSubject(subject);
            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(text, "text/html; charset=utf-8");
            mp.addBodyPart(htmlPart);
            msg.setContent(mp);
            javaMailSender.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendVerificationEmail(String address, String token) {
        String subject = "Подтвердите свой E-mail адрес и закончите регистрацию";
        token = "http://localhost:"+ port + "/confirm_reg/" + token;
        final Context ctx = new Context();
        ctx.setVariable("name", address);
        ctx.setVariable("token", token);
        ctx.setVariable("subscriptionDate", new Date());
        sendEmail(address, subject, templateEngine.process("/emails/regemail.html", ctx));
    }

}
