package com.aziz.pf4jspringpaymentplugins.bankspi.model.enums;

public enum PaymentStatus {

    SUCCESS("SUCCESS"),
    PENDING("PENDING"),
    FAILED("FAILED"),
    CANCELLED("CANCELLED");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
