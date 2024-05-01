package com.andrew.hcsservice.service.addit_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${smtp.mail.name}")
    private String fromAddress;
    @Value("${auth.config.url}")
    private String url;

    public void sendEmail(String toAddress, String subject) {
        String text = "Для прохождения процесса регистрации перейдите по ссылке - " +
                url + toAddress;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromAddress);
        mailMessage.setTo(toAddress);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);
    }
}
