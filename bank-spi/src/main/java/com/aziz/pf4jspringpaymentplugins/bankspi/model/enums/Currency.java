package com.aziz.pf4jspringpaymentplugins.bankspi.model.enums;

public enum Currency {

    USD("USD"),
    EUR("EUR"),
    SAR("SAR"),
    INR("INR");

    private final String code;

    Currency(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }
}