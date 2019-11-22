package com.hlj.proj.dto.Demo;

import lombok.Data;

import java.util.List;

/**
 * @author HealerJean
 * @ClassName SpaceDTO
 * @date 2019/11/21  19:55.
 * @Description
 */
@Data
public class SpaceDTO {

    private String space;

    private List<String> listSpace;

    private List<Integer> integers;

    private Integer integer;
}
