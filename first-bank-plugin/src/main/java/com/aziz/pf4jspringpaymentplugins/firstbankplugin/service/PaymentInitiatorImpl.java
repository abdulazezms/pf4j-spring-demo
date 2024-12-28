package com.aziz.pf4jspringpaymentplugins.firstbankplugin.service;

import com.aziz.pf4jspringpaymentplugins.bankspi.model.InitiatePaymentRequest;
import com.aziz.pf4jspringpaymentplugins.bankspi.model.InitiatePaymentResponse;
import com.aziz.pf4jspringpaymentplugins.bankspi.model.enums.PaymentStatus;
import com.aziz.pf4jspringpaymentplugins.bankspi.spi.PaymentInitiator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Extension
@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentInitiatorImpl implements PaymentInitiator {

    private final PaymentInitiatorValidator paymentInitiatorValidator;

    @Override
    public InitiatePaymentResponse initiatePayment(InitiatePaymentRequest request) {
        log.info("Validating payment request...");
        paymentInitiatorValidator.validatePaymentInitiationRequest(request);
        return InitiatePaymentResponse
                .builder()
                .paymentStatus(PaymentStatus.PENDING)
                .txId(UUID.randomUUID().toString())
                .build();
    }
}
