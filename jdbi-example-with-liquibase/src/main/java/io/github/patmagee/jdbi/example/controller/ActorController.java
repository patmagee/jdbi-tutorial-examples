package io.github.patmagee.jdbi.example.controller;

import io.github.patmagee.jdbi.example.data.ActorDao;
import io.github.patmagee.jdbi.example.data.NotFoundException;
import io.github.patmagee.jdbi.example.model.Actor;
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
@RequestMapping("/api/actors")
public class ActorController {


    private final Jdbi jdbi;

    @Autowired
    public ActorController(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Actor createActor(@RequestBody Actor movie) {
        movie.setId(UUID.randomUUID().toString());
        return jdbi.withExtension(ActorDao.class, dao -> {
            dao.addActor(movie);
            return movie;
        });
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Actor> listMovies() {
        return jdbi.withExtension(ActorDao.class, ActorDao::listActors);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Actor getMovie(@PathVariable("id") String id) {
        return jdbi.withExtension(ActorDao.class, dao ->
            Optional.ofNullable(dao.getActor(id))
                .orElseThrow(() -> new NotFoundException("Movie with id: " + id + " does not exist"))
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        jdbi.useExtension(ActorDao.class, dao -> {
            dao.removeMoviesFromActor(id);
            dao.deleteActor(id);
        });

    }

    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Actor updateMovie(@PathVariable("id") String id, @RequestBody Actor updatedActor) {
        return jdbi.withExtension(ActorDao.class, dao -> {
            Actor actor = Optional.ofNullable(dao.getActor(id))
                .orElseThrow(() -> new NotFoundException("Movie with id: " + id + " does not exist"));

            if (updatedActor.getFirstName() != null) {
                actor.setFirstName(updatedActor.getFirstName());
            }

            if (updatedActor.getLastName() != null) {
                actor.setLastName(updatedActor.getLastName());
            }

            if (updatedActor.getDob() != null) {
                actor.setDob(updatedActor.getDob());
            }

            dao.updateActor(actor);
            return actor;
        });
    }
}
