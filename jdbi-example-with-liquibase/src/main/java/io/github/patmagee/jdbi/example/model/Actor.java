package io.github.patmagee.jdbi.example.model;

import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@EqualsAndHashCode(of = {"id","firstName","lastName"})
public class Actor {

    String id;
    String firstName;
    String lastName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    Date dob;

    List<Movie> movies;
}
