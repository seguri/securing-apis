package com.application.config;

import com.application.domain.exceptions.BusTicketException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class AppConfig {

    private final Properties properties = new Properties();
    private static final String APPLICATION_PROPERTIES_PATH = "/app/application.properties";

    public AppConfig() {
        try {
            FileInputStream inStream = new FileInputStream(APPLICATION_PROPERTIES_PATH);
            this.properties.load(inStream);
        } catch (IOException e) {
            throw new BusTicketException("failed to read application properties at " + APPLICATION_PROPERTIES_PATH);
        }
    }

    public String getDBConfigPath() {
        return properties.getProperty("database.config.path");
    }


    public List<String> getAPISecrets() {
        return Arrays.asList(properties.getProperty("api.secrets").split(";"));
    }

    public String getBatchUsersXSDPath() {
        return properties.getProperty("xsd.users.batch");
    }

    public String getPhotoUploadDirectory() {
        return properties.getProperty("upload.profile_photo.path");
    }
}
