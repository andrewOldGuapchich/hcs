package com.andrew.hcsservice.service.addit_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${smtp.mail.name}")
    private String fromAddress;
    //@Value("${auth.config.url}")
    //private String url;

    public void sendEmail(String toAddress, String url) {
        String htmlContent = "<p>Для прохождения процесса регистрации перейдите по ссылке - " +
                "<a href=\"" + url + "\">регистрация</a></p>";
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(fromAddress);
            helper.setTo(toAddress);
            helper.setSubject("ЖКХ регистрация");
            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();

        }

        /*String text = "Для прохождения процесса регистрации перейдите по ссылке - " +
                url + toAddress;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromAddress);
        mailMessage.setTo(toAddress);
        mailMessage.setSubject("ЖКХ регистрация");
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);*/
    }
}
