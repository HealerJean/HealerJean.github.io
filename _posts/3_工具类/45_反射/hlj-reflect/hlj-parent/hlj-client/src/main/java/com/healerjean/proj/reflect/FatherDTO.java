package com.healerjean.proj.reflect;

import lombok.Data;

/**
 * @author HealerJean
 * @ClassName FatherDTO
 * @date 2019/11/12  11:26.
 * @Description
 */
@Data
public class FatherDTO extends GrandFatherDTO {

    private Long privateId;

    private String privateName;

    private String privatefatherVar;

    public Long publicId;

    public String publicName;

    public String publicFatherVar;
}
