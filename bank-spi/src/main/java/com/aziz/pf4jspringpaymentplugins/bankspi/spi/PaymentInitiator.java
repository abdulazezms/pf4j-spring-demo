package com.aziz.pf4jspringpaymentplugins.bankspi.spi;

import com.aziz.pf4jspringpaymentplugins.bankspi.model.InitiatePaymentRequest;
import com.aziz.pf4jspringpaymentplugins.bankspi.model.InitiatePaymentResponse;
import org.pf4j.ExtensionPoint;

public interface PaymentInitiator extends ExtensionPoint {
    InitiatePaymentResponse initiatePayment(InitiatePaymentRequest request);
}
