package com.healerjean.proj.service;

/**
 * @author HealerJean
 * @ClassName DictionaryService
 * @date 2020/4/1  10:03.
 * @Description
 */
public interface DictionaryService {


    void importDistrictJson(String districtJson);

    String exportDistrictJson();
}
