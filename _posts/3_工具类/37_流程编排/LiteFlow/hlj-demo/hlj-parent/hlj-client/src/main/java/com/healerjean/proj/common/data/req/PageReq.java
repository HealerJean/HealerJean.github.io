package com.healerjean.proj.common.data.req;

import com.healerjean.proj.common.data.dto.PageQueryDTO;
import lombok.Data;

import java.io.Serializable;


/**
 * PageReq
 *
 * @author zhangyujin
 * @date 2023/6/14  14:24
 */
@Data
public class PageReq<T> implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 467204528695545241L;

    /**
     * 条件对象
     */
    private T data;

    /**
     * pageQuery
     */
    private PageQueryDTO pageQuery;
}
