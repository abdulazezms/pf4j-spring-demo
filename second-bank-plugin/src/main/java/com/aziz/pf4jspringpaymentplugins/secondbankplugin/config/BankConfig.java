package com.aziz.pf4jspringpaymentplugins.secondbankplugin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@ComponentScan(value = "com.aziz.pf4jspringpaymentplugins.secondbankplugin")
public class BankConfig {
}