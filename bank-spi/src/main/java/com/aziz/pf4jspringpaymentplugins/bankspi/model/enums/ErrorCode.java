package com.aziz.pf4jspringpaymentplugins.bankspi.model.enums;

public enum ErrorCode {
    INVALID_AMOUNT(400, "Amount must be greater than zero"),
    INVALID_CURRENCY(400, "Currency not supported"),
    MISSING_DESCRIPTION(400, "Payment description is required"),
    INVALID_PAYMENT_METHOD(400, "Payment method is invalid"),
    ACCOUNT_NOT_FOUND(404, "Account not found"),
    INSUFFICIENT_FUNDS(402, "Insufficient funds for the transaction"),
    INVALID_PAYLOAD(400, "Invalid payload");

    private final int statusCode;  // HTTP Status Code
    private final String message;  // Error message

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
