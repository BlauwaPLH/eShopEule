package org.senju.eshopeule.utils;

import org.senju.eshopeule.constant.enums.NotificationType;
import org.senju.eshopeule.dto.NotificationDTO;

import static org.senju.eshopeule.constant.enums.NotificationType.SendMethodType.*;

public class MessageUtil {

    public static NotificationDTO buildMessage(NotificationDTO notification) {
        final NotificationType notificationType = notification.getType();
        final var sendMethodType = notificationType.getSendMethodType();
        final var contentType = notificationType.getContentType();

        if (sendMethodType.equals(EMAIL)) {
            switch (contentType) {
                case MFA -> {return buildMfaEmail(notification);}
                case FORGOT_PASSWORD -> {return buildResetPasswordEmail(notification);}
                case VERIFY_SIGNUP -> {return buildVerifySignUpEmail(notification);}
            }
        } else if (sendMethodType.equals(SMS)) {
            switch (contentType) {
                case MFA -> {return buildMfaSMS(notification);}
                case VERIFY_SIGNUP -> {return buildVerifySignUpSMS(notification);}
                default -> {return null;}
            }
        }
        return null;
    }

    private static NotificationDTO buildResetPasswordEmail(NotificationDTO notification) {
        notification.setMessageBody("build a simple reset password email");
        return notification;
    }

    private static NotificationDTO buildVerifySignUpEmail(NotificationDTO notification) {
        notification.setMessageBody("build a simple verify sign up email");
        return notification;
    }

    private static NotificationDTO buildVerifySignUpSMS(NotificationDTO notification) {
        notification.setMessageBody("iuewfhwe");
        return notification;
    }

    private static NotificationDTO buildMfaEmail(NotificationDTO notification) {
        notification.setMessageBody("build a simple mfa email");
        return notification;
    }

    private static NotificationDTO buildMfaSMS(NotificationDTO notification) {
        notification.setMessageBody("build a simple mfa sms");
        return notification;
    }
}
