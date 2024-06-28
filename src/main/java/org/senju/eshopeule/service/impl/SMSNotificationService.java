package org.senju.eshopeule.service.impl;

import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.constant.enums.NotificationType;
import org.senju.eshopeule.dto.NotificationDTO;
import org.senju.eshopeule.exceptions.SendNotificationException;
import org.senju.eshopeule.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.senju.eshopeule.constant.pattern.RegexPattern.*;

@Service
@RequiredArgsConstructor
public class SMSNotificationService implements NotificationService {

    @Value("${twilio.account-sid}")
    private String twilioAccountSID;

    @Value("${twilio.auth-token}")
    private String twilioAuthToken;

    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;

    private static final Logger logger = LoggerFactory.getLogger(SMSNotificationService.class);


    @Override
    public void sendNotification(NotificationDTO notification) throws SendNotificationException {
        try {
            if (!notification.getType().getSendMethodType().equals(NotificationType.SendMethodType.SMS))
                throw new SendNotificationException("Only SMS format is supported");
            String recipientPhoneNumber = (String) notification.getRecipient();
            final String message = (String) notification.getMessageBody();
            checkValidPhoneNumber(recipientPhoneNumber);

            Twilio.init(twilioAccountSID, twilioAuthToken);
            recipientPhoneNumber = "+84" + recipientPhoneNumber.substring(1);
            final PhoneNumber to = new PhoneNumber(recipientPhoneNumber);
            final PhoneNumber from = new PhoneNumber(twilioPhoneNumber);
            final MessageCreator creator = Message.creator(to, from, message);
            creator.create();

        } catch (ClassCastException ex) {
            throw new SendNotificationException("Recipient or message format is invalid");
        } catch (TwilioException ex) {
            throw new SendNotificationException(ex.getMessage());
        }
    }

    private void  checkValidPhoneNumber(String phoneNumber) throws SendNotificationException{
        if (!phoneNumber.matches(PHONE_PATTERN)) throw new SendNotificationException("Phone number is invalid");
    }
}
