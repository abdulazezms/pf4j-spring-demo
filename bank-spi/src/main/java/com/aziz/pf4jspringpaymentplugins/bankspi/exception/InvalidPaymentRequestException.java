package com.aziz.pf4jspringpaymentplugins.bankspi.exception;

import com.aziz.pf4jspringpaymentplugins.bankspi.model.enums.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidPaymentRequestException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidPaymentRequestException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
