package com.cinema.project.controller;

import com.cinema.project.dto.MovieDto;
import com.cinema.project.service.TmdbService;
import com.cinema.project.dto.MovieListResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MoviePageController {

    private final TmdbService tmdbService;

    public MoviePageController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/movies/view")
    public String showNowPlayingMovies(Model model) {
        MovieListResponse response = tmdbService.getNowPlayingMovies();
        model.addAttribute("movies", response.getResults()); // 영화 목록 전달
        return "movies"; // templates/movies.html 템플릿을 렌더링
    }

    // 영화 상세 정보 화면
    @GetMapping("/movies/detail/{id}")
    public String showMovieDetail(
            @PathVariable("id") Long id,
            @RequestParam(value = "prev", required = false) String prev,
            Model model) {

        MovieDto movieWithCredits = tmdbService.getMovieDetailWithCredits(id);

        model.addAttribute("movie", movieWithCredits);
        model.addAttribute("prevPage", prev); //
        return "movie-detail";
    }


    // 영화 검색 결과 화면
    @GetMapping("/movies/search-view")
    public String searchMoviesPage(@RequestParam("query") String query, Model model, HttpServletRequest request) {
        if (query == null || query.isBlank()) {
            model.addAttribute("movies", null);
            model.addAttribute("query", "");
        } else {
            MovieListResponse result = tmdbService.searchMoviesByTitle(query);
            model.addAttribute("movies", result.getResults());
            model.addAttribute("query", query);
        }
        String fullUrl = request.getRequestURI();
        if (request.getQueryString() != null) {
            fullUrl += "?" + request.getQueryString();
        }
        model.addAttribute("currentUrl", fullUrl);

        return "movie-search";
    }


}
