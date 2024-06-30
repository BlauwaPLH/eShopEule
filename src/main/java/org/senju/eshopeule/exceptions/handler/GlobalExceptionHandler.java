package org.senju.eshopeule.exceptions.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handle(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            if (err instanceof FieldError) {
                errors.put(
                        ((FieldError) err).getField(),
                        err.getDefaultMessage()
                );
            } else {
                errors.put("error", err.getDefaultMessage());
            }
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            DataAccessException.class,
            ExpressionException.class
    })
    public ResponseEntity<Map<String, String>> handle(Exception ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Something went wrong!"));
    }
}
