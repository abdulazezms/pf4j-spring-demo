package com.aziz.pf4jspringpaymentplugins.api.exception;

public class UnsupportedBankException extends RuntimeException {
    public UnsupportedBankException(String message) {
        super(message);
    }
}
