package io.github.patmagee.jdbi.example.data;

import io.github.patmagee.jdbi.example.model.Actor;
import io.github.patmagee.jdbi.example.model.Movie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

public class MovieReducer implements LinkedHashMapRowReducer<String, Movie> {

    @Override
    public void accumulate(Map<String, Movie> container, RowView rowView) {

        Movie movie = container
            .computeIfAbsent(rowView.getColumn("m_id", String.class), (k) -> rowView.getRow(Movie.class));

        if (rowView.getColumn("a_id", String.class) != null) {
            Actor actor = rowView.getRow(Actor.class);
            if (movie.getActors() == null) {
                movie.setActors(new ArrayList<>());
            }

            List<Actor> actors = movie.getActors();
            if (!actors.contains(actor)){
                actors.add(actor);
            }
        }
    }
}
