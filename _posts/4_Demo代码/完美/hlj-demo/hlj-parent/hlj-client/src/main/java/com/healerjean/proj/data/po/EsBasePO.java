package com.healerjean.proj.data.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * es 基础实体
 */
@Accessors(chain = true)
@Data
public class EsBasePO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7526916195085428595L;

    /**
     * uuid
     */
    public String uuid;

}
