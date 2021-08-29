package com.epam.esm.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    private static final Properties properties = new Properties();
    private static PropertyReader instance = null;

    public static Properties getProperties() {
        if (instance == null) {
            loadProperties();
            instance = new PropertyReader();
        }
        return properties;
    }
    /**
     * Reading properties from application.properties
     */
    public static void loadProperties() {
        String propertiesFileName = "/Users/andrew_wannasesh/folders/" +
                "EPAMLAB/EPAM_LAB/src/main/resources/application.properties";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(propertiesFileName);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private PropertyReader() {
    }
}
