package com.epam.esm.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.epam.esm.persistence.jdbc.*",
        "com.epam.esm.persistence.dao",
        "com.epam.esm.persistence.util.*",
        "com.epam.esm.service.*",
        "com.epam.esm.web.controller",
        "com.epam.esm.web.util.*"
})
public class JdbcConfig {
    private ApplicationContext applicationContext;

    @Autowired
    public JdbcConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
