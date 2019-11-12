package com.healerjean.proj.reflect;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HealerJean
 * @ClassName GrandFatherDTO
 * @date 2019/11/12  11:27.
 * @Description
 */
@Data
public class GrandFatherDTO {

    private Long privateId;

    private String privateName;

    private String privateGrandVar;

    public Long publicId;

    public String publicName;

    public String publicGrandVar;
}
