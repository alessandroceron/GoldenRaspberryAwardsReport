package com.texo.goldenraspberryawardsreport.service;

import com.texo.goldenraspberryawardsreport.entity.Movie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
