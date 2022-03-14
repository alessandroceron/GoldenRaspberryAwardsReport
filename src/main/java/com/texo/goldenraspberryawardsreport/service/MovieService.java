package com.texo.goldenraspberryawardsreport.service;

import com.opencsv.bean.CsvToBeanBuilder;
import com.texo.goldenraspberryawardsreport.builder.MovieBuilder;
import com.texo.goldenraspberryawardsreport.dto.CurrentWinnersAndIntervalDto;
import com.texo.goldenraspberryawardsreport.dto.MovieDto;
import com.texo.goldenraspberryawardsreport.entity.Movie;
import com.texo.goldenraspberryawardsreport.repository.MovieRepository;
import com.texo.goldenraspberryawardsreport.response.FastAndLongerWinners;
import com.texo.goldenraspberryawardsreport.response.IntervalMoviesResponse;
import com.texo.goldenraspberryawardsreport.utils.FilterWinner;
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

import static java.util.stream.Collectors.toList;

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

    /**
     * Busca os vencedores mais rápidos e com o maior intervalo
     */
    public FastAndLongerWinners minAndMaxWinners() {
        return FastAndLongerWinners.builder()
                .min(findFastestWinners())
                .max(findLongerRangeWinners())
                .build();
    }

    /**
     * Busca os vencedores com o menor intervalo
     */
    public List<IntervalMoviesResponse> findFastestWinners() {
        return filterWinnerByComparator(new FilterWinner.FastestWinners());
    }

    /**
     * Busca os vencedores com o maior intervalo
     */
    public List<IntervalMoviesResponse> findLongerRangeWinners() {
        return filterWinnerByComparator(new FilterWinner.LongerRangeWinners());
    }

    /**
     * Faz a busca conforme a necessidade do filtro, vencedor mais rápido ou com maior intervalo
     */
    private List<IntervalMoviesResponse> filterWinnerByComparator(FilterWinner.Filter filterWinner) {
        return goThroughListMoreOneWinner(filterWinner, new CurrentWinnersAndIntervalDto()).getResponse();
    }

    /**
     * Agrupamos os produtores e já filtramos somente quem ganhou mais de uma vez
     *
     * @return um Dto com os resultado e o intervalo
     */
    private CurrentWinnersAndIntervalDto goThroughListMoreOneWinner(FilterWinner.Filter filterWinner, CurrentWinnersAndIntervalDto current) {
        for (Map.Entry<String, List<Movie>> winner : findMoreOneWin().entrySet()) {
            goThroughOrderedMovies(filterWinner, current, winner);
        }
        return current;
    }

    /**
     * Ordenamos cada agrupamento para garantir a integridade dos cálculos
     */
    private void goThroughOrderedMovies(FilterWinner.Filter filterWinner, CurrentWinnersAndIntervalDto current, Map.Entry<String, List<Movie>> winner) {
        Movie previous = null;
        for (Movie movie : orderedMoviesByYear(winner.getValue())) {
            previous = calculateIntervals(filterWinner, current, previous, movie, winner.getKey());
        }
    }

    private Movie calculateIntervals(FilterWinner.Filter filterWinner, CurrentWinnersAndIntervalDto current, Movie previous, Movie movie, String producer) {
        // primeiro item desse produtor
        if (previous == null)
            return movie;

        var interval = movie.getYear() - previous.getYear();

        // Verificamos se essa é a primeira validação de intervalo
        if (validateFirstInterval(current, previous, movie, producer, interval, current.getCurrentInterval() == null))
            return movie;

        // Verificamos se o novo intervalo é menos que o anterior, se for resetamos o array de movies e o intervalo de controle
        if (validateFirstInterval(current, previous, movie, producer, interval, filterWinner.run(current.getCurrentInterval(), interval)))
            return movie;

        // Se o intervalo é o mesmo do intervalo de controle, adicionamos esse movie
        if (current.getCurrentInterval() == interval)
            addMovieResponse(current, previous, movie, producer, interval, false);

        return movie;
    }

    private boolean validateFirstInterval(CurrentWinnersAndIntervalDto current, Movie previous, Movie movie, String producer, int interval, boolean filterWinnerIsTrue) {
        if (filterWinnerIsTrue) {
            addMovieResponse(current, previous, movie, producer, interval, true);
            return true;
        }
        return false;
    }

    private void addMovieResponse(CurrentWinnersAndIntervalDto current, Movie previous, Movie movie, String producer, int interval, boolean isNewInterval) {
        if (isNewInterval)
            current.setCurrentInterval(interval);

        current.setResponse(addIntervalResponse(current.getResponse(), producer, previous, movie, isNewInterval));
    }

    private List<Movie> orderedMoviesByYear(List<Movie> movies) {
        return movies
                .stream()
                .sorted(Comparator.comparing(Movie::getYear))
                .collect(Collectors.toList());
    }

    /**
     * Buscar todos os ganhadores com mais de uma vitória
     *
     * @return List de ganhadores agrupados em um Map
     */
    private Map<String, List<Movie>> findMoreOneWin() {
        List<Movie> winners = movieRepository.findWinners();
        HashMap<String, List<Movie>> map = new HashMap<>();

        for (String name : buildListProducers(winners)) {
            map.put(name, winners.parallelStream()
                    .filter(winner -> winner.getProducers().contains(name))
                    .collect(Collectors.toList()));
        }
        return map;
    }

    private List<String> buildListProducers(List<Movie> winners) {
        return winners.stream()
                .flatMap(winner -> Arrays.stream(winner.getProducers().split(",| AND | and |;|:|-")))
                .filter(m -> !m.isBlank())
                .map(String::trim)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private List<IntervalMoviesResponse> addIntervalResponse(List<IntervalMoviesResponse> response, String producer, Movie previous, Movie next, boolean clearResponse) {
        if (clearResponse) response = new ArrayList<>();
        response.add(MovieBuilder.buildIntervalMoviesRequest(producer, previous, next.getYear()));
        return response;
    }

    public List<Movie> listAll() {
        Spliterator<Movie> movieSpliterator = movieRepository.findAll().spliterator();
        if (Objects.isNull(movieSpliterator)) return new ArrayList<>();

        return StreamSupport
                .stream(movieSpliterator, false)
                .collect(toList());
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
