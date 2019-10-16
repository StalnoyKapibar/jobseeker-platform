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

    //подтверждение регистрации
    public void sendVerificationEmail(String address, String token) {
        String subject = "Подтвердите свой E-mail адрес и закончите регистрацию";
        token = "http://localhost:" + port + "/confirm_reg/" + token;
        final Context ctx = new Context();
        ctx.setVariable("name", address);
        ctx.setVariable("token", token);
        ctx.setVariable("subscriptionDate", new Date());
        sendEmail(address, subject, templateEngine.process("/emails/regemail.html", ctx));
    }

    public void sendFeedBackEMail(String address, String companyName, long employerProfileID) {
        String subject = "На ваше резюме отозвались!";
        String employerProfileLink = "http://localhost:" + port + "/employer/" + employerProfileID;
        String linkToChats = "http://localhost:" + port + "/seeker/chats";
        final Context ctx = new Context();
        ctx.setVariable("name", address);
        ctx.setVariable("employerProfileLink", employerProfileLink);
        ctx.setVariable("company", companyName);
        ctx.setVariable("linkToChat", linkToChats);
        sendEmail(address, subject, templateEngine.process("/emails/feedBackEmail.html", ctx));
    }

    // приглашение друга
    public void sendFriendInvitaionEmail(String address, String friendAddres) {
        String subject = address + " Приглашает вас попробовать нашу платформу";
        String href = "http://localhost:" + port + "/registration?email=" + friendAddres;
        final Context ctx = new Context();
        ctx.setVariable("inviter", address);
        ctx.setVariable("friend", friendAddres);
        ctx.setVariable("href", href);
        sendEmail(friendAddres, subject, templateEngine.process("/emails/friendInviteEmail.html", ctx));
    }

    // оповещение о добавлении
    public void sendNotificationAboutAddEmail(String address, String password) {
        String subject = address + "Приглашет вас попробовать нашу платформу";
        String href = "http://localhost:" + port + "/login";
        final Context ctx = new Context();
        ctx.setVariable("your_login", address);
        ctx.setVariable("password", password);
        ctx.setVariable("href", href);
        ctx.setVariable("subscriptionDate", new Date());
        sendEmail(address, subject, templateEngine.process("/emails/notificationEmail.html", ctx));
    }

    // востановление пароля
    public void sendRecoveryPassEmail(String address, String token) {
        String subject = "Вы сделали запрос на востановление пароля";
        String href = "http://localhost:" + port + "/password_reset/" + token;
        final Context ctx = new Context();
        ctx.setVariable("your_login", address);
        ctx.setVariable("href", href);
        ctx.setVariable("subscriptionDate", new Date());
        sendEmail(address, subject, templateEngine.process("/emails/recoveryPassEmail.html", ctx));
    }

	public void sendFeedBackEMailVacancy(String address, String seekerFullName, long seekerProfileID) {
		String subject = "На вашу вакансию отозвались!";
		String seekerProfileLink = "http://localhost:" + port + "/seeker/" + seekerProfileID;
		String linkToChats = "http://localhost:" + port + "/employer/chats";
		final Context ctx = new Context();
		ctx.setVariable("name", address);
		ctx.setVariable("seekerProfileLink", seekerProfileLink);
		ctx.setVariable("seekerFullName", seekerFullName);
		ctx.setVariable("linkToChat", linkToChats);
		sendEmail(address, subject, templateEngine.process("/emails/feedBackEmailVacancy.html", ctx));
	}

}
