package com.epam.esm.model.domain;

import com.epam.esm.model.util.PropertyReader;
import lombok.Getter;

import java.util.Properties;

@Getter
public class Property {
    private final String URL;
    private final String SCHEME;
    private final String PASSWORD;
    private final String USER;
    private final String DRIVER;
    private static Property instance = null;

    public static Property getInstance(){
        if(instance == null){
            instance = new Property();
        }
        return instance;
    }

    private Property(){
        Properties properties = PropertyReader.getProperties();
        URL = properties.getProperty("db.url");
        SCHEME =  properties.getProperty("db.scheme");
        PASSWORD = properties.getProperty("db.password");
        USER = properties.getProperty("db.user");
        DRIVER = properties.getProperty("db.driver");
    }
}
