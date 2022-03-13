package com.texo.goldenraspberryawardsreport.repository;

import com.texo.goldenraspberryawardsreport.entity.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, String> {

//    @Query("select m from Movie m")
    @Query("select m from Movie m where m.winner = true")
    List<Movie> findWinners();
}
