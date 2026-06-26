package com.utkarsh.movie_service.repository;

import com.utkarsh.movie_service.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByMovieId(Long movieId);

    List<Show> findByTheatre(String theatre);

    List<Show> findByAvailableSeatsGreaterThan(int seats);
}