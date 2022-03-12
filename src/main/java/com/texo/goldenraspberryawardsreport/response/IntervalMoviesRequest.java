package com.texo.goldenraspberryawardsreport.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntervalMoviesRequest {

    private String producer;
    private Integer interval;
    private Integer previousWin;
    private Integer followingWin;
}
