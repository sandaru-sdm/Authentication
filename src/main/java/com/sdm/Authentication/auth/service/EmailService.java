package com.sdm.Authentication.auth.service;

import com.sdm.Authentication.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${app.base.url}")
    private String BASE_URL = "";

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendActivationEmail(String toEmail, String activationCode) {
        String activationLink = BASE_URL+"activate?code=" + activationCode;
        String subject = "Account Activation";
        String text = "Please activate your account using the following link: " + activationLink;

        sendEmail(toEmail, subject, text);

//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(toEmail);
//        message.setSubject("Account Activation");
//        message.setText("Please activate your account using the following link: " + activationLink);
//
//        mailSender.send(message);
    }

    public void sendPasswordResetEmail(User user, String token) {
        String resetUrl = BASE_URL+"reset-password?token=" + token;
        String subject = "Password Reset Request";
        String text = "To reset your password, click the following link: " + resetUrl;

        sendEmail(user.getEmail(), subject, text);

//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(user.getEmail());
//        message.setSubject("Password Reset Request");
//        message.setText("To reset your password, click the following link: " + resetUrl);
//
//        mailSender.send(message);

    }

    public void sendEmail(String email, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}

