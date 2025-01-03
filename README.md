# PF4J Spring Plugin Demo

## Table of Contents
1. [Overview](#overview)
2. [Key Components](#key-components)
3. [Getting Started](#getting-started)
4. [Notes](#notes)
5. [References](#references)


A demonstration project showcasing how to implement a plugin-based system using PF4J-Spring framework with dynamic plugin loading capabilities. This project implements a simple payment initiation API resource to illustrate plugin concepts.


## Overview

This demo shows how to:
- Create a plugin-based architecture with Spring
- Dynamically load plugins at runtime
- Map plugins to business identifiers (bank IDs in this case)

#### 1. Project Structure
```
pf4j-spring-payment-plugins/
├── bank-spi/                 # Shared interfaces
├── first-bank-plugin/        # Implementation for Bank 1
├── second-bank-plugin/       # Implementation for Bank 2
└── api/                      # Host application
```

## Key Components

### Plugin Configuration in Host Application

```java
import org.pf4j.ExtensionFactory;
import org.pf4j.spring.SingletonSpringExtensionFactory;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SpringPluginManager springPluginManager(AppProperties appProperties) {
        return new SpringPluginManager(Path.of(appProperties.getPluginsPath())) {
            @Override
            protected ExtensionFactory createExtensionFactory() {
                return new SingletonSpringExtensionFactory(this);
            }
        };
    }
}

```

### REST Controller for Plugin Management
```java
@RestController
@RequestMapping("/api/plugins")
@RequiredArgsConstructor
public class PluginsController {
    private final BankPluginManager bankPluginManager;

    @PostMapping(value = "/{bankId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addBankPlugin(@PathVariable String bankId,
                                                @RequestParam("file") MultipartFile file) {
        bankPluginManager.addBankPlugin(bankId, file);
        return new ResponseEntity<>("Plugin added successfully", HttpStatus.CREATED);
    }
}
```

### REST Controller for Payment Initiation
```java
package com.aziz.pf4jspringpaymentplugins.api.controller;

import com.aziz.pf4jspringpaymentplugins.api.plugin.BankPluginManager;
import com.aziz.pf4jspringpaymentplugins.bankspi.model.InitiatePaymentRequest;
import com.aziz.pf4jspringpaymentplugins.bankspi.model.InitiatePaymentResponse;
import com.aziz.pf4jspringpaymentplugins.bankspi.spi.PaymentInitiator;
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
    public ResponseEntity<InitiatePaymentResponse> initiatePayment(@PathVariable String bankId,
                                                                   @RequestBody InitiatePaymentRequest request) {
        PaymentInitiator bankPaymentInitiator =
                bankPluginManager.getBankPluginImplementation(bankId, PaymentInitiator.class);
        InitiatePaymentResponse initiatePaymentResponse = bankPaymentInitiator.initiatePayment(request);
        return new ResponseEntity<>(initiatePaymentResponse, HttpStatus.CREATED);
    }
}


```

### Plugin Implementation Example

```java
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
```
### Extensions metadata
Any extension marked with `@Extension` should be part of the `target/classes/META-INF/extensions.idx` generated file by PF4J.
If annotation processing isn't working properly in your IDE, you can manually add it.

```
# Generated by PF4J
com.aziz.pf4jspringpaymentplugins.firstbankplugin.service.PaymentInitiatorImpl
```

## Getting Started

### 1. Build the Project
```bash
mvn clean package
```

### 2. Start the Application
```bash
java -jar api/target/api-0.0.1-SNAPSHOT.jar
```

### 3. Upload a Plugin
1. go to [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
2. Upload either bank's plugin JAR from `[first|second]-bank-plugin/target/*.jar` and associate it with the `<bank-id>`
3. Initiate a payment against `<bank-id>`. `<bank-id>` plugin implementation will then be used.

## Notes

### Application Context Sharing

A plugin module can share the application context with the host module.
This can be done by setting the parent application context of the plugin module to that of the host.

```java
if(wrapper.getPluginManager() instanceof SpringPluginManager springPluginManager) {
        applicationContext.setParent(springPluginManager.getApplicationContext());
}
```

## References

- [PF4J Documentation](https://pf4j.org/)
- [PF4J-Spring Documentation](https://github.com/pf4j/pf4j-spring)
