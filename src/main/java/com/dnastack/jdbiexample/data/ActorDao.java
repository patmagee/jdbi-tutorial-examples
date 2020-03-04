package com.dnastack.jdbiexample.data;

import com.dnastack.jdbiexample.model.Actor;
import com.dnastack.jdbiexample.model.Movie;
import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterBeanMappers;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;
import org.jdbi.v3.sqlobject.transaction.Transaction;



@RegisterBeanMappers(value = {
    @RegisterBeanMapper(value = Movie.class,prefix = "m"),
    @RegisterBeanMapper(value = Actor.class,prefix = "a")
})
public interface ActorDao {

    @Transaction
    @SqlUpdate("INSERT INTO actors(id,first_name,last_name,dob) VALUES(:id,:firstName,:lastName,:dob)")
    void addActor(@BindBean Actor movie);

    @SqlQuery("SELECT "
        + "  a.id a_id, a.first_name a_first_name, a.last_name a_last_name, a.dob a_dob, "
        + "  m.id m_id, m.name m_name, m.release_date m_release_date "
        + "FROM actors a "
        + "  LEFT JOIN movies_actors_joint_table jt on jt.actor_id = a.id "
        + "  LEFT JOIN movies m on m.id = jt.movie_id ")
    @UseRowReducer(ActorReducer.class)
    List<Actor> listActors();

    @SqlQuery("SELECT "
        + "  a.id a_id, a.first_name a_first_name, a.last_name a_last_name, a.dob a_dob, "
        + "  m.id m_id, m.name m_name, m.release_date m_release_date "
        + "FROM actors a "
        + "  LEFT JOIN movies_actors_joint_table jt on jt.actor_id = a.id "
        + "  LEFT JOIN movies m on m.id = jt.movie_id "
        + "WHERE a.id = :id"
    )
    @UseRowReducer(ActorReducer.class)
    Actor getActor(@Bind("id") String id);

    @Transaction
    @SqlUpdate("UPDATE actors set first_name = :firstName, last_name = :lastName, dob = :dob WHERE id = :id")
    void updateActor(@BindBean Actor movie);

    @Transaction
    @SqlUpdate("DELETE FROM actors where id = :id")
    void deleteActor(@Bind("id") String id);

    @Transaction
    @SqlUpdate("DELETE FROM movies_actors_joint_table WHERE actor_id = :id")
    void removeMoviesFromActor(@Bind("id") String id);

    @Transaction
    @SqlUpdate("INSERT INTO movies_actors_joint_table(actor_id,movie_id) VALUES(:actorId,:movieId)")
    void addMovieToActor(@Bind("actorId") String actorId, @Bind("movieId") String movieId);
}
