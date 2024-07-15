package org.senju.eshopeule.exceptions;

public class RatingException extends RuntimeException {
    public RatingException() {super();}
    public RatingException(String message) {super(message);}
    public RatingException(String message, Throwable cause) {super(message, cause);}
    public RatingException(Throwable cause) {super(cause);}
}
