package com.example.server.businessLayer.Market.ResourcesObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DebugLog {

    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_BLACK = "\u001B[30m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_YELLOW = "\u001B[33m";
    public static final String TEXT_BLUE = "\u001B[34m";
    public static final String TEXT_PURPLE = "\u001B[35m";
    public static final String TEXT_CYAN = "\u001B[36m";
    public static final String TEXT_WHITE = "\u001B[37m";
    private Logger logger ;
    File loggerFile;

    private static DebugLog instance;

    private DebugLog(){
        logger = LogManager.getLogger("warning logger");
        loggerFile = new File(System.getProperty("user.dir")+"/ErrorLog.txt");
    }

    public static DebugLog getInstance() {
        if (instance == null)
            instance = new DebugLog();
        return instance;
    }

    public void Log(String msg){
        try {
            FileWriter fileWriter = new FileWriter(loggerFile,true);
            String level = "Level Error : ";
            fileWriter.write((level + msg)+"\n");
            System.out.println(TEXT_YELLOW + level + TEXT_RESET + msg);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
