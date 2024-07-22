package org.senju.eshopeule.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.*;
import org.senju.eshopeule.dto.NotificationDTO;
import org.senju.eshopeule.exceptions.SendNotificationException;
import org.senju.eshopeule.service.NotificationService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import static org.senju.eshopeule.constant.enums.NotificationType.SendMethodType.EMAIL;
import static org.senju.eshopeule.constant.pattern.RegexPattern.EMAIL_PATTERN;

@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender mailSender;

    @Override
    public void sendNotification(NotificationDTO notification) throws SendNotificationException {
        if (notification.getType().getSendMethodType().equals(EMAIL)) {
            try {
                String recipient = (String) notification.getRecipient();
                if (recipient == null || recipient.isBlank()) throw new SendNotificationException("Recipient's email is not blank");
                checkValidEmail(recipient);
                String messageBody = (String) notification.getMessageBody();
                if (messageBody == null || messageBody.isBlank()) throw new SendNotificationException("Message body is not blank");
                String source = (String) notification.getSource();
                if (source == null || source.isEmpty()) source = "eshop27test@gmail.com";

                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setText(messageBody, true);
                messageHelper.setSubject(notification.getType().getContentType().getContentName());
                messageHelper.setTo(recipient);
                messageHelper.setFrom(source);
                mailSender.send(mimeMessage);

            } catch (ClassCastException e) {
                throw new SendNotificationException("Recipient or message format is invalid");
            } catch (MessagingException e) {
                throw new SendNotificationException(e.getMessage());
            }
        } else {
            throw new SendNotificationException("Only Email format is supported");
        }
    }

    private void checkValidEmail(String email) throws SendNotificationException{
        if (!email.matches(EMAIL_PATTERN))
            throw new SendNotificationException("Email is not valid");
    }
}