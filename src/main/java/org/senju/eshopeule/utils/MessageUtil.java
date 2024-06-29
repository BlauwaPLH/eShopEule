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
                case FORGOT_PASSWORD -> {return buildResetPasswordEmail(notification);}
                case VERIFY_SIGNUP -> {return buildVerifySignUpEmail(notification);}
            }
        } else if (sendMethodType.equals(SMS)) {
            switch (contentType) {
                case VERIFY_SIGNUP -> {return buildVerifySignUpSMS(notification);}
                case FORGOT_PASSWORD -> {return null;}
            }
        }
        return null;
    }

    private static NotificationDTO buildResetPasswordEmail(NotificationDTO notification) {
        final String newPassword = (String) notification.getMessageBody();
        notification.setMessageBody(
                "<div style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; background-color: #f4f4f4; width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border: 1px solid #dddddd;\">\n" +
                        "    <div style=\"text-align: center; padding-bottom: 20px;\">\n" +
                        "        <h1 style=\"margin: 0;\">Your New Password</h1>\n" +
                        "    </div>\n" +
                        "    <div style=\"margin-bottom: 20px;\">\n" +
                        "        <p>Hi [User],</p>\n" +
                        "        <p>Your password has been successfully reset. Your new password is: </p>\n" +
                        "        <p style=\"font-size: 20px; font-weight: bold; color: #007bff;\">" + newPassword + "</p>\n" +
                        "        <p>Please use this new password to log in to your account. For security reasons, we recommend that you change this password after logging in.</p>\n" +
                        "        <p>If you did not request a password reset, please contact our support team immediately.</p>\n" +
                        "        <p>Thank you,<br>The [Your Company] Team</p>\n" +
                        "    </div>\n" +
                        "    <div style=\"text-align: center; font-size: 12px; color: #777; padding-top: 20px; border-top: 1px solid #dddddd;\">\n" +
                        "        <p>If you need assistance, please contact our support team at <a href=\"mailto:eshop27test@gmail.com\" style=\"color: #007bff;\">eshop27test@gmail.com</a>.</p>\n" +
                        "    </div>\n" +
                        "</div>\n"
        );
        return notification;
    }


    private static NotificationDTO buildVerifySignUpEmail(NotificationDTO notification) {
        final String recipientName = (String) notification.getRecipient();
        final String verifyCode = (String) notification.getMessageBody();
        notification.setMessageBody(
                "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                        "\n" +
                        "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                        "\n" +
                        "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                        "        \n" +
                        "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                        "          <tbody><tr>\n" +
                        "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                        "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                    <td style=\"padding-left:10px\">\n" +
                        "                  \n" +
                        "                    </td>\n" +
                        "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                        "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                        "                    </td>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "              </a>\n" +
                        "            </td>\n" +
                        "          </tr>\n" +
                        "        </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                        "      <td>\n" +
                        "        \n" +
                        "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                        "        \n" +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + recipientName + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Continue signing up for eShop27 by entering the code below: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" + verifyCode + "</p></blockquote>\n Code will expire in 5 minutes. <p>See you soon</p>" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                        "\n" +
                        "</div></div>"
        );
        return notification;
    }

    private static NotificationDTO buildVerifySignUpSMS(NotificationDTO notification) {
        notification.setMessageBody("Demo");
        return notification;
    }

    private static NotificationDTO buildResetPasswordSMS(NotificationDTO notification) {
        notification.setMessageBody("Demo");
        return notification;
    }
}
