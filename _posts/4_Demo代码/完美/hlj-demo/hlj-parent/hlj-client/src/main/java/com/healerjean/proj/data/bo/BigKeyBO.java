package com.healerjean.proj.data.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * BigKeyBO
 *
 * @author zhangyujin
 * @date 2024/2/7
 */
@Accessors(chain = true)
@Data
public class BigKeyBO<T> implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7560782834954336712L;

    /**
     * 小key缓存索引
     */
    private Long minKeyIndex;

    /**
     * 小Key缓存数量
     */
    private Long minValueSize;

    /**
     * 查询是否结束
     */
    private Boolean endFlag;

    /**
     * 待缓存对象
     */
    private List<T> readyList;

    /**
     * 总数
     */
    private Long count;

}
