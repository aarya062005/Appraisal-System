package com.project.AppraisalSystem.service;

public interface EmailService {
    void sendNotificationEmail(String title, String message);
    void sendWelcomeEmail(String toEmail, String firstName, String tempPassword);
}