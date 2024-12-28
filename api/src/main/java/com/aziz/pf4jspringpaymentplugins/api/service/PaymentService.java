package com.aziz.pf4jspringpaymentplugins.api.service;

import com.aziz.pf4jspringpaymentplugins.api.plugin.BankPluginManager;
import com.aziz.pf4jspringpaymentplugins.bankspi.model.InitiatePaymentRequest;
import com.aziz.pf4jspringpaymentplugins.bankspi.model.InitiatePaymentResponse;
import com.aziz.pf4jspringpaymentplugins.bankspi.spi.PaymentInitiator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final BankPluginManager bankPluginManager;

    public InitiatePaymentResponse initiatePayment(String bankId, InitiatePaymentRequest request) {
        PaymentInitiator bankPaymentInitiator =
                bankPluginManager.getBankPluginImplementation(bankId, PaymentInitiator.class);
        return bankPaymentInitiator.initiatePayment(request);
    }
}
