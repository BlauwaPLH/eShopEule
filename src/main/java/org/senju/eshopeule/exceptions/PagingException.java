package org.senju.eshopeule.exceptions;

public class PagingException extends RuntimeException {
    public PagingException() {super();}
    public PagingException(String message) {super(message);}
    public PagingException(Throwable cause) {super(cause);}
    public PagingException(String message, Throwable cause) {super(message, cause);}
}
