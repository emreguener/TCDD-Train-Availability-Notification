package com.emreguener.TCDD_Ticket.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            System.out.println("📧 E-posta gönderme işlemi başladı...");
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("tcdddenemejava@gmail.com"); // Gönderici mail

            mailSender.send(message);
            System.out.println("✅ E-posta başarıyla gönderildi: " + to);
        } catch (Exception e) {
            System.err.println("❌ E-posta gönderme HATASI: " + e.getMessage());
        }
    }
}
