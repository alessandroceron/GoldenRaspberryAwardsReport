package com.texo.goldenraspberryawardsreport.controller;

import com.texo.goldenraspberryawardsreport.response.FastAndLongerWinners;
import com.texo.goldenraspberryawardsreport.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/goldenraspberryawards")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping(path = "/minandmaxwinners", produces = MediaType.APPLICATION_JSON_VALUE)
    public FastAndLongerWinners minAndMaxWinners() {
        return movieService.minAndMaxWinners();
    }
}
