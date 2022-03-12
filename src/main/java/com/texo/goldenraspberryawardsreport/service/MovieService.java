package com.texo.goldenraspberryawardsreport.service;

import com.opencsv.bean.CsvToBeanBuilder;
import com.texo.goldenraspberryawardsreport.builder.MovieBuilder;
import com.texo.goldenraspberryawardsreport.dto.MovieDto;
import com.texo.goldenraspberryawardsreport.entity.Movie;
import com.texo.goldenraspberryawardsreport.repository.MovieRepository;
import com.texo.goldenraspberryawardsreport.response.IntervalMoviesResponse;
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

    public List<IntervalMoviesResponse> findFastestWinners() {
        List<IntervalMoviesResponse> response = new ArrayList<>();

        Integer min = null;
        for (Map.Entry<String, List<Movie>> winners : findMoreOneWin()) {

            Movie previous = null;
            for (Movie movie : orderedMoviesByYear(winners.getValue())) {
                if (previous == null)
                    previous = movie;
                else {
                    var interval = movie.getYear() - previous.getYear();
                    if (min == null) {
                        min = interval;
                        response = addIntervalResponse(response, previous, movie, true);
                    } else {
                        if (min > interval) {
                            min = interval;
                            response = addIntervalResponse(response, previous, movie, true);
                        } else if (min == interval){
                            response = addIntervalResponse(response, previous, movie, false);
                        }
                    }
                }
            }
        }
        return response;
    }

    private List<Movie> orderedMoviesByYear(List<Movie> movies) {
        return movies
                .stream()
                .sorted(Comparator.comparing(Movie::getYear))
                .collect(Collectors.toList());
    }

    /**
     * Buscar todos os ganhadores com mais de uma vitória
     * @return List de ganhadores agrupados em um Map
     */
    private List<Map.Entry<String, List<Movie>>> findMoreOneWin() {
        return movieRepository.findWinners().stream()
                .collect(Collectors.groupingBy(Movie::getProducers))
                .entrySet().stream()
                .filter(pg -> pg.getValue().size() > 1)
                .collect(Collectors.toList());
    }

    private List<IntervalMoviesResponse> addIntervalResponse(List<IntervalMoviesResponse> response, Movie previous, Movie next, boolean clearResponse) {
        if (clearResponse) response = new ArrayList<>();
        response.add(MovieBuilder.buildIntervalMoviesRequest(previous, next.getYear()));
        return response;
    }

    public List<IntervalMoviesResponse> findLongerRangeWinners() {
        List<IntervalMoviesResponse> response = new ArrayList<>();

        Integer max = null;
        for (Map.Entry<String, List<Movie>> winners : findMoreOneWin()) {

            Movie previous = null;
            for (Movie movie : orderedMoviesByYear(winners.getValue())) {
                if (previous == null)
                    previous = movie;
                else {
                    var interval = movie.getYear() - previous.getYear();
                    if (max == null) {
                        max = interval;
                        response = addIntervalResponse(response, previous, movie, true);
                    } else {
                        if (max < interval) {
                            max = interval;
                            response = addIntervalResponse(response, previous, movie, true);
                        } else if (max == interval){
                            response = addIntervalResponse(response, previous, movie, false);
                        }
                    }
                }
            }
        }
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
