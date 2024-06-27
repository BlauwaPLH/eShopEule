package org.senju.eshopeule.dto;

import lombok.*;
import org.senju.eshopeule.constant.enums.NotificationType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public final class NotificationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6843112309116436556L;

    private NotificationType type;
    private Object messageBody;
    private Object recipient;
    private Object source;
    private long timeStamp;

    public NotificationDTO(NotificationType type, Object messageBody, Object recipient) {
        this(type, messageBody, recipient, null);
    }

    public NotificationDTO(NotificationType type, Object messageBody, Object recipient, Object source) {
        this.type = type;
        this.source = source;
        this.recipient = recipient;
        this.messageBody = messageBody;
        this.timeStamp = (new Date()).getTime();
    }

    public NotificationDTO(NotificationType type, Object messageBody, Object recipient, long timeStamp) {
        this(type, messageBody, recipient, null, timeStamp);
    }

    public NotificationDTO(NotificationType type, Object messageBody, Object recipient, Object source, long timeStamp) {
        this.type = type;
        this.messageBody = messageBody;
        this.recipient = recipient;
        this.source = source;
        this.timeStamp = timeStamp;
    }
}
