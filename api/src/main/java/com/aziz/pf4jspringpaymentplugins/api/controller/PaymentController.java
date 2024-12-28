package com.aziz.pf4jspringpaymentplugins.api.controller;

import com.aziz.pf4jspringpaymentplugins.api.plugin.BankPluginManager;
import com.aziz.pf4jspringpaymentplugins.bankspi.model.InitiatePaymentRequest;
import com.aziz.pf4jspringpaymentplugins.bankspi.model.InitiatePaymentResponse;
import com.aziz.pf4jspringpaymentplugins.bankspi.spi.PaymentInitiator;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final BankPluginManager bankPluginManager;

    @PostMapping("/initiate/{bankId}")
    public ResponseEntity<InitiatePaymentResponse> initiatePayment(@Parameter(description = "The unique identifier of the bank", required = true, example = "first-bank") @PathVariable String bankId,
                                                                   @Schema(example = "{\"accountNumber\": \"1\", \"currency\": \"SAR\", \"amount\": 100.00}") @RequestBody InitiatePaymentRequest request) {
        PaymentInitiator bankPaymentInitiator =
                bankPluginManager.getBankPluginImplementation(bankId, PaymentInitiator.class);
        InitiatePaymentResponse initiatePaymentResponse = bankPaymentInitiator.initiatePayment(request);
        return new ResponseEntity<>(initiatePaymentResponse, HttpStatus.CREATED);
    }
}
