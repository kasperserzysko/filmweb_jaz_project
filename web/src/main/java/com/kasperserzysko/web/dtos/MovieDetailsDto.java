package com.kasperserzysko.web.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MovieDetailsDto {

    private String title;
    private String description;
    private LocalDate premiere;
    private int length;
}
