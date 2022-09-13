package com.example.server.businessLayer.Market.ResourcesObjects;

public class MarketConfig {
    public static final long SCHEDULE_MILSEC =1000 ;
    public static boolean IS_TEST_MODE=true;
    public static boolean USING_DATA=true;
    public static String PAYMENT_SERVICE_NAME="PaymentService";
    public static String SUPPLY_SERVICE_NAME="SupplyService";
    public static String PUBLISHER_SERVICE_NAME="Publisher";
    public static String SYSTEM_MANAGER_NAME="SystemManager";
    public static String WSEP_SERVICE="WSEP";
    public static String WSEP_ADDRESS="https://cs-bgu-wsep.herokuapp.com/";
    public static String TEXT_PUBLISHER="Text";
    public static String NOTIFICATIONS_PUBLISHER="Notifications";
    public static String SERVICES_FILE_NAME="config.txt";
    public static String DATA_FILE_NAME="Data.txt";
    public static boolean IS_MAC=false;
    public static String DATA_SOURCE_FILE_NAME ="DataSourceConfig.txt";
    public static String MISS_NAME_DATA_SOURCE_FILE_NAME ="missNameDataSourceConfig.txt";
    public static String MISS_PASSWORD_DATA_SOURCE_FILE_NAME ="missPassDataSourceConfig.txt";
}
