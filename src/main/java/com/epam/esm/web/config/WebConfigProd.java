package com.epam.esm.web.config;

import com.epam.esm.util.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
public class WebConfigProd implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;

    @Autowired
    public WebConfigProd(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(getDataSource());
    }

    @Bean
    public DataSource getDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        Property property = Property.getInstance();

        dataSource.setDriverClassName(property.getMYSQL_DRIVER());
        dataSource.setUsername(property.getMYSQL_USER());
        dataSource.setPassword(property.getMYSQL_PASSWORD());
        dataSource.setUrl(property.getMYSQL_URL()+property.getMYSQL_SCHEME());

        return dataSource;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(getDataSource());
    }
}
