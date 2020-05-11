package io.github.patmagee.jdbi.example.controller;

import io.github.patmagee.jdbi.example.data.MovieLibraryDao;
import io.github.patmagee.jdbi.example.data.NotFoundException;
import io.github.patmagee.jdbi.example.model.Movie;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
public class MovieLibraryController {


    private final Jdbi jdbi;

    @Autowired
    public MovieLibraryController(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Movie createMovie(@RequestBody Movie movie) {
        movie.setId(UUID.randomUUID().toString());
        return jdbi.withExtension(MovieLibraryDao.class, dao -> {
            dao.addMovie(movie);
            return movie;
        });
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Movie> listMovies() {
        return jdbi.withExtension(MovieLibraryDao.class, MovieLibraryDao::listMovies);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Movie getMovie(@PathVariable("id") String id) {
        return jdbi.withExtension(MovieLibraryDao.class, dao ->
            Optional.ofNullable(dao.getMovie(id))
                .orElseThrow(() -> new NotFoundException("Movie with id: " + id + " does not exist"))
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        jdbi.useExtension(MovieLibraryDao.class, dao -> {
            dao.removeActorsFromMovie(id);
            dao.deleteMovieReviewsForMovie(id);
            dao.deleteMovie(id);
        });

    }

    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Movie updateMovie(@PathVariable("id") String id, @RequestBody Movie updatedMovie) {
        return jdbi.withExtension(MovieLibraryDao.class, dao -> {
            Movie movie = Optional.ofNullable(dao.getMovie(id))
                .orElseThrow(() -> new NotFoundException("Movie with id: " + id + " does not exist"));

            if (updatedMovie.getName() != null) {
                movie.setName(updatedMovie.getName());
            }

            if (updatedMovie.getReleaseDate() != null) {
                movie.setReleaseDate(updatedMovie.getReleaseDate());
            }

            dao.updateMovie(movie);
            return movie;
        });
    }
}
