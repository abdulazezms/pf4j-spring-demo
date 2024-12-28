package com.aziz.pf4jspringpaymentplugins.bankspi.model;

import com.aziz.pf4jspringpaymentplugins.bankspi.model.enums.PaymentStatus;
import lombok.Builder;

/**
 * DTO for initiating a payment request.
 */
@Builder
public record InitiatePaymentResponse(String txId, PaymentStatus paymentStatus) {
}