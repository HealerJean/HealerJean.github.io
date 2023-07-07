package com.healerjean.proj.utils.db;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author zhangyujin
 * @date 2023/7/5$  15:45$
 */
@Accessors(chain = true)
@Data
public class IdQueryBO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3145985803712258405L;

    /**
     * 是否等于最小
     */
    private Boolean minEqualFlag;
    /**
     * 最小Id
     */
    private Long minId;

    /**
     * 是否等于最大
     */
    private Boolean maxEqualFlag;
    /**
     * 最大Id
     */
    private Long maxId;

    /**
     * 每次查询多少个
     */
    private Long size;

    public IdQueryBO(Long minId, Long maxId, Long size) {
        this.minId = minId;
        this.maxId = maxId;
        this.size = size;
    }

    public IdQueryBO(Boolean minEqualFlag, Long minId, Boolean maxEqualFlag, Long maxId, Long size) {
        this.minEqualFlag = minEqualFlag;
        this.minId = minId;
        this.maxEqualFlag = maxEqualFlag;
        this.maxId = maxId;
        this.size = size;
    }

    public IdQueryBO(Long minId, Long size) {
        this.minId = minId;
        this.size = size;
    }
}
