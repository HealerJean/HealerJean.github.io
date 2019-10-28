package com.hlj.proj.service.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hlj.proj.dto.DateBean;
import com.hlj.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName D02DteErrorJson
 * @date 2019-08-04  16:14.
 * 1、只要是经过JSONObject的对象， date就会走样,但是我们的对象到了前端之后，就会帮我们变成long类型，这是由于HttpMessageCover帮我们做的
 */
@Slf4j
public class D02DteErrorJson {


    /**
     * 3、带有String类型的日期2018-12-13 20:31:04 转换为 Date类型的对象会报错，默认是Json是Long类型的才能成功，其实就是相当于序列化和反序列化以后都是Long类型的
     */
    @Test
    public void testStringDateOrLong() throws IOException {

        // 报错 String dateJsonSTR = "{\"name\":\"HealerJean\",\"date\":2018-12-13 20:31:04}\n" ;
        String dateJsonSTR = "{\"name\":\"HealerJean\",\"date\":1544782308409}\n";

        ObjectMapper objectMapper = new ObjectMapper();
        DateBean dateBean = objectMapper.readValue(dateJsonSTR, DateBean.class);

    }


    /**
     * 4、JSONObject  会将对象中具有date的对象变质
     * 4.1、不转换为Long类型的，反而转换为一堆参数，也就是说它将date内部进行转换了
     * 4.2、传递到前台是正常的
     */
    @Test
    public void jsonObjectDateERROR() throws IOException {

        DateBean dateBean = new DateBean();
        dateBean.setDate(new Date());
        dateBean.setName("HealerJean");

        System.out.println(JSONObject.fromObject(dateBean));
        // {"date":{"date":4,"day":0,"hours":16,"minutes":42,"month":7,"seconds":21,"time":1564908141006,"timezoneOffset":-480,"year":119},"name":"HealerJean"}


        Map<String, Object> map = (Map<String, Object>) JSONObject.fromObject(dateBean);
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(map));
        // 下面这里发现date 变质了，但是传递到前台是正常的
        // {"date":{"date":4,"day":0,"hours":16,"minutes":47,"month":7,"seconds":38,"time":1564908458283,"timezoneOffset":-480,"year":119},"name":"HealerJean"}
    }


    /**
     * 5、对象转Map 、Map转JavaBean
     * 对象转Map ：现将对象转换为Json，在将Json转换为map
     * Map转JavaBean ：map转换为Json，再讲Json转换为JavaBean
     */
    @Test
    public void MapTobaen() {

        DateBean dateBean = new DateBean();
        dateBean.setDate(new Date());
        dateBean.setName("HealerJean");

        Map<String, Object> map = JsonUtils.toObject(JsonUtils.toJsonString(dateBean), new TypeReference<Map<String, Object>>() {
        });
        System.out.println(map);

        String mapJsonSTR = JsonUtils.toJsonString(map);
        dateBean = JsonUtils.toObject(mapJsonSTR, DateBean.class);
        System.out.println(dateBean);


    }


}
