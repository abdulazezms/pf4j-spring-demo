package com.aziz.pf4jspringpaymentplugins.api.exception;

import com.aziz.pf4jspringpaymentplugins.api.exception.response.ErrorResponse;
import com.aziz.pf4jspringpaymentplugins.bankspi.exception.InvalidPaymentRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String GENERIC_CODE = "GENERIC_CODE";

    @ExceptionHandler(ExtensionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleExtensionNotFoundException(RuntimeException ex) {
        return new ResponseEntity<>(getErrorResponse(Optional.empty(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnsupportedBankException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedBankException(RuntimeException ex) {
        return new ResponseEntity<>(getErrorResponse(Optional.empty(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MoreThanOneExtensionFound.class)
    public ResponseEntity<ErrorResponse> handleMoreThanOneExtensionFoundException(RuntimeException ex) {
        return new ResponseEntity<>(getErrorResponse(Optional.empty(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidPaymentRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPaymentRequestException(InvalidPaymentRequestException ex) {
        return new ResponseEntity<>(
                getErrorResponse(Optional.of(ex.getErrorCode().name()), ex.getMessage()),
                HttpStatusCode.valueOf(ex.getErrorCode().getStatusCode())
        );
    }

    @ExceptionHandler(BankPluginAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleBankPluginAlreadyExistsException(RuntimeException ex) {
        return new ResponseEntity<>(getErrorResponse(Optional.empty(), ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnsupportedPluginFileException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedPluginFileException(RuntimeException ex) {
        return new ResponseEntity<>(getErrorResponse(Optional.empty(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PluginJarProcessingException.class)
    public ResponseEntity<ErrorResponse> handlePluginJarProcessingException(RuntimeException ex) {
        return new ResponseEntity<>(getErrorResponse(Optional.empty(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
                getErrorResponse(Optional.empty(), "Malformed JSON request or invalid data format"),
                HttpStatus.BAD_REQUEST
        );
    }

    private ErrorResponse getErrorResponse(Optional<String> errorCodeOptional, String message) {
        return new ErrorResponse(errorCodeOptional.orElse(GENERIC_CODE), message);
    }
}
