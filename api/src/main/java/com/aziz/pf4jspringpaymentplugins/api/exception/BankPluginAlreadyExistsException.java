package com.aziz.pf4jspringpaymentplugins.api.exception;

public class BankPluginAlreadyExistsException extends RuntimeException {
    public BankPluginAlreadyExistsException(String message) {
        super(message);
    }
}
