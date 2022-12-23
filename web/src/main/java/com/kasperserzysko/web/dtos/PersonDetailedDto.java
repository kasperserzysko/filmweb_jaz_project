package com.kasperserzysko.web.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PersonDetailedDto {

    private String firstName;
    private String lastName;
    private String biography;
    private LocalDate birthday;
    private LocalDate deathday;
}
