package com.epam.esm.web.config;

import com.epam.esm.util.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableWebMvc
@ComponentScan("com.epam.esm")
@EnableTransactionManagement
@Profile("dev")
public class WebConfigDev implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;

    @Autowired
    public WebConfigDev(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public DataSource getDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        Property property = Property.getInstance();

        dataSource.setDriverClassName(property.getH2_DRIVER());
        dataSource.setUsername(property.getH2_DRIVER());
        dataSource.setPassword(property.getH2_PASSWORD());
        dataSource.setUrl(property.getH2_URL());

        return dataSource;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(getDataSource());
    }
}
