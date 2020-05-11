# JDBI Movie Online Database Example

This project provides a simple Application whch demonstrates the capabilities of JDBI in a simple spring application


## Requirements

- Java 11
- Postgres


## Creating Postgres User and Database


```sh
createuser -r -d jdbiexample
createdb -U jdbiexample jdbiexample
```

## Building

```sh
./mvnw clean install
```


## Running

```sh
java -jar ./target/jdbi-example-0.0.1-SNAPSHOT.jar
```