package org.senju.eshopeule.exceptions;

public class SendNotificationException extends RuntimeException {
    public SendNotificationException() {super();}
    public SendNotificationException(String message) {super(message);}
    public SendNotificationException(Throwable cause) {super(cause);}
    public SendNotificationException(String message, Throwable cause) {super(message, cause);}
}
