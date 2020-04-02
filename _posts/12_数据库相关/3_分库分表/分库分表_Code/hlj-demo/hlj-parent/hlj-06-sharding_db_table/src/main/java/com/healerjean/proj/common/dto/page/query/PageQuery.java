package com.healerjean.proj.common.dto.page.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 *
 */
@Data
public class PageQuery {

    @ApiModelProperty(value = "开始页数，从1开始,默认为1", dataType = "java.lang.Integer", hidden = true)
    @JsonIgnore
    private Integer pageNo = 1;
    @ApiModelProperty(value = "每页数量，默认为5", dataType = "java.lang.Integer", hidden = true)
    @JsonIgnore
    private Integer pageSize = 5;

    /**
     * 是否分页，默认false不分页
     */
    @ApiModelProperty(value = "是否分页，默认false不分页", hidden = true)
    @JsonIgnore
    private Boolean page = false;

}
