package com.rodiugurlu.authservice.service.impl;

import com.rodiugurlu.authservice.service.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements IEmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String email, String verificationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Doğrulama Kodu");
            helper.setText("Doğrulama kodunuz: " + verificationCode);

            mailSender.send(message);
            System.out.println("Doğrulama kodu başarıyla gönderildi.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("E-posta gönderilemedi: " + e.getMessage());
        }
    }

    public boolean sendVerificationCode(String email, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body);

            mailSender.send(message);
            return true;
        } catch (MessagingException e) {
            System.out.println("E-posta gönderim hatası: {}"+ e.getMessage());
            return false;
        }
    }



}
