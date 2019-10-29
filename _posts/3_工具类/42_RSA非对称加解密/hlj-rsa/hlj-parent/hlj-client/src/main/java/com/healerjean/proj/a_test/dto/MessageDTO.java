package com.healerjean.proj.a_test.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;


/**
 * @author HealerJean
 * @ClassName MessageDTO
 * @date 2019/10/29  14:22.
 * @Description
 */
@Data
@JacksonXmlRootElement(localName = "MESSAGE")
public class MessageDTO {

    @JacksonXmlProperty(localName = "BASIC_INFO")
    private BasicInfo basicInfo;

    @JacksonXmlProperty(localName = "BUSINESS_DATA")
    private BussinssData bussinssData;

}
