package org.senju.eshopeule.exceptions.handler;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.google.cloud.storage.StorageException;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.expression.ExpressionException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            DataAccessException.class,
            ExpressionException.class,
            InvalidDefinitionException.class,
            HttpMessageNotReadableException.class,
            HttpRequestMethodNotSupportedException.class,
            StorageException.class
    })
    public ResponseEntity<? extends BaseResponse> handleBadRequest(Exception ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.badRequest().body(new SimpleResponse("Something went wrong!"));
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<?> handleNotFoundResponse(Exception ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.notFound().build();
    }
}
