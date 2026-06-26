package com.utkarsh.movie_service.controller;

import com.utkarsh.movie_service.entity.Show;
import com.utkarsh.movie_service.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    // GET /api/shows
    @GetMapping
    public ResponseEntity<List<Show>> getAllShows() {
        return ResponseEntity.ok(showService.getAllShows());
    }

    // GET /api/shows/movie/1
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Show>> getShowsByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(showService.getShowsByMovie(movieId));
    }

    // GET /api/shows/available
    @GetMapping("/available")
    public ResponseEntity<List<Show>> getAvailableShows() {
        return ResponseEntity.ok(showService.getAvailableShows());
    }

    // GET /api/shows/1
    @GetMapping("/{id}")
    public ResponseEntity<Show> getShowById(@PathVariable Long id) {
        return ResponseEntity.ok(showService.getShowById(id));
    }

    // POST /api/shows
    @PostMapping
    public ResponseEntity<Show> addShow(@RequestBody Show show) {
        return ResponseEntity.ok(showService.addShow(show));
    }
}