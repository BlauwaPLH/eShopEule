package org.senju.eshopeule.exceptions;

public class UsernameAlreadyExistsException extends UserAlreadyExistsException {
    public UsernameAlreadyExistsException() {super();}
    public UsernameAlreadyExistsException(String message) {super(message);}
    public UsernameAlreadyExistsException(String message, Throwable cause) {super(message, cause);}
    public UsernameAlreadyExistsException(Throwable cause) {super(cause);}
}
