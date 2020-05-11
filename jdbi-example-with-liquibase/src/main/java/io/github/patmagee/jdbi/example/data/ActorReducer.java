package io.github.patmagee.jdbi.example.data;

import io.github.patmagee.jdbi.example.model.Actor;
import io.github.patmagee.jdbi.example.model.Movie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

public class ActorReducer implements LinkedHashMapRowReducer<String, Actor> {

    @Override
    public void accumulate(Map<String, Actor> container, RowView rowView) {

        Actor actor = container
            .computeIfAbsent(rowView.getColumn("a_id", String.class), (k) -> rowView.getRow(Actor.class));

        if (rowView.getColumn("m_id", String.class) != null) {
            Movie movie = rowView.getRow(Movie.class);
            if (actor.getMovies() == null) {
                actor.setMovies(new ArrayList<>());
            }

            List<Movie> movies = actor.getMovies();
            if (!movies.contains(movie)) {
                movies.add(movie);
            }
        }
    }
}
