package com.epam.esm.web.config;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        WebApplicationContext context = super.createServletApplicationContext();
        ((ConfigurableEnvironment)context.getEnvironment()).setActiveProfiles("prod");
        return context;
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfigProd.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
