package com.texo.goldenraspberryawardsreport.builder;

import com.texo.goldenraspberryawardsreport.dto.MovieDto;
import com.texo.goldenraspberryawardsreport.entity.Movie;
import com.texo.goldenraspberryawardsreport.response.IntervalMoviesResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MovieBuilder {

    private MovieBuilder() {
    }

    public static List<Movie> buildMoviesList(List<MovieDto> moviesDto) {
        return moviesDto.stream()
                .map(MovieBuilder::buildMovie)
                .collect(Collectors.toList());
    }

    private static Movie buildMovie(MovieDto movieDto) {
        return Movie.builder()
                .year(movieDto.getYear())
                .title(movieDto.getTitle())
                .studios(movieDto.getStudios())
                .producers(movieDto.getProducers())
                .winner(movieDto.getWinner() != null ? movieDto.getWinner() : Boolean.FALSE)
                .build();
    }

    public static IntervalMoviesResponse buildIntervalMoviesRequest(Movie movie, Integer nextYear) {
        return IntervalMoviesResponse.builder()
                .producer(movie.getProducers())
                .interval(nextYear - movie.getYear())
                .previousWin(movie.getYear())
                .followingWin(nextYear)
                .build();
    }
}
