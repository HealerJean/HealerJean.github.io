package com.healerjean.proj.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @ClassName UcenterFrontMenuMetaDto
 * @Author TD
 * @Date 2019/3/21 11:48
 * @Description
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UcenterFrontMenuMetaDTO {

    private String title;
    private String icon;
}
