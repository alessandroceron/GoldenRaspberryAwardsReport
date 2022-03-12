package com.texo.goldenraspberryawardsreport;

import com.texo.goldenraspberryawardsreport.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class GoldenRaspberryAwardsReportApplication {

    @Autowired
    private MovieService movieService;

    public static void main(String[] args) {
        SpringApplication.run(GoldenRaspberryAwardsReportApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        movieService.saveInfosByCsv();
    }
}
