package com.tomashajek.moviesratingsvc.repository;

import com.tomashajek.moviesratingsvc.model.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {

    @Query(
        value = """
            SELECT m.*
            FROM movies m
            LEFT JOIN ratings r ON m.id = r.movie_id
            GROUP BY m.id
            ORDER BY COALESCE(AVG(r.value), 0) DESC
        """,
        countQuery = "SELECT COUNT(*) FROM movies",
        nativeQuery = true
    )
    Page<Movie> pageAllMoviesSorterByRating(Pageable pageable);

    @Query(
        value = """
            SELECT m.*
            FROM movies m
            LEFT JOIN ratings r ON m.id = r.movie_id
            GROUP BY m.id
            ORDER BY COALESCE(AVG(r.value), 0) DESC
        """,
        nativeQuery = true
    )
    List<Movie> findTopRatedMovies(@Param("limit") int limit);

}
