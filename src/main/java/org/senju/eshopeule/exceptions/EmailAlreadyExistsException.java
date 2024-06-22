package org.senju.eshopeule.exceptions;

public class EmailAlreadyExistsException extends UserAlreadyExistsException {
    public EmailAlreadyExistsException() {super();}
    public EmailAlreadyExistsException(String message) {super(message);}
    public EmailAlreadyExistsException(String message, Throwable cause) {super(message, cause);}
    public EmailAlreadyExistsException(Throwable cause) {super(cause);}
}
