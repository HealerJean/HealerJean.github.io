package com.healerjean.proj.service.rsa.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * @author HealerJean
 * @ClassName ResponBean
 * @date 2019/10/28  20:27.
 * @Description
 */
@Data
@JacksonXmlRootElement(localName = "RESPON_BEAN")
public class ResponBean {

    private String trxCode;

    private DataBean dataBean;

    private String signedMsg;

    private int retCode;
    private String errMsg;


    @Data
    public static class DataBean {

        private String name;


    }
}
