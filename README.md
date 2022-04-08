![paymybuddylogo](https://user-images.githubusercontent.com/52921038/159732811-502f1fd7-479a-4796-bf37-030051178460.png)

# PayMyBuddy
Pay My Buddy - We make moving your money easy!

# Prerequisites
What things you need to install the software and how to install them

Java 11

Maven 4.0.0

Spring boot 2.4.5

MySql 8

# Installing
A step by step series of examples that tell you how to get a development env running:

Install Java: https://www.oracle.com/fr/java/technologies/javase/jdk11-archive-downloads.html

Install Maven: https://maven.apache.org/install.html

Install MySql: https://dev.mysql.com/downloads/mysql/

After downloading the mysql 8 installer and installing it, you will be asked to configure the password for the default
root account. This code uses the default root account to connect and the password can be set as rootroot.
If you add another user/credentials make sure to change the same in the code base.

To run app and integration, add password to application.properties in resources folder to the app and also to the part test. 
it is recommended to change the username and password. the identifiers are in the application.properties file
in the resources folder. To run the integration tests, make sure to also modify the connection identifiers in the
application.properties file in the tests resources folder.

# Before Running App
Before running the application, you must create the database with the following commands:

CREATE DATABASE pay_my_buddy;

USE pay_my_buddy;

When the application is run for the first time, the tables will be created automatically using Spring Boot and Spring Data,
so there is no need to execute the commands provided in the sql file named create_db.sql. 
The data.sql insertion file found in the resources folder of the tests must be executed to launch integration tests.


# Running App
Post installation of Spring Boot, Java, Maven and MySql, you will have to be ready to import the code into an IDE of your choice
and run the server to launch the application.

# Testing
The app has unit tests written. To run the tests from maven, go to the folder that contains the pom.xml file and execute the below command. mvn site and mvn test to produce some reports (surefire, JaCoCo).

# UML diagram Explaining the class structure of the models

![PayMyBuddy](https://user-images.githubusercontent.com/52921038/161936842-1c3f9ace-22cc-4ab0-9469-eecac557d520.png)

# Physical data model Explaining the structure of the database tables

![Balestrino_Clement_2_mdp_022022](https://user-images.githubusercontent.com/52921038/162394233-129433b1-b26e-4e97-8583-9571d6a6d682.png)

