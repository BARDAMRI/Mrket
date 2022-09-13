package com.example.server.businessLayer.Market.ResourcesObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ErrorLog {
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_RED = "\u001B[31m";

    private Logger logger ;
    File loggerFile;

    private static ErrorLog instance;

    private ErrorLog(){
        logger = LogManager.getLogger("warning logger");
        loggerFile = new File(System.getProperty("user.dir")+"/ErrorLog.txt");
    }

    public static ErrorLog getInstance() {
        if (instance == null)
            instance = new ErrorLog();
        return instance;
    }

    public void Log(String msg){
        try{
            FileWriter fileWriter = new FileWriter(loggerFile,true);
            String level = "Level Error : ";
            fileWriter.write((level + msg)+"\n");
            System.out.println(TEXT_RED + level + TEXT_RESET + msg);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
