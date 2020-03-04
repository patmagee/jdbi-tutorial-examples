package com.dnastack.jdbiexample.data;

import com.dnastack.jdbiexample.model.MovieReview;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class MoveReviewRowMapper implements RowMapper<MovieReview> {

    @Override
    public MovieReview map(ResultSet rs, StatementContext ctx) throws SQLException {
        MovieReview review = new MovieReview();
        review.setId(rs.getString("id"));
        review.setMovieId(rs.getString("movie_id"));
        review.setContent(rs.getString("content"));
        review.setSubmittedBy(rs.getString("submitted_by"));
        review.setSubmitted(rs.getTimestamp("submitted").toInstant().atZone(ZoneId.systemDefault()));
        return review;
    }
}
