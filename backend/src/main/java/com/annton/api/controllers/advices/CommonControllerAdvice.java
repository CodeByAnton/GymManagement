package com.annton.api.controllers.advices;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.annton.api.dto.OperationInfo;
import com.annton.api.services.exceptions.NotFound;
import com.annton.api.services.exceptions.ProhibitedOperationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class CommonControllerAdvice {

    private static final Logger logger = LogManager.getLogger(CommonControllerAdvice.class);

    @ExceptionHandler({JWTVerificationException.class})
    private ResponseEntity<OperationInfo> unauthorized(Exception exception) {
        logger.error(exception.getMessage());
        logFinishRequestExecutionWithError();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(OperationInfo.builder().success(false)
                        .description(exception.getMessage()).build());
    }

    @ExceptionHandler({BadCredentialsException.class, ProhibitedOperationException.class, NotFound.class, JsonProcessingException.class})
    private ResponseEntity<OperationInfo> badCredentials(Exception exception) {
        logger.error(exception.getMessage());
        logFinishRequestExecutionWithError();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(OperationInfo.builder().success(false)
                        .description(exception.getMessage()).build());
    }

    @ExceptionHandler({RuntimeException.class, MessagingException.class})
    private ResponseEntity<OperationInfo> internalServerError(RuntimeException exception) {
        logger.error(exception.getMessage());
        exception.printStackTrace();
        logFinishRequestExecutionWithError();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(OperationInfo.builder().success(false)
                        .description(exception.getMessage()).build());
    }

    private static void logFinishRequestExecutionWithError(){
        logger.error("--- Finish request processing with error ---");
    }


}
