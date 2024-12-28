package com.aziz.pf4jspringpaymentplugins.bankspi.model;

import com.aziz.pf4jspringpaymentplugins.bankspi.model.enums.Currency;
import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record InitiatePaymentRequest(String accountNumber, BigDecimal amount, Currency currency) {
}