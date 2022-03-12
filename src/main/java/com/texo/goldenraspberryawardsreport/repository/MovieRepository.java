package com.texo.goldenraspberryawardsreport.repository;

import com.texo.goldenraspberryawardsreport.entity.Movie;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, String> {
}
