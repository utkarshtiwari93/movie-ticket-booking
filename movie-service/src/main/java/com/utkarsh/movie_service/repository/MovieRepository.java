package com.utkarsh.movie_service.repository;

import com.utkarsh.movie_service.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByGenre(String genre);

    List<Movie> findByLanguage(String language);

    List<Movie> findByTitleContainingIgnoreCase(String keyword);
}