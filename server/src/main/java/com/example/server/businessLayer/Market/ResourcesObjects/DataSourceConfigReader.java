package com.example.server.businessLayer.Market.ResourcesObjects;

import org.springframework.util.DefaultPropertiesPersister;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class DataSourceConfigReader {

    private static DataSourceConfigReader instance;
    public Properties prop;

    private DataSourceConfigReader(String path) throws MarketException {
        checkContent(path);
        FileInputStream propsInput;
        try {
            propsInput = new FileInputStream(path);
        } catch (FileNotFoundException ex) {
            throw new MarketException("Data source config file not found.");
        }
        try {
            this.prop = new Properties();
            prop.load(propsInput);
            File f = new File(getProperiesDir());
            OutputStream out = new FileOutputStream(f);
            // write into it
            DefaultPropertiesPersister p = new DefaultPropertiesPersister();
            p.store(prop, out, "Header Comment");
        } catch (Exception e) {
            throw new MarketException("Failed to init properties out of Data source config file.");
        }

    }
    private void checkContent(String path) throws MarketException {
        Map<String, String> ret= readFromFile(path);
        if(!ret.containsKey("url") || (ret.get("url")==null || ret.get("url").isEmpty())){throw new MarketException("Missing url in data source config file.");}
        if(!ret.containsKey("password") || (ret.get("password")==null || ret.get("password").isEmpty())){throw new MarketException("Missing password in data source config file.");}
        if(!ret.containsKey("username") || (ret.get("username")==null || ret.get("username").isEmpty())){throw new MarketException("Missing username in data source config file.");}
        if(!ret.containsKey("spring.jpa.hibernate.ddl-auto") || (ret.get("spring.jpa.hibernate.ddl-auto")==null || ret.get("spring.jpa.hibernate.ddl-auto").isEmpty())){throw new MarketException("Missing hibernate auto config in data source config file.");}
    }


        private Map<String, String> readFromFile(String path) throws MarketException {
        Map<String,String> ret= new HashMap<>();
            File myObj = new File(path);
            Scanner myReader = null;
            try {
                myReader = new Scanner(myObj);
            } catch (FileNotFoundException ex) {
                throw new MarketException("Data source config file not found.");
            }
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] vals = data.split("=");
                if(vals.length<2){
                    String err=vals[0];
                    if(err.contains("url"))
                            throw new MarketException("Missing url in data source config file.");
                    else if(err.contains("password"))
                            throw new MarketException("Missing password in data source config file.");
                    else if(err.contains("username"))
                            throw new MarketException("Missing username in data source config file.");
                    else if(err.contains("spring.jpa.hibernate.ddl-auto"))
                            throw new MarketException("Missing hibernate auto config in data source config file.");
                    else
                        throw new MarketException("Missing configurations in data source config file.");
                }
                setData(vals[0], vals[1],ret);
            }


        return ret;
    }

    private void setData(String val, String val1,Map<String,String> data) {
        if(val.toLowerCase().contains("url")){
            if(!data.containsKey("url")){
                data.put("url",val1);
            }
        }
        else if(val.toLowerCase().contains("username")){
            if(!data.containsKey("username")){
                data.put("username",val1);
            }
        }
        else if(val.toLowerCase().contains("password")){
            if(!data.containsKey("password")){
                data.put("password",val1);
            }
        } else if (val.toLowerCase().contains("spring.jpa.hibernate.ddl-auto")) {
            if(!data.containsKey("spring.jpa.hibernate.ddl-auto")){
                data.put("spring.jpa.hibernate.ddl-auto",val1);
            }
        }
    }


    public static DataSourceConfigReader getInstance(String path) throws MarketException {
        if (instance == null){
            instance = new DataSourceConfigReader(path);
        }
        return instance;
    }

    public static void resetInstance(){
        instance=null;
    }

    public String getProperty(String prop) {
        return this.prop.getProperty(prop);
    }

     private String getProperiesDir() {
        String dir = System.getProperty("user.dir");
        String additional_dir = "\\src\\main\\resources\\application.properties";
        if (MarketConfig.IS_MAC) {
            additional_dir = "/src/main/resources/application.properties";
        }
        dir += additional_dir;
        return dir;
    }
}
