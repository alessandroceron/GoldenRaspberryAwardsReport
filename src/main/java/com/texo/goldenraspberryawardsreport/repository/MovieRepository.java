package com.texo.goldenraspberryawardsreport.repository;

import com.texo.goldenraspberryawardsreport.entity.Movie;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MovieRepository extends PagingAndSortingRepository<Movie, String> {
}
