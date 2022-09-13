# market_2022



# config file
The configuration file contains the initialized data needed for the market loading. The configuration file has three parts:
- Services configuration.
- Init data configuration.
- Data source configurations.

All configurations, are called  only if the market initializer asked for data load. In case the market will be initialize by the user without any additional data, only the services file will be used in order to set the services. The services will be authomatically configure into default values unless the user choose otherwise in the config file.

## Services Configurations
The configurations file is a txt file called config.txt contains the relevand parameters for the initialization of the system external services. The services will include:
- Payment service
- Supply service
- Notification service

The config.txt fille will appear in the path:

> ../server/config/config.txt

The file format is:

> 'service name'::'instance type'

The instances will be from the options below:
- Payment service : WSEP
- Supply service : WSEP
- Notification service : Text, Notifications (when the text save the notifications in txt file in the server and the notifications is a 
notification-dispacher working through web-socket connection)

If the fill will not be found, or one of the files will not contains the services with the right format and context, the system will not be initialized and the user will get right message. 



## Data
The data nedded to be loaded in the system directly. The data contains first information on users, shops, items and another objects. The only one thing needed to be in the Data file is the system manager configuration. If it will not be in the file, the system will not be initialized and right message will be sent to the user.

When the system is being intialized, the data is being read from of file in the system's current directory under the path:
> ../server/config/Data.txt

The file will be in the next format:
> 'method'::'field1'::'field2::'field3' etc..
The number of fields is changing according to the method type and arguments number.

The methods available are: 
- SystemManager
- Register
- Login
- Logout
- Open_Shop
- Add_Item
- Appoint_Manager
- Appoint_Owner

Important! 
The methods should be in the correct order, If a method will not appear after all the pre-methods nedded for it to succeed, it will not be executed.

By default, the data will be:
- System manager: u1
- Members: u1,u2,u3,u4,u5,u6
- mpasswords: all of the members passwords are 'password'
- Logged In: none
- Shops: s1
- Items: bamba- in shop s1
- Owners: s2,u4,u5- owners of s1
- Managers: s3- manager of s1


## Data source 
A file contains the data source configurations. The file contains the configurations called DataSourceConfig.txt as part of the configuration files directory.

The file path is:
> ../server/config/DataSourceConfig.txt

The configurations in the are:
- user name
-password
-url
- hybernate dll config

Each configuration, will be from the format:
> 'configuration'='value'

In case one of them is missing, the system will not be initialized and a right message will be sent to the user. When the system is being initialized, the file will be read and the lines will be inserted into the file 'application.properties' for the spring-boot and hybernate bootstrap.
