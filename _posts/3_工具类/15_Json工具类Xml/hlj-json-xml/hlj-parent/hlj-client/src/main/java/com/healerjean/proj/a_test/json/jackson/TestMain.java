package com.healerjean.proj.a_test.json.jackson;

import com.healerjean.proj.util.json.JsonUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyujin
 * @date 2022/6/8  16:20.
 */
public class TestMain {


    @Test
    public void test(){
        String json = "[\n" +
                "    {\n" +
                "        \"supplierId\": \"1016151\",\n" +
                "        \"supplierName\": \"京东安联财产保险有限公司北京分公司\",\n" +
                "        \"supplierPhone\": \"4008002020\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplierId\": \"1030033\",\n" +
                "        \"supplierName\": \"中国太平洋财险保险股份有限公司深圳分公司\",\n" +
                "        \"supplierPhone\": \"0755-22695215\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplierId\": \"3115659\",\n" +
                "        \"supplierName\": \"华农财产保险股份有限公司\",\n" +
                "        \"supplierPhone\": \"4000100000\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplierId\": \"3191757\",\n" +
                "        \"supplierName\": \"太平财产保险有限公司北京分公司\",\n" +
                "        \"supplierPhone\": \"010-83253227\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplierId\": \"11211352\",\n" +
                "        \"supplierName\": \"中国平安财产保险股份有限公司深圳分公司\",\n" +
                "        \"supplierPhone\": \"95511\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplierId\": \"134\",\n" +
                "        \"supplierName\": \"中国人寿财产保险股份有限公司北京市分公司\",\n" +
                "        \"supplierPhone\": \"010-65602366-8016\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplierId\": \"11216195\",\n" +
                "        \"supplierName\": \"紫金财产保险股份有限公司浙江分公司\",\n" +
                "        \"supplierPhone\": \"95312\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplierId\": \"139\",\n" +
                "        \"supplierName\": \"中国大地财产保险股份有限公司北京分公司\",\n" +
                "        \"supplierPhone\": \"010-82277957\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplierId\": \"42826\",\n" +
                "        \"supplierName\": \"中国人民财产保险股份有限公司\",\n" +
                "        \"supplierPhone\": \"95518\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"supplierId\": \"1002841\",\n" +
                "        \"supplierName\": \"泰康在线财产保险股份有限公司\",\n" +
                "        \"supplierPhone\": \"95522-3\"\n" +
                "    }\n" +
                "]";


        List<SupplierDto> supplierDtos = JsonUtils.toArrayList(json, SupplierDto.class);
        Map<String, SupplierDto> map = new HashMap();
        supplierDtos.stream().forEach(item->{
            map.put(item.getSupplierId(),item);
            item.setSupplierId(null);
        });
        System.out.println(JsonUtils.toJsonString(map));
    }
}
