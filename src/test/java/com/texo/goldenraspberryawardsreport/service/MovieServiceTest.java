package com.texo.goldenraspberryawardsreport.service;

import com.texo.goldenraspberryawardsreport.entity.Movie;
import com.texo.goldenraspberryawardsreport.response.IntervalMoviesRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @Test
    void saveInfosByCsv() {
        movieService.saveInfosByCsv();

        Iterable<Movie> movies = movieService.listAll();
        Assertions.assertNotNull(movies);
    }

    @Test
    void findFastestWinners(){
        List<IntervalMoviesRequest> fastestWinners = movieService.findFastestWinners();
        Assertions.assertNotNull(fastestWinners);
        fastestWinners.forEach(System.out::println);
    }

    @Test
    void findLongerRangeWinners(){
        List<IntervalMoviesRequest> longerRangeWinners = movieService.findLongerRangeWinners();
        Assertions.assertNotNull(longerRangeWinners);
        longerRangeWinners.forEach(System.out::println);
    }

    @Test
    void findFastestAndLongerRangeWinners(){
        findFastestWinners();
        findLongerRangeWinners();
    }
}
