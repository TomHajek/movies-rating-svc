package com.tomashajek.moviesratingsvc.repository;

import com.tomashajek.moviesratingsvc.model.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {

    Page<Movie> findAll(Pageable pageable);
    List<Movie> findTop10ByOrderByAvgRatingDescCreatedAtDesc();

}
