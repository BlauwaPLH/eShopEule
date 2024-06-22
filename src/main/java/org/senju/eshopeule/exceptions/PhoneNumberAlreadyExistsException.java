package org.senju.eshopeule.exceptions;

public class PhoneNumberAlreadyExistsException extends UserAlreadyExistsException {
    public PhoneNumberAlreadyExistsException() {super();}
    public PhoneNumberAlreadyExistsException(String message) {super(message);}
    public PhoneNumberAlreadyExistsException(String message, Throwable cause) {super(message, cause);}
    public PhoneNumberAlreadyExistsException(Throwable cause) {super(cause);}
}
