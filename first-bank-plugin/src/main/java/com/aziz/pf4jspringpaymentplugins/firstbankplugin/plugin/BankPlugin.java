package com.aziz.pf4jspringpaymentplugins.firstbankplugin.plugin;

import com.aziz.pf4jspringpaymentplugins.firstbankplugin.config.BankConfig;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringPlugin;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class BankPlugin extends SpringPlugin {
    public BankPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    protected ApplicationContext createApplicationContext() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.setClassLoader(getWrapper().getPluginClassLoader());
        applicationContext.register(BankConfig.class);
        applicationContext.refresh();
        return applicationContext;
    }

    @Override
    public void start() {
        log.info("FirstBankPlugin started");
    }

    @Override
    public void stop() {
        log.info("FirstBankPlugin stopped");
    }

    @Override
    public void delete() {
        log.info("FirstBankPlugin deleted");
    }
}