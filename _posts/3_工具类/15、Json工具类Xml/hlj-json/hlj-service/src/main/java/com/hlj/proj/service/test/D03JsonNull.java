package com.hlj.proj.service.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hlj.proj.dto.DateBean;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

/**
 * @author HealerJean
 * @ClassName D03JsonNull
 * @date 2019-08-04  17:36.
 * @Description
 */
public class D03JsonNull {




    /**
     * 6、对象是空或者null的Json转换
     * 6.1、Json中不论是字符串还是对象null，经过objectMapper转对象后还是null，
     *      对象中的null，                经过objectMapper转json后也还是null
     * 6.2、Json中不论是字符串还是对象null  经过JSONObject.fromObject后还是null
     *      对象中如果字符串是null，       经过JSONObject.fromObject后会变成"" ，对象中的对象，（比如date）是null，转换后还是null ；
     */
    @Test
    public void nullOrEmpty() throws IOException {
        DateBean dateBean = new DateBean();
        dateBean.setDate(null);
        dateBean.setName(null);

        // 6.1、Json中不论是字符串还是对象null，经过objectMapper以后原封不动
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonSTR =    objectMapper.writeValueAsString(dateBean);
        System.out.println(jsonSTR);
        //{"name":null,"date":null}       对象中的null，经过objectMapper转json后也还是null
        dateBean = objectMapper.readValue(jsonSTR,DateBean.class);
        System.out.println(dateBean);
        // DateBean(name=null, date=null) Json中不论是字符串还是对象null，经过objectMapper后还是null，


        // 6.2、Json中null的字符串通过 JSONObject.fromObject 后变成 "" ；
        JSONObject jsonObJect =  JSONObject.fromObject(dateBean) ;
        System.out.println(jsonObJect);
        // {"date":null,"name":""}    对象中如果字符串是null，则通过JSONObject.fromObject 后会变成"" ，对重中的对象，（比如date）是null，转换后还是null ；
        jsonObJect =  JSONObject.fromObject(jsonSTR) ;
        System.out.println(jsonObJect);
        // {"name":null,"date":null} Json中不论是字符串还是对象null  经过JSONObject.fromObject后还是null


        // {"name":null,"date":null}
        // DateBean(name=null, date=null)
        // {"date":null,"name":""}
        // {"name":null,"date":null}
    }


    /**
     * 7、配置对象中null的元素转Json后不存在
     */
    @Test
    public  void ignoreNull() throws JsonProcessingException {

        // 1、注解@JsonInclude(JsonInclude.Include.NON_NULL)

        // 2。objectmapper配置
        DateBean dateBean = new DateBean();
        dateBean.setDate(new Date());
        dateBean.setName(null);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonSTR =    objectMapper.writeValueAsString(dateBean);
        System.out.println(jsonSTR);
        // {"date":1564910951087}

    }


}
