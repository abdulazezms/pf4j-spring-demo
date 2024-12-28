package com.aziz.pf4jspringpaymentplugins.api.config;

import org.pf4j.ExtensionFactory;
import org.pf4j.spring.SingletonSpringExtensionFactory;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SpringPluginManager springPluginManager() {
        return new SpringPluginManager() {
            //Or use new SpringPluginManager(pluginsPath) if plugins are packaged with the application.
            @Override
            protected ExtensionFactory createExtensionFactory() {
                return new SingletonSpringExtensionFactory(this);
            }
        };
    }
}
