package com.epam.esm;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.web.WebAppConfiguration;


@Configuration
@ComponentScan(basePackages = {
        "com.epam.esm.persistence.dao",
        "com.epam.esm.persistence.jdbc.*",
        "com.epam.esm.persistence.util.*",
})
@WebAppConfiguration
public class TestConfigJdbc {

    private final ApplicationContext applicationContext;

    @Autowired
    public TestConfigJdbc(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
