package com.project.AppraisalSystem.service.implementation;

import com.project.AppraisalSystem.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final String notificationEmail;

    public EmailServiceImpl(JavaMailSender mailSender,
                            @Value("${app.notification.email}") String notificationEmail) {
        this.mailSender = mailSender;
        this.notificationEmail = notificationEmail;
    }

    @Override
    public void sendNotificationEmail(String title, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(notificationEmail);
        mail.setSubject("AppraisalPro: " + title);
        mail.setText(message);
        mailSender.send(mail);
    }

    @Override
    public void sendWelcomeEmail(String toEmail, String firstName, String tempPassword) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(toEmail);
        mail.setSubject("Welcome to AppraisalPro — Your Account Details");
        mail.setText(
                "Hi " + firstName + ",\n\n" +
                        "An account has been created for you on AppraisalPro.\n\n" +
                        "Login email: " + toEmail + "\n" +
                        "Temporary password: " + tempPassword + "\n\n" +
                        "Please log in and change your password as soon as possible.\n\n" +
                        "— AppraisalPro Team"
        );
        mailSender.send(mail);
    }
}