package com.epam.esm.web.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Locale;

@Configuration
@ComponentScan(basePackages = {
        "com.epam.esm.persistence.jpa.*",
        "com.epam.esm.persistence.dao",
        "com.epam.esm.persistence.util.*",
        "com.epam.esm.service.*",
        "com.epam.esm.web.controller",
        "com.epam.esm.web.util.*"
})
public class JpaConfig {
    private ApplicationContext applicationContext;
    private final JpaVendorAdapter jpaVendorAdapter;
    private static final String PACKAGES_TO_SCAN = "com.epam.esm";
    private static final String DEFAULT = "default";

    @Autowired
    public JpaConfig(ApplicationContext applicationContext, JpaVendorAdapter jpaVendorAdapter) {
        this.applicationContext = applicationContext;
        this.jpaVendorAdapter = jpaVendorAdapter;
    }

    @Bean
    @Primary
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setJpaVendorAdapter(jpaVendorAdapter);
        emf.setPackagesToScan(PACKAGES_TO_SCAN);
        emf.setPersistenceUnitName(DEFAULT);
        emf.afterPropertiesSet();
        return emf.getObject();
    }

    @Bean
    public SessionFactory sessionFactory(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.unwrap(SessionFactory.class);
    }
}
