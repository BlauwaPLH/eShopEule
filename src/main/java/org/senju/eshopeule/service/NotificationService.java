package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.NotificationDTO;
import org.senju.eshopeule.exceptions.SendNotificationException;

public interface NotificationService {

    void sendNotification(NotificationDTO notification) throws SendNotificationException;
}
