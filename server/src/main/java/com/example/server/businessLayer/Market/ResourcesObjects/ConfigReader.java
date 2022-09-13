package com.example.server.businessLayer.Market.ResourcesObjects;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
    private static ConfigReader instance;
    public Properties prop;

    private ConfigReader(){
        String configFilePath = "com/example/server/businessLayer/Market/ResourcesObjects/config.properties";
        try {
            FileInputStream propsInput = new FileInputStream(configFilePath);
            this.prop = new Properties();
            prop.load(propsInput);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null){
            instance = new ConfigReader();
        }
        return instance;
    }

    public String getProperty(String prop) {
        return this.prop.getProperty(prop);
    }
}
