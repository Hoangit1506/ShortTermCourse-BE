package com.short_term_course.service;

import com.short_term_course.dto.email.SendEmailDto;
import com.short_term_course.exception.AppException;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailService {
    @Value("${spring.mail.username}")
    String systemEmail;

    final JavaMailSender mailSender;

    public void sendEmail(SendEmailDto emailPayload) {
        var message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailPayload.getTo());
            helper.setSubject(emailPayload.getSubject());
            helper.setText(emailPayload.getText(), true);
            helper.setFrom(systemEmail);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.out.print(e.toString());
            throw new AppException(HttpStatus.BAD_REQUEST, "Failed to send email", "mail-e-01");
        }
    }

    public void sendEmailToVerifyRegister(String toEmail, String verificationCode) {
        String verifyUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/auth/register/verify/{verificationCode}")
                .buildAndExpand(verificationCode)
                .toUriString();
        String emailText = "Please click the link below to verify your email and complete the registration process:\n" + verifyUrl;
        SendEmailDto emailPayload = SendEmailDto.builder()
                .to(toEmail)
                .subject("Verity email to register")
                .text(emailText)
                .build();
        sendEmail(emailPayload);
    }

    public void sendEmailToWelcome(String toEmail) {
        String emailText = "Welcome to Short Term Course";
        SendEmailDto emailPayload = SendEmailDto.builder()
                .to(toEmail)
                .subject("Short Term Course welcome")
                .text(emailText)
                .build();
        sendEmail(emailPayload);
    }

    public void sendEmailToVerifyForgotPassword(String toEmail, String verificationCode) {
        String emailText = "Verify forgot password code:\n" + verificationCode;
        SendEmailDto emailPayload = SendEmailDto.builder()
                .to(toEmail)
                .subject("Verity to create new password")
                .text(emailText)
                .build();
        sendEmail(emailPayload);
    }
}
