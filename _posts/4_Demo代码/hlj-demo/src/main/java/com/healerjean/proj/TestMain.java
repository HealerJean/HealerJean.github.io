package com.healerjean.proj;

import com.healerjean.proj.beanmap.BeanUtils;
import com.healerjean.proj.dto.DictionaryType;
import com.healerjean.proj.enmus.SystemEmum;
import com.healerjean.proj.pojo.DictionaryTypeDTO;
import org.junit.Test;

import java.util.Date;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */

public class TestMain {

    @Test
    public void test() {
        DictionaryType dictionaryType = new DictionaryType();
        dictionaryType.setDictionaryTypeId(1L);
        dictionaryType.setTypeDesc("Loan");
        dictionaryType.setStatus("10");
        dictionaryType.setCreateTime(new Date());
        dictionaryType.setSex(1);
        DictionaryTypeDTO dto = BeanUtils.MAPPER.dictionaryType2DTO(dictionaryType);
        System.out.println(dto);
    }


    @Test
    public void test2(){
        SystemEmum.SexEnum to = SystemEmum.SexEnum.to(1);
        System.out.println(to);
    }


}
