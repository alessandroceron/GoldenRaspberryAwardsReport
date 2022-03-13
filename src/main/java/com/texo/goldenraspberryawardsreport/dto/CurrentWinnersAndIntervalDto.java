package com.texo.goldenraspberryawardsreport.dto;

import com.texo.goldenraspberryawardsreport.response.IntervalMoviesResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CurrentWinnersAndIntervalDto {
    private List<IntervalMoviesResponse> response;
    private Integer currentInterval;

    public CurrentWinnersAndIntervalDto() {
        this.response = new ArrayList<>();
        this.currentInterval = null;
    }
}
