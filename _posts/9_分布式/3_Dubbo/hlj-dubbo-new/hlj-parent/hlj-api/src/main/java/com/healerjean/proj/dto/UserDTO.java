package com.healerjean.proj.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserDTO  implements Serializable {
    private Long id;
    private String name;
    private String description;

}

