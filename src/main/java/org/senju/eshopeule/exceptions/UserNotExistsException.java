package org.senju.eshopeule.exceptions;

public class UserNotExistsException extends Exception{
    public UserNotExistsException() {super();}
    public UserNotExistsException(String message) {super(message);}
    public UserNotExistsException(String message, Throwable cause) {super(message, cause);}
    public UserNotExistsException(Throwable cause) {super(cause);}
}
