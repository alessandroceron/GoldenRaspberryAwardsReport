package com.texo.goldenraspberryawardsreport.service;

import com.opencsv.bean.CsvToBeanBuilder;
import com.texo.goldenraspberryawardsreport.builder.MovieBuilder;
import com.texo.goldenraspberryawardsreport.dto.MovieDto;
import com.texo.goldenraspberryawardsreport.entity.Movie;
import com.texo.goldenraspberryawardsreport.repository.MovieRepository;
import com.texo.goldenraspberryawardsreport.response.IntervalMoviesRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileReader;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private Environment env;

    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    public void saveAll(List<Movie> movies) {
        movieRepository.saveAll(movies);
    }

    public List<IntervalMoviesRequest> findFastestWinners() {
        List<IntervalMoviesRequest> response = new ArrayList<>();
        List<Movie> winners = movieRepository.findWinners();
        Map<String, List<Movie>> producersGroup =
                winners.stream().collect(Collectors.groupingBy(Movie::getProducers));

        producersGroup.forEach((key, value) -> {
            List<Movie> orderedMovies = value
                    .stream()
                    .sorted(Comparator.comparing(Movie::getYear))
                    .collect(Collectors.toList());

            Integer min = null;
            Movie previous = null;
            for (Movie om : orderedMovies) {
                if (previous == null)
                    previous = om;
                else {
                    if (min == null) {
                        min = om.getYear() - previous.getYear();
                        response.add(MovieBuilder.buildIntervalMoviesRequest(previous, om.getYear()));
                    } else {
                        if (min >= om.getYear() - previous.getYear()) {
                            min = om.getYear() - previous.getYear();
                            response.clear();
                            response.add(MovieBuilder.buildIntervalMoviesRequest(previous, om.getYear()));
                        }
                    }
                }
            }
        });
        return response;
    }

    public List<Movie> listAll() {
        Spliterator<Movie> movieSpliterator = movieRepository.findAll().spliterator();
        if (Objects.isNull(movieSpliterator)) return new ArrayList<>();

        return StreamSupport
                .stream(movieSpliterator, false)
                .collect(Collectors.toList());
    }

    public void delete(Movie movie) {
        movieRepository.delete(movie);
    }

    public void delete(String id) {
        movieRepository.deleteById(id);
    }

    public void saveInfosByCsv() {
        // primeiro vamos limpar o banco para inserir os novos dados
        movieRepository.deleteAll();

        //buscamos os dados através do arquivo csv
        movieRepository.saveAll(buildMoviesByCsv());
    }


    private List<Movie> buildMoviesByCsv() {
        return MovieBuilder.buildMoviesList(conversationCsv());
    }

    private List<MovieDto> conversationCsv() {
        return new CsvToBeanBuilder(readingFile())
                .withSeparator(';')
                .withType(MovieDto.class)
                .build().parse();
    }

    // Vou deixar anotado com SneakyThrows pois tenho um teste validando a leitura
    @SneakyThrows
    private Reader readingFile() {
        return new FileReader(ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + env.getProperty("path.file.csv")));
    }
}
