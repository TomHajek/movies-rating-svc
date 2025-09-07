package com.tomashajek.moviesratingsvc.repository;

import com.tomashajek.moviesratingsvc.model.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {

    Optional<Rating> findByUserIdAndMovieId(UUID userId, UUID movieId);

    @Query("SELECT AVG(r.value) FROM Rating r WHERE r.movie.id = :movieId")
    Double findAverageByMovieId(@Param("movieId") UUID movieId);

}
