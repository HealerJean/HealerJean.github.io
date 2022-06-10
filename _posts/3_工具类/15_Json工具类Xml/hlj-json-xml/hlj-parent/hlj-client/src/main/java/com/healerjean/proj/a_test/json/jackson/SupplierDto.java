package com.healerjean.proj.a_test.json.jackson;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangyujin
 * @date 2022/6/8  14:14.
 */
@Data
public class SupplierDto implements Serializable {

    private static final long serialVersionUID = -489720058610744525L;

    /**
     * 保司Id
     */
    private String supplierId;

    /**
     * 保司名称
     */
    private String supplierName;

    /**
     * 保司手机号
     */
    private String supplierPhone;

}
