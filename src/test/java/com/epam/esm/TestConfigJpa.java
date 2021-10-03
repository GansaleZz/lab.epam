package com.epam.esm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.epam.esm.persistence.jpa",
        "com.epam.esm.persistence.dao",
        "com.epam.esm.persistence.util.*",
        "com.epam.esm.service.*",
        "com.epam.esm.web.controller",
        "com.epam.esm.web.util.*",
        "com.epam.esm.web.main"
})
public class TestConfigJpa {
    private final ApplicationContext applicationContext;

    @Autowired
    public TestConfigJpa(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
