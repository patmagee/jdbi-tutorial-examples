# JDBI Spring Boot Example

This project provides a simple Application which demonstrates the capabilities of Jdbi in a simple spring application


## Requirements

- Java 11
- Postgres


## Creating Postgres User and Database


```sh
createuser -r -d jdbi-example-spring-boot
createdb -U jdbi-example-spring-boot jdbi-example-spring-boot
```

## Building

```sh
./mvnw clean install
```


## Running

```sh
java -jar ./target/jdbi-example-spring-boot-0.0.1-SNAPSHOT.jar
```