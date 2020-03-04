package com.dnastack.jdbiexample.data;

import com.dnastack.jdbiexample.model.Actor;
import com.dnastack.jdbiexample.model.Movie;
import com.dnastack.jdbiexample.model.MovieReview;
import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterBeanMappers;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;
import org.jdbi.v3.sqlobject.transaction.Transaction;

@RegisterBeanMappers(value = {
    @RegisterBeanMapper(value = Movie.class,prefix = "m"),
    @RegisterBeanMapper(value = Actor.class,prefix = "a")
})
public interface MovieLibraryDao {

    @Transaction
    @SqlUpdate("INSERT INTO movies(id,name,release_date) VALUES(:id,:name,:releaseDate)")
    void addMovie(@BindBean Movie movie);


    @SqlQuery("SELECT "
        + "  a.id a_id, a.first_name a_first_name, a.last_name a_last_name, a.dob a_dob, "
        + "  m.id m_id, m.name m_name, m.release_date m_release_date "
        + "FROM movies m "
        + "  LEFT JOIN movies_actors_joint_table jt on jt.movie_id = m.id "
        + "  LEFT JOIN actors a on a.id = jt.actor_id "
    )
    @UseRowReducer(MovieReducer.class)
    List<Movie> listMovies();


    @SqlQuery("SELECT m.id m_id, m.name m_name, m.release_date m_release_date FROM movies m WHERE name = :name")
    Movie findMovieByName(@Bind("name") String name);


    @SqlQuery("SELECT "
        + "  a.id a_id, a.first_name a_first_name, a.last_name a_last_name, a.dob a_dob, "
        + "  m.id m_id, m.name m_name, m.release_date m_release_date "
        + "FROM movies m "
        + "  LEFT JOIN movies_actors_joint_table jt on jt.movie_id = m.id "
        + "  LEFT JOIN actors a on a.id = jt.actor_id "
        + "WHERE m.id = :id"
    )
    @UseRowReducer(MovieReducer.class)
    Movie getMovie(@Bind("id") String id);

    @Transaction
    @SqlUpdate("DELETE FROM movies where id = :id")
    void deleteMovie(@Bind("id") String id);

    @Transaction
    @SqlUpdate("DELETE FROM movies_actors_joint_table WHERE movie_id = :id")
    void removeActorsFromMovie(@Bind("id") String id);

    @Transaction
    @SqlUpdate("UPDATE movies set name = :name, release_date = :releaseDate WHERE id = :id")
    void updateMovie(@BindBean Movie movie);

    @Transaction
    @SqlUpdate("INSERT INTO movie_reviews(id,movie_id,submitted_by,submitted,content) VALUES(:id,:movieId,:submittedBy,:submitted,:content)")
    void addReview(@BindBean MovieReview movieReview);

    @Transaction
    @SqlUpdate("DELETE FROM movie_reviews where movie_id = :movieId")
    void deleteMovieReviewsForMovie(@Bind("movieId") String movieId);

    @UseRowMapper(MoveReviewRowMapper.class)
    @SqlQuery("SELECT * From movie_reviews WHERE movie_id = :movieId")
    List<MovieReview> getReviews(@Bind("movieId") String movieId);
}
