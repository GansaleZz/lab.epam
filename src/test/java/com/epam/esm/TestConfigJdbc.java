package com.epam.esm;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@ComponentScan(basePackages = {
        "com.epam.esm.persistence.jdbc.*",
        "com.epam.esm.persistence.dao",
        "com.epam.esm.persistence.util.*",
})
@WebAppConfiguration
public class TestConfigJdbc {

    private final ApplicationContext applicationContext;

    @Autowired
    public TestConfigJdbc(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        return new DataSourceTransactionManager(getDataSource());
//    }
//
//    @Bean
//    public EmbeddedDatabase getDataSource(){
//        return new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.H2)
//                .build();
//    }

//    @Bean
//    public JdbcTemplate getJdbcTemplate(){
//        return new JdbcTemplate(getDataSource());
//    }
}
