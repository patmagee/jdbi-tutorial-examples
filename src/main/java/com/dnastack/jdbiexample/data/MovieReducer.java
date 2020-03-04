package com.dnastack.jdbiexample.data;

import com.dnastack.jdbiexample.model.Actor;
import com.dnastack.jdbiexample.model.Movie;
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
