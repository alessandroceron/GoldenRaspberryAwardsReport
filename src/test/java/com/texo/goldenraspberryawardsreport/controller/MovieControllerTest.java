package com.texo.goldenraspberryawardsreport.controller;

import com.texo.goldenraspberryawardsreport.entity.Movie;
import com.texo.goldenraspberryawardsreport.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void validadeFindMinAndMaxWinner() throws Exception {
        createDb();

        mvc.perform(get("/goldenraspberryawards/minandmaxwinners")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("min.size()", is(3)))

                .andExpect(jsonPath("min[0].producer", is("Produtor 4")))
                .andExpect(jsonPath("min[0].interval", is(2)))
                .andExpect(jsonPath("min[0].previousWin", is(2004)))
                .andExpect(jsonPath("min[0].followingWin", is(2006)))

                .andExpect(jsonPath("min[1].producer", is("Produtor 4")))
                .andExpect(jsonPath("min[1].interval", is(2)))
                .andExpect(jsonPath("min[1].previousWin", is(2006)))
                .andExpect(jsonPath("min[1].followingWin", is(2008)))

                .andExpect(jsonPath("min[2].producer", is("Produtor 2")))
                .andExpect(jsonPath("min[2].interval", is(2)))
                .andExpect(jsonPath("min[2].previousWin", is(2001)))
                .andExpect(jsonPath("min[2].followingWin", is(2003)))

                .andExpect(jsonPath("max.size()", is(2)))

                .andExpect(jsonPath("max[0].producer", is("Produtor 3")))
                .andExpect(jsonPath("max[0].interval", is(5)))
                .andExpect(jsonPath("max[0].previousWin", is(2002)))
                .andExpect(jsonPath("max[0].followingWin", is(2007)))

                .andExpect(jsonPath("max[1].producer", is("Produtor 1")))
                .andExpect(jsonPath("max[1].interval", is(5)))
                .andExpect(jsonPath("max[1].previousWin", is(2000)))
                .andExpect(jsonPath("max[1].followingWin", is(2005)));
    }

    private void createDb() {
        movieRepository.deleteAll();
        List<Movie> movies = Arrays.asList(
                Movie.builder().year(2000).title("Titulo 1").studios("Estúdio 1").producers("Produtor 1").winner(true).build(),
                Movie.builder().year(2000).title("Titulo 2").studios("Estúdio 2").producers("Produtor 2").winner(false).build(),
                Movie.builder().year(2000).title("Titulo 3").studios("Estúdio 3").producers("Produtor 3").winner(false).build(),
                Movie.builder().year(2000).title("Titulo 4").studios("Estúdio 4").producers("Produtor 4").winner(false).build(),

                Movie.builder().year(2001).title("Titulo 1").studios("Estúdio 1").producers("Produtor 1").winner(false).build(),
                Movie.builder().year(2001).title("Titulo 2").studios("Estúdio 2").producers("Produtor 2").winner(true).build(),
                Movie.builder().year(2001).title("Titulo 3").studios("Estúdio 3").producers("Produtor 3").winner(false).build(),
                Movie.builder().year(2001).title("Titulo 4").studios("Estúdio 4").producers("Produtor 4").winner(false).build(),

                Movie.builder().year(2002).title("Titulo 1").studios("Estúdio 1").producers("Produtor 1").winner(false).build(),
                Movie.builder().year(2002).title("Titulo 2").studios("Estúdio 2").producers("Produtor 2").winner(false).build(),
                Movie.builder().year(2002).title("Titulo 3").studios("Estúdio 3").producers("Produtor 3").winner(true).build(),
                Movie.builder().year(2002).title("Titulo 4").studios("Estúdio 4").producers("Produtor 4").winner(false).build(),

                Movie.builder().year(2003).title("Titulo 1").studios("Estúdio 1").producers("Produtor 1").winner(false).build(),
                Movie.builder().year(2003).title("Titulo 2").studios("Estúdio 2").producers("Produtor 2").winner(true).build(),
                Movie.builder().year(2003).title("Titulo 3").studios("Estúdio 3").producers("Produtor 3").winner(false).build(),
                Movie.builder().year(2003).title("Titulo 4").studios("Estúdio 4").producers("Produtor 4").winner(false).build(),

                Movie.builder().year(2004).title("Titulo 1").studios("Estúdio 1").producers("Produtor 1").winner(false).build(),
                Movie.builder().year(2004).title("Titulo 2").studios("Estúdio 2").producers("Produtor 2").winner(false).build(),
                Movie.builder().year(2004).title("Titulo 3").studios("Estúdio 3").producers("Produtor 3").winner(false).build(),
                Movie.builder().year(2004).title("Titulo 4").studios("Estúdio 4").producers("Produtor 4").winner(true).build(),

                Movie.builder().year(2005).title("Titulo 1").studios("Estúdio 1").producers("Produtor 1").winner(true).build(),
                Movie.builder().year(2005).title("Titulo 2").studios("Estúdio 2").producers("Produtor 2").winner(false).build(),
                Movie.builder().year(2005).title("Titulo 3").studios("Estúdio 3").producers("Produtor 3").winner(false).build(),
                Movie.builder().year(2005).title("Titulo 4").studios("Estúdio 4").producers("Produtor 4").winner(false).build(),

                Movie.builder().year(2006).title("Titulo 1").studios("Estúdio 1").producers("Produtor 1").winner(false).build(),
                Movie.builder().year(2006).title("Titulo 2").studios("Estúdio 2").producers("Produtor 2").winner(false).build(),
                Movie.builder().year(2006).title("Titulo 3").studios("Estúdio 3").producers("Produtor 3").winner(false).build(),
                Movie.builder().year(2006).title("Titulo 4").studios("Estúdio 4").producers("Produtor 4").winner(true).build(),

                Movie.builder().year(2007).title("Titulo 1").studios("Estúdio 1").producers("Produtor 1").winner(false).build(),
                Movie.builder().year(2007).title("Titulo 2").studios("Estúdio 2").producers("Produtor 2").winner(false).build(),
                Movie.builder().year(2007).title("Titulo 3").studios("Estúdio 3").producers("Produtor 3").winner(true).build(),
                Movie.builder().year(2007).title("Titulo 4").studios("Estúdio 4").producers("Produtor 4").winner(false).build(),

                Movie.builder().year(2008).title("Titulo 1").studios("Estúdio 1").producers("Produtor 1").winner(false).build(),
                Movie.builder().year(2008).title("Titulo 2").studios("Estúdio 2").producers("Produtor 2").winner(false).build(),
                Movie.builder().year(2008).title("Titulo 3").studios("Estúdio 3").producers("Produtor 3").winner(false).build(),
                Movie.builder().year(2008).title("Titulo 4").studios("Estúdio 4").producers("Produtor 4").winner(true).build()
        );
        movieRepository.saveAll(movies);
    }
}
