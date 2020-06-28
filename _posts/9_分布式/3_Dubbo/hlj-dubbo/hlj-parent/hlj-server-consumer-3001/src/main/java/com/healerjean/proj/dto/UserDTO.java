package com.healerjean.proj.dto;

import lombok.Data;
import lombok.experimental.Accessors;


@Accessors(chain = true)
public class UserDTO {
    private Long id;
    private String name;

}

