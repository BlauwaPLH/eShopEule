package org.senju.eshopeule.exceptions.handlers;

import org.senju.eshopeule.exceptions.JwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler({JwtAuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(JwtAuthenticationException exception) {
        ProblemDetail body = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED.value());
        body.setProperty("error", exception.getMessage());
        return ResponseEntity.status(401).body(new ErrorResponseException(HttpStatusCode.valueOf(401), body, null));
    }
}
