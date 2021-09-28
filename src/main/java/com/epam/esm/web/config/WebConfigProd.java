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
@EnableTransactionManagement(proxyTargetClass = true)
@Profile("prod")
@PropertySource("classpath:/application.properties")
public class WebConfigProd implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;
    private final Environment environment;
    private static final String MYSQL_DRIVER = "db.mysql.driver";
    private static final String MYSQL_URL = "db.mysql.url";
    private static final String MYSQL_SCHEME = "db.mysql.scheme";
    private static final String MYSQL_USER = "db.mysql.user";
    private static final String MYSQL_PASSWORD = "db.mysql.password";

    @Autowired
    public WebConfigProd(ApplicationContext applicationContext, Environment environment) {
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

        dataSource.setDriverClassName(environment.getProperty(MYSQL_DRIVER));
        dataSource.setUsername(environment.getProperty(MYSQL_USER));
        dataSource.setPassword(environment.getProperty(MYSQL_PASSWORD));
        dataSource.setUrl(environment.getProperty(MYSQL_URL)+environment.getProperty(MYSQL_SCHEME));

        return dataSource;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(getDataSource());
    }
}
