package io.github.patmagee.jdbi.example.model;

import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@EqualsAndHashCode(of = {"name", "id"})
public class Movie {

    String id;

    String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    Date releaseDate;


    List<MovieReview> reviews;

    List<Actor> actors;

}
