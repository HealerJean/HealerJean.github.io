package com.healerjean.proj.common.page;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName PageDTO
 * @Author HealerJean
 * @Date 2019/5/29 14:20
 * @Description 分页数据传输
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageDTO<T> {

    public PageDTO(Integer pageNo, Integer pageSize, Integer total, Integer totalPage, List<T> datas) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
        this.totalPage = totalPage;
        this.datas = datas;
    }

    public PageDTO(Integer minId, List<T> datas) {
        this.minId = minId;
        this.datas = datas;
        this.pageNo = null;
        this.pageSize = null;
        this.total = null;
    }

    public PageDTO() {

    }

    public PageDTO(List<T> datas) {
        this.datas = datas;
    }

    @ApiModelProperty(value = "作为请求地址中获取下一页的参数值")
    private Integer minId;
    @ApiModelProperty(value = "'作为请求地址中获取下一页联盟超级搜数据的参数值'")
    private String tbP;
    @ApiModelProperty(value = "'页码，默认是第一页'")
    private Integer pageNo = 1;
    @ApiModelProperty(value = "'每页显示的记录数，默认是10'")
    private Integer pageSize = 10;
    @ApiModelProperty(value = "'总记录数'")
    private Integer total;
    @ApiModelProperty(value = "'总页数'")
    private Integer totalPage;
    /**
     * 对应的当前页记录
     */
    private List<T> datas;
}
