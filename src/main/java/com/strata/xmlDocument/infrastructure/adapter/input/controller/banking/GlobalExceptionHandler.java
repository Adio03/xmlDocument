//package com.strata.xmlDocument.infrastructure.adapter.input.controller.banking;
//
//import com.strata.xmlDocument.application.input.banking.UserManagementUseCase.*;
//import com.strata.xmlDocument.application.input.banking.AccountManagementUseCase.*;
//import com.strata.xmlDocument.application.input.banking.TransactionProcessingUseCase.*;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.time.LocalDateTime;
//
///**
// * Global exception handler for the banking application
// * Provides consistent error responses across all banking endpoints
// */
//@RestControllerAdvice(basePackages = "com.strata.xmlDocument.infrastructure.adapter.input.controller.banking")
//@Slf4j
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(UserAlreadyExistsException.class)
//    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
//        log.error("User already exists: {}", ex.getMessage());
//
//        ErrorResponse error = new ErrorResponse(
//            "USER_ALREADY_EXISTS",
//            ex.getMessage(),
//            HttpStatus.CONFLICT.value(),
//            LocalDateTime.now()
//        );
//
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
//    }
//
//    @ExceptionHandler(InvalidCredentialsException.class)
//    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
//        log.error("Invalid credentials: {}", ex.getMessage());
//
//        ErrorResponse error = new ErrorResponse(
//            "INVALID_CREDENTIALS",
//            ex.getMessage(),
//            HttpStatus.UNAUTHORIZED.value(),
//            LocalDateTime.now()
//        );
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
//    }
//
//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
//        log.error("User not found: {}", ex.getMessage());
//
//        ErrorResponse error = new ErrorResponse(
//            "USER_NOT_FOUND",
//            ex.getMessage(),
//            HttpStatus.NOT_FOUND.value(),
//            LocalDateTime.now()
//        );
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
//    }
//
//    @ExceptionHandler(AccountNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleAccountNotFound(AccountNotFoundException ex) {
//        log.error("Account not found: {}", ex.getMessage());
//
//        ErrorResponse error = new ErrorResponse(
//            "ACCOUNT_NOT_FOUND",
//            ex.getMessage(),
//            HttpStatus.NOT_FOUND.value(),
//            LocalDateTime.now()
//        );
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
//    }
//
//    @ExceptionHandler(InsufficientFundsException.class)
//    public ResponseEntity<ErrorResponse> handleInsufficientFunds(InsufficientFundsException ex) {
//        log.error("Insufficient funds: {}", ex.getMessage());
//
//        ErrorResponse error = new ErrorResponse(
//            "INSUFFICIENT_FUNDS",
//            ex.getMessage(),
//            HttpStatus.BAD_REQUEST.value(),
//            LocalDateTime.now()
//        );
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
//        log.error("Invalid argument: {}", ex.getMessage());
//
//        ErrorResponse error = new ErrorResponse(
//            "INVALID_ARGUMENT",
//            ex.getMessage(),
//            HttpStatus.BAD_REQUEST.value(),
//            LocalDateTime.now()
//        );
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
//        log.error("Validation error: {}", ex.getMessage());
//
//        String message = ex.getBindingResult().getFieldErrors().stream()
//            .map(error -> error.getField() + ": " + error.getDefaultMessage())
//            .reduce("", (acc, error) -> acc.isEmpty() ? error : acc + ", " + error);
//
//        ErrorResponse error = new ErrorResponse(
//            "VALIDATION_ERROR",
//            message,
//            HttpStatus.BAD_REQUEST.value(),
//            LocalDateTime.now()
//        );
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
//        log.error("Unexpected error in banking module: ", ex);
//
//        ErrorResponse error = new ErrorResponse(
//            "INTERNAL_SERVER_ERROR",
//            "An unexpected error occurred in the banking system",
//            HttpStatus.INTERNAL_SERVER_ERROR.value(),
//            LocalDateTime.now()
//        );
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }
//
//    // Error response DTO
//    public record ErrorResponse(
//            String errorCode,
//            String message,
//            int statusCode,
//            LocalDateTime timestamp
//    ) {}
//}