package com.epam.esm.util;

import lombok.Getter;

import java.util.Properties;

@Getter
public class Property {
    private final String MYSQL_URL;
    private final String MYSQL_SCHEME;
    private final String MYSQL_PASSWORD;
    private final String MYSQL_USER;
    private final String MYSQL_DRIVER;
    private final String H2_URL;
    private final String H2_PASSWORD;
    private final String H2_USER;
    private final String H2_DRIVER;
    private static Property instance = null;

    public static Property getInstance(){
        if (instance == null) {
            instance = new Property();
        }
        return instance;
    }

    private Property(){
        Properties properties = PropertyReader.getProperties();
        MYSQL_URL = properties.getProperty("db.mysql.url");
        MYSQL_SCHEME =  properties.getProperty("db.mysql.scheme");
        MYSQL_PASSWORD = properties.getProperty("db.mysql.password");
        MYSQL_USER = properties.getProperty("db.mysql.user");
        MYSQL_DRIVER = properties.getProperty("db.mysql.driver");
        H2_URL = properties.getProperty("db.h2.url");
        H2_DRIVER = properties.getProperty("db.h2.driver");
        H2_USER = properties.getProperty("db.h2.username");
        H2_PASSWORD = properties.getProperty("db.h2.password");
    }
}
