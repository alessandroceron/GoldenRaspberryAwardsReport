package com.texo.goldenraspberryawardsreport.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class Movie {

    @Id
    private String id;

    @NotNull
    private Integer year;

    @NotNull
    private String title;

    @NotNull
    private String studios;

    @NotNull
    private String producers;

    private Boolean winner;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected LocalDateTime createdDate;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected LocalDateTime lastmodifiedDate;
}
