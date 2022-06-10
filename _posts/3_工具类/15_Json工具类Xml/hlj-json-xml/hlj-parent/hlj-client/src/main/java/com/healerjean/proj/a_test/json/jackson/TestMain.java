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
        //测试
        // String json = "[{\"id\":\"1016151\",\"name\":\"京东安联财产保险有限公司北京分公司\",\"phone\":\"4008002020\"},{\"id\":\"1030033\",\"name\":\"中国太平洋财险保险股份有限公司深圳分公司\",\"phone\":\"0755-22695215\"},{\"id\":\"3115659\",\"name\":\"华农财产保险股份有限公司\",\"phone\":\"4000100000\"},{\"id\":\"11252859\",\"name\":\"太平财产保险有限公司北京分公司\",\"phone\":\"010-83253227\"},{\"id\":\"11284905\",\"name\":\"中国平安财产保险股份有限公司深圳分公司\",\"phone\":\"95511\"},{\"id\":\"134\",\"name\":\"中国人寿财产保险股份有限公司北京市分公司\",\"phone\":\"010-65602366-8016\"},{\"id\":\"11221724\",\"name\":\"紫金财产保险股份有限公司浙江分公司\",\"phone\":\"95312\"},{\"id\":\"139\",\"name\":\"中国大地财产保险股份有限公司北京分公司\",\"phone\":\"010-82277957\"},{\"id\":\"146\",\"name\":\"中国人民财产保险股份有限公司\",\"phone\":\"95518\"},{\"id\":\"147\",\"name\":\"泰康在线财产保险股份有限公司\",\"phone\":\"95522-3\"}]";

        //生产
        String json = "[{\"id\":\"1016151\",\"name\":\"京东安联财产保险有限公司北京分公司\",\"phone\":\"4008002020\"},{\"id\":\"1030033\",\"name\":\"中国太平洋财险保险股份有限公司深圳分公司\",\"phone\":\"0755-22695215\"},{\"id\":\"3115659\",\"name\":\"华农财产保险股份有限公司\",\"phone\":\"4000100000\"},{\"id\":\"3191757\",\"name\":\"太平财产保险有限公司北京分公司\",\"phone\":\"010-83253227\"},{\"id\":\"11211352\",\"name\":\"中国平安财产保险股份有限公司深圳分公司\",\"phone\":\"95511\"},{\"id\":\"134\",\"name\":\"中国人寿财产保险股份有限公司北京市分公司\",\"phone\":\"010-65602366-8016\"},{\"id\":\"11216195\",\"name\":\"紫金财产保险股份有限公司浙江分公司\",\"phone\":\"95312\"},{\"id\":\"139\",\"name\":\"中国大地财产保险股份有限公司北京分公司\",\"phone\":\"010-82277957\"},{\"id\":\"42826\",\"name\":\"中国人民财产保险股份有限公司\",\"phone\":\"95518\"},{\"id\":\"1002841\",\"name\":\"泰康在线财产保险股份有限公司\",\"phone\":\"95522-3\"}]";
        List<SupplierDto> supplierDtos = JsonUtils.toArrayList(json, SupplierDto.class);
        Map<String, SupplierDto> map = new HashMap();
        supplierDtos.stream().forEach(item->{
            // System.out.println("      - "+item.getId());
            System.out.println("    "+item.getId()+":");
            System.out.println("      supplierId: " +item.getId());
            System.out.println("      supplierName: " +item.getName());
            System.out.println("      supplierPhone: "+item.getPhone());
        });
        System.out.println(JsonUtils.toJsonString(map));
    }
}
