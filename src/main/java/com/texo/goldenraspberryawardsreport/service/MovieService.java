package com.texo.goldenraspberryawardsreport.service;

import com.opencsv.bean.CsvToBeanBuilder;
import com.texo.goldenraspberryawardsreport.builder.MovieBuilder;
import com.texo.goldenraspberryawardsreport.dto.MovieDto;
import com.texo.goldenraspberryawardsreport.entity.Movie;
import com.texo.goldenraspberryawardsreport.repository.MovieRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileReader;
import java.io.Reader;
import java.util.List;

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

    public Iterable<Movie> listAll() {
        return movieRepository.findAll();
    }

    public void delete(Movie movie) {
        movieRepository.delete(movie);
    }

    public void delete(String id) {
        movieRepository.deleteById(id);
    }

    public void saveInfosByCsv(){
        // primeiro vamos limpar o banco para inserir os novos dados
        movieRepository.deleteAll();

        //buscamos os dados através do arquivo csv
        movieRepository.saveAll(buildMoviesByCsv());
    }


    private List<Movie> buildMoviesByCsv(){
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
