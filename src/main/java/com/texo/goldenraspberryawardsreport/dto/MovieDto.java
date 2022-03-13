package com.texo.goldenraspberryawardsreport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {

    private Integer year;
    private String title;
    private String studios;
    private String producers;
    private Boolean winner;
}
