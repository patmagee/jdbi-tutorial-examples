package io.github.patmagee.jdbi.example.controller;


import io.github.patmagee.jdbi.example.data.ActorDao;
import io.github.patmagee.jdbi.example.data.MovieLibraryDao;
import io.github.patmagee.jdbi.example.data.NotFoundException;
import io.github.patmagee.jdbi.example.model.Actor;
import io.github.patmagee.jdbi.example.model.Movie;
import io.github.patmagee.jdbi.example.model.MovieReview;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {

    @Autowired
    Jdbi jdbi;

    @GetMapping
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/movies")
    public String getMovies(Model model) {
        List<Movie> movies = jdbi.withExtension(MovieLibraryDao.class, MovieLibraryDao::listMovies);
        model.addAttribute("newMovie", new Movie());
        model.addAttribute("movies", movies);
        return "movies";
    }


    @PostMapping("/movies")
    public ModelAndView createMovie(ModelMap model, @ModelAttribute Movie movie) {
        jdbi.useExtension(MovieLibraryDao.class, dao -> {
            movie.setId(UUID.randomUUID().toString());
            dao.addMovie(movie);
            model.addAttribute("movie", movie);
        });

        return new ModelAndView("redirect:/movies/" + movie.getId(), model);
    }

    @GetMapping("/movies/{id}")
    public String getMovie(Model model, @PathVariable("id") String id) {
        jdbi.useExtension(MovieLibraryDao.class, dao -> {
            Movie movie = Optional.ofNullable(dao.getMovie(id))
                .orElseThrow(() -> new NotFoundException("Movie with id: " + id + " does not exist"));
            movie.setReviews(dao.getReviews(id));
            model.addAttribute("movie", movie);
            model.addAttribute("review", new MovieReview());
        });
        return "movie";
    }


    @PostMapping("/movies/{id}")
    public String updateMovie(Model model, @PathVariable("id") String id, @ModelAttribute Movie movie) {
        jdbi.useExtension(MovieLibraryDao.class, dao -> {
            Movie existing = Optional.ofNullable(dao.getMovie(id))
                .orElseThrow(() -> new NotFoundException("Movie with id: " + id + " does not exist"));

            if (movie.getName() != null) {
                existing.setName(movie.getName());
            }

            if (movie.getReleaseDate() != null) {
                existing.setReleaseDate(movie.getReleaseDate());
            }

            dao.updateMovie(existing);
            model.addAttribute("movie", existing);
            model.addAttribute("review", new MovieReview());
        });
        return "movie";
    }


    @GetMapping("/actors")
    public String getActors(Model model) {
        List<Actor> actors = jdbi.withExtension(ActorDao.class, ActorDao::listActors);
        model.addAttribute("actors", actors);
        model.addAttribute("newActor", new Actor());
        return "actors";
    }

    @PostMapping("/actors")
    public ModelAndView createActor(ModelMap model, @ModelAttribute Actor actor) {
        jdbi.useExtension(ActorDao.class, dao -> {
            actor.setId(UUID.randomUUID().toString());
            dao.addActor(actor);
            model.addAttribute("actor", actor);
        });

        return new ModelAndView("redirect:/actors/" + actor.getId(), model);
    }


    @GetMapping("/actors/{id}")
    public String getActor(Model model, @PathVariable("id") String id) {
        jdbi.useExtension(ActorDao.class, dao -> {
            Actor actor = Optional.ofNullable(dao.getActor(id))
                .orElseThrow(() -> new NotFoundException("actor with id: " + id + " does not exist"));
            model.addAttribute("actor", actor);
        });
        return "actor";
    }


    @PostMapping("/actors/{id}")
    public String updateActor(Model model, @PathVariable("id") String id, @ModelAttribute Actor actor) {
        jdbi.useExtension(ActorDao.class, dao -> {
            Actor existing = Optional.ofNullable(dao.getActor(id))
                .orElseThrow(() -> new NotFoundException("actor with id: " + id + " does not exist"));

            if (actor.getLastName() != null) {
                existing.setLastName(actor.getLastName());
            }

            if (actor.getFirstName() != null) {
                existing.setFirstName(actor.getFirstName());
            }

            if (actor.getDob() != null) {
                existing.setDob(actor.getDob());
            }

            dao.updateActor(actor);
            model.addAttribute("actor", existing);
        });
        return "actor";
    }

    @PostMapping("/actors/{id}/addmovie")
    public String addMovieToActor(Model model, @PathVariable("id") String id, @RequestParam("idOrName") String idOrName) {
        if (idOrName != null) {
            Movie foundMovie = jdbi.withExtension(MovieLibraryDao.class, dao -> {
                Movie movie = dao.getMovie(idOrName);
                if (movie == null) {
                    movie = dao.findMovieByName(idOrName);
                }

                return Optional.ofNullable(movie)
                    .orElseThrow(() -> new NotFoundException("Movie with id: " + idOrName + " does not exist"));

            });

            jdbi.useExtension(ActorDao.class, dao -> {
                dao.addMovieToActor(id, foundMovie.getId());
            });
        }
        return "redirect:/actors/" + id;

    }

    @PostMapping("/movies/{id}/review")
    public String addReviewToMovie( @PathVariable("id") String id,@ModelAttribute MovieReview review){
        jdbi.useExtension(MovieLibraryDao.class , dao -> {
            review.setId(UUID.randomUUID().toString());
            review.setMovieId(id);
            review.setSubmitted(ZonedDateTime.now());
            dao.addReview(review);
        });


        return "redirect:/movies/" + id;
    }

}
