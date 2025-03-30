package com.cinema.project.controller;

import com.cinema.project.dto.MovieListResponse;
import com.cinema.project.service.TmdbService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final TmdbService tmdbService;

    public MovieController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    // 현재 상영작 목록
    @GetMapping("/now-playing")
    public MovieListResponse getNowPlayingMovies() {
        return tmdbService.getNowPlayingMovies();
    }

    // 영화 제목 검색
    @GetMapping("/search")
    public MovieListResponse searchMovies(@RequestParam("query") String query) {
        return tmdbService.searchMoviesByTitle(query);
    }


}
