package io.github.patmagee.jdbi.example.model;

import java.time.ZonedDateTime;
import lombok.Data;

@Data
public class MovieReview {

    String id;
    String movieId;
    String submittedBy;
    ZonedDateTime submitted;
    String content;

}
