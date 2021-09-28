package com.epam.esm.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableWebMvc
@ComponentScan("com.epam.esm")
@EnableTransactionManagement
@Profile("dev")
@PropertySource("classpath:/application.properties")
public class WebConfigDev implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;
    private final Environment environment;
    private static final String H2_DRIVER = "db.h2.driver";
    private static final String H2_USER = "db.h2.username";
    private static final String H2_PASSWORD = "db.h2.password";
    private static final String H2_URL = "db.h2.url";

    @Autowired
    public WebConfigDev(ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;
        this.environment = environment;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(getDataSource());
    }

    @Bean
    public DataSource getDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(environment.getProperty(H2_DRIVER));
        dataSource.setUsername(environment.getProperty(H2_USER));
        dataSource.setPassword(environment.getProperty(H2_PASSWORD));
        dataSource.setUrl(environment.getProperty(H2_URL));

        return dataSource;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(getDataSource());
    }
}
