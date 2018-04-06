# spring-api-template

A skeleton project for implementing REST APIs with spring.

Project uses:
* java 8
* maven 3
* spring-boot-5.x.x
* spring-security-5.x.x
* spring-data-jpa-2.x.x
* spring actuator-2.x.x
* hibernate-core-5.2.x
* hibernate-validator-6.x.x
* swagger2-2.x
* lombok-1.x.x
* mapstruct-jdk8
* liquibase-core-3.x.x
* postgresql 
* junit-4.x.x (testing)
* h2 (testing)
* mockito (testing)

## Building
Since the project uses Maven , it can be built using:

```mvn clean install```

This will produce a launchable artifact in `target/`.

## Running
The executable jar can be started with

```java -jar target/spring-api-template.jar```

Alternatively, the application can also be started with

```mvn spring-boot:run```

For the application to run you need a `postgresql` database running and adjusting the connection string in `application.yml`.
