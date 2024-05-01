package com.andrew.authserver.service.add_serv

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
open class EmailService(
        @Value("\${smtp.mail.name}") private val name: String,
        private val mailSender: JavaMailSender
        ) {

    open fun sendEmail(toAddr: String, subject: String, text: String) {
        val message = SimpleMailMessage().apply {
            this.from = name
            this.setTo(toAddr)
            this.subject = subject
            this.text = text
        }

        mailSender.send(message);
    }
}