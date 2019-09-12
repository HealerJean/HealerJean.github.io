package com.hlj.proj.service.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.hlj.proj.dto.DateBean;
import com.hlj.proj.utils.JsonUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author HealerJean
 * @ClassName D05Collection
 * @date 2019-08-04  18:56.
 * @Description
 */
public class D05Collection {


    @Test
    public void colect() {


        DateBean one = new DateBean();
        one.setDate(new Date());
        one.setName("HealerJean");
        DateBean two = new DateBean();
        two.setDate(new Date());
        two.setName("zhangcui");
        List<DateBean> list = new ArrayList<>();
        list.add(one);
        list.add(two);
        String jsonSTR = JsonUtils.toJsonString(list);


        List<DateBean> dateBeans = null;
        dateBeans = JsonUtils.toObject(jsonSTR, new TypeReference<List<DateBean>>() {
        });
        System.out.println(dateBeans);

        JavaType javaType = JsonUtils.toJavaType(new TypeReference<List<DateBean>>() {
        });
        dateBeans = JsonUtils.toObject(jsonSTR, javaType);
        System.out.println(dateBeans);

        dateBeans = JsonUtils.jsonToArray(jsonSTR, DateBean.class, ArrayList.class);
        System.out.println(dateBeans);

    }
}
