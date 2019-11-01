package com.healerjean.proj.a_test.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.healerjean.proj.a_test.json.JsonDemoDTO.User;

import com.healerjean.proj.util.json.JsonUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author HealerJean
 * @ClassName JsonDemoDTO
 * @date 2019/10/29  16:22.
 * @Description
 */
@Data
@Accessors(chain = true)
public class JsonDemoDTO {

    private String reqSn;
    private int code;
    private String msg;
    private Date transDate;
    private Integer integer;
    private BigDecimal bigDecimal;
    private User user;
    private List<String> strList;
    private List<Company> companys;

    @Data
    @Accessors(chain = true)
    public static class Company {
        private Long companyId;
        private String companyName;
    }

    @Data
    @Accessors(chain = true)
    public static class User {
        private Long userId;
        private String userName;
    }


    public static JsonDemoDTO jsonDemo() {
        JsonDemoDTO jsonDemoDTO = new JsonDemoDTO();
        jsonDemoDTO.setReqSn(UUID.randomUUID().toString().replace("-", ""));
        jsonDemoDTO.setMsg("Success");
        jsonDemoDTO.setCode(200);
        jsonDemoDTO.setTransDate(new Date());
        jsonDemoDTO.setBigDecimal(new BigDecimal(100));
        jsonDemoDTO.setInteger(100);
        jsonDemoDTO.setStrList(Arrays.asList(new String[]{"奔驰", "宝马"}));
        jsonDemoDTO.setUser(new User().setUserId(1L).setUserName("HealerJean"));
        List<Company> companies = Arrays.asList(new Company[]{
                new Company().setCompanyId(1L).setCompanyName("汽车公司"),
                new Company().setCompanyId(2L).setCompanyName("房产公司")});
        jsonDemoDTO.setCompanys(companies);
        return jsonDemoDTO;
    }

    public static String jsonString() {
        String json = JsonUtils.toJsonString(jsonDemo());
        return json;
    }



    @Test
    public   void packageRequestParams() {
        StringBuffer jsonBuffer = new StringBuffer();
        /**
         * 拼接系统参数
         */
        jsonBuffer.append("{");

        jsonBuffer.append("\"name\":");
        jsonBuffer.append("\"");
        jsonBuffer.append("HealerJean");
        jsonBuffer.append("\",");

        jsonBuffer.append("\"age\":");
        jsonBuffer.append("\"\",");

        jsonBuffer.append("\"sex\":");
        jsonBuffer.append("\"男\"");


        jsonBuffer.append("}");

        JsonNode jsonNode = JsonUtils.toJsonNode(jsonBuffer.toString());
        System.out.println(jsonNode);

        // {"name":"HealerJean","age":"","sex":"男"}
    }
}
