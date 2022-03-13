package com.texo.goldenraspberryawardsreport.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FastAndLongerWinners {

    private List<IntervalMoviesResponse> min;
    private List<IntervalMoviesResponse> max;
}
