package org.senju.eshopeule.exceptions;

public class RoleAlreadyExistsException extends Exception {
    public RoleAlreadyExistsException() {super();}
    public RoleAlreadyExistsException(String message) {super(message);}
    public RoleAlreadyExistsException(String message, Throwable cause) {super(message, cause);}
    public RoleAlreadyExistsException(Throwable cause) {super(cause);}
}
