package com.epam.esm.web.config;

import com.epam.esm.model.domain.Property;
import com.epam.esm.persistence.jdbc.JdbcTemplateGiftDaoImpl;
import com.epam.esm.persistence.jdbc.JdbcTemplateTagDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;

    @Autowired
    public SpringConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        Property property = Property.getInstance();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername(property.getUSER());
        dataSource.setPassword(property.getPASSWORD());
        dataSource.setUrl(property.getURL()+property.getSCHEME());

        return dataSource;
    }

    @Bean
    JdbcTemplateGiftDaoImpl jdbcTemplateGiftDao(){
        JdbcTemplateGiftDaoImpl jdbcTemplateGiftDao = new JdbcTemplateGiftDaoImpl();
        jdbcTemplateGiftDao.setDataSource(dataSource());
        return jdbcTemplateGiftDao;
    }

    @Bean
    JdbcTemplateTagDaoImpl jdbcTemplateTagDao(){
        JdbcTemplateTagDaoImpl jdbcTemplateTagDao = new JdbcTemplateTagDaoImpl();
        jdbcTemplateTagDao.setDataSource(dataSource());
        return jdbcTemplateTagDao;
    }
}
