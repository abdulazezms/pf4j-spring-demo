package com.aziz.pf4jspringpaymentplugins.api.exception;

public class MoreThanOneExtensionFound extends RuntimeException {
    public MoreThanOneExtensionFound(String message) {
        super(message);
    }
}
