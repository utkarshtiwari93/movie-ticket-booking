package com.utkarsh.movie_service.service;

import com.utkarsh.movie_service.entity.Show;
import com.utkarsh.movie_service.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepository;

    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    public List<Show> getShowsByMovie(Long movieId) {
        return showRepository.findByMovieId(movieId);
    }

    public List<Show> getAvailableShows() {
        return showRepository.findByAvailableSeatsGreaterThan(0);
    }

    public Show addShow(Show show) {
        return showRepository.save(show);
    }

    public Show getShowById(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + id));
    }
}