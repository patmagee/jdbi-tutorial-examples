package io.github.patmagee.jdbi.example;

import lombok.Data;

@Data
public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
