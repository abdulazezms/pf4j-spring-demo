package com.aziz.pf4jspringpaymentplugins.api.config;

import org.pf4j.ExtensionFactory;
import org.pf4j.spring.SingletonSpringExtensionFactory;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

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
