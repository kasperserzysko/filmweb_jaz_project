package com.kasperserzysko.web.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class GenreDto implements Serializable {

    private Long id;
    private String name;
}
