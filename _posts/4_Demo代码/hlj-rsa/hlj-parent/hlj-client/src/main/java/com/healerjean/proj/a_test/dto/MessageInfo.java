package com.healerjean.proj.a_test.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * @author HealerJean
 * @ClassName MessageInfo
 * @date 2019-10-28  23:07.
 * @Description
 */
@JacksonXmlRootElement(localName = "MESSAGE_INFO")
@Data
public class MessageInfo {


    private int retCode ;

    private String retMsg;

    private String signedMsg ;

}
