package com.cinema.project.service;

import com.cinema.project.dto.MovieDto;
import com.cinema.project.dto.MovieListResponse;
//import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.stream.Collectors;

import java.util.List;

@Service
public class TmdbService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    // 현재 상영작 목록
    public  MovieListResponse getNowPlayingMovies() {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org/3/movie/now_playing")
                .queryParam("api_key", apiKey)
                .queryParam("language", "ko-KR")
                .queryParam("page", 1)
                .toUriString();

        return restTemplate.getForObject(url, MovieListResponse.class);
    }

    // 영화 제목 검색
    public MovieListResponse searchMoviesByTitle(String title) {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org/3/search/movie")
                .queryParam("api_key", apiKey)
                .queryParam("language", "ko-KR")
                .queryParam("query", title)
                .toUriString();

        return restTemplate.getForObject(url, MovieListResponse.class);
    }

    public MovieDto getMovieDetail(Long movieId) {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org/3/movie/" + movieId)
                .queryParam("api_key", apiKey)
                .queryParam("language", "ko-KR")
                .toUriString();

        return restTemplate.getForObject(url, MovieDto.class);
    }

    public MovieDto getMovieDetailWithCredits(Long movieId) {
        // 기본 영화 정보
        MovieDto movie = getMovieDetail(movieId);  // 기존 기본 정보

        // 크레딧 정보 가져오기 - 이제 MovieCreditDto는 사용하지 않음
        String creditUrl = UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org/3/movie/" + movieId + "/credits")
                .queryParam("api_key", apiKey)
                .queryParam("language", "ko-KR")
                .toUriString();

        // MovieCreditDto 대신 직접 받아서 처리
        Map<String, Object> credits = restTemplate.getForObject(creditUrl, Map.class);


        List<Map<String, Object>> crewList = (List<Map<String, Object>>) credits.get("crew");
        List<Map<String, Object>> castList = (List<Map<String, Object>>) credits.get("cast");

        // 감독 추출 (crew 리스트에서 Director를 찾기)
        String director = crewList.stream()
                .filter(c -> "Director".equals(c.get("job")))
                .map(c -> (String) c.get("name"))
                .findFirst()
                .orElse("정보 없음");

        // 출연진 상위 5명 추출
        List<String> actors = castList.stream()
                .limit(5)
                .map(c -> (String) c.get("name"))
                .collect(Collectors.toList());

        // MovieDto에 감독, 출연진 정보 설정
        movie.setDirector(director);
        movie.setActors(actors);

        return movie;
    }




}
