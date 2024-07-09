package org.senju.eshopeule.exceptions;

public class ChangePasswordException extends RuntimeException {
    public ChangePasswordException() {super();}
    public ChangePasswordException(String message) {super(message);}
    public ChangePasswordException(String message, Throwable cause) {super(message, cause);}
    public ChangePasswordException(Throwable cause) {super(cause);}
}
