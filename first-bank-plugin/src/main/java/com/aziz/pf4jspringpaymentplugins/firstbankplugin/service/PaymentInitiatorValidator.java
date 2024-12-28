package com.aziz.pf4jspringpaymentplugins.firstbankplugin.service;

import com.aziz.pf4jspringpaymentplugins.bankspi.exception.InvalidPaymentRequestException;
import com.aziz.pf4jspringpaymentplugins.bankspi.model.InitiatePaymentRequest;
import com.aziz.pf4jspringpaymentplugins.bankspi.model.enums.Currency;
import com.aziz.pf4jspringpaymentplugins.bankspi.model.enums.ErrorCode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
public class PaymentInitiatorValidator {

    private static final Set<String> validAccountNumbers;
    private static final Set<Currency> validCurrencies;


    static {
        validAccountNumbers = new HashSet<>(Set.of("1", "2", "3"));
        validCurrencies = new HashSet<>(Set.of(Currency.SAR, Currency.INR));
    }

    public void validatePaymentInitiationRequest(InitiatePaymentRequest initiatePaymentRequest) {
        if(initiatePaymentRequest == null) {
            throw new InvalidPaymentRequestException(ErrorCode.INVALID_PAYLOAD, "Payload cannot be null");
        }
        if(initiatePaymentRequest.amount() == null) {
            throw new InvalidPaymentRequestException(ErrorCode.INVALID_PAYLOAD, "Amount cannot be null");
        }
        if(initiatePaymentRequest.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentRequestException(ErrorCode.INVALID_AMOUNT, "Amount cannot be less than or equal to zero");
        }
        if(initiatePaymentRequest.currency() == null) {
            throw new InvalidPaymentRequestException(ErrorCode.INVALID_CURRENCY, "Currency cannot be null");
        }
        if(initiatePaymentRequest.accountNumber() == null) {
            throw new InvalidPaymentRequestException(ErrorCode.INVALID_PAYLOAD, "AccountNumber cannot be null");
        }
        if(!validCurrencies.contains(initiatePaymentRequest.currency())) {
            throw new InvalidPaymentRequestException(ErrorCode.INVALID_CURRENCY, "Currency not allowed must be one of " + validCurrencies);
        }
        if(!validAccountNumbers.contains(initiatePaymentRequest.accountNumber())) {
            throw new InvalidPaymentRequestException(ErrorCode.ACCOUNT_NOT_FOUND, "Account number not found");
        }

    }
}
