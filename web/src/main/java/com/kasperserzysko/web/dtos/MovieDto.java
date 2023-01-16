package com.kasperserzysko.web.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MovieDto implements Serializable {

    private Long id;
    private String title;
}
