package com.tomashajek.moviesratingsvc.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "movies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer year;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Rating> ratings;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private int ratingSum = 0;

    @Column(nullable = false)
    private int ratingCount = 0;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }

    public double getAvgRating() {
        return ratingCount == 0 ? 0.0 : (double) ratingSum / ratingCount;
    }

}
