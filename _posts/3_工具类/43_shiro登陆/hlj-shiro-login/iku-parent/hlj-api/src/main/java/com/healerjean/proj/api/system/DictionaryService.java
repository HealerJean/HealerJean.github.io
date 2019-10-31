package com.healerjean.proj.api.system;


import com.healerjean.proj.common.page.PageDTO;
import com.healerjean.proj.dto.system.*;
import com.healerjean.proj.dto.user.LoginUserDTO;

import java.util.List;

/**
 * @ClassName DictionaryService
 * @Author TD
 * @Date 2019/4/11 15:13
 * @Description 字典业务类
 */
public interface DictionaryService {


    /**
     * 添加字典类型
     */
    void addDictType(DictionaryTypeDTO typeDTO, LoginUserDTO loginUserDTO);

    /**
     * 删除字典类型
     */
    void deleteDictType(Long id, LoginUserDTO loginUserDTO);

    /**
     * 修改字典类型
     */
    void updateDictType(DictionaryTypeDTO typeDTO, LoginUserDTO loginUserDTO);


    /**
     * 分页查询字典类型列表
     */
    PageDTO<DictionaryTypeDTO> queryDictTypesPageLikes(DictionaryTypeDTO typeDTO);

    /**
     * 查询字典类型列表
     */
    List<DictionaryTypeDTO> queryDictTypesLikes(DictionaryTypeDTO typeDTO);

    /**
     * 查询字典类型
     * 1、根据主键Id查询
     * 2、id为null，根据typeKey查询 （typeKey唯一）
     */
    DictionaryTypeDTO queryDictionaryTypeSingle(DictionaryTypeDTO typeDTO);


    /**
     * 添加字典
     */
    void addDictionary(DictionaryDataDTO dataDTO, LoginUserDTO loginUserDTO);

    /**
     * 删除字典
     */
    void deleteDictionary(Long id, LoginUserDTO loginUserDTO);

    /**
     * 修改字典
     */
    void updateDictionary(DictionaryDataDTO dataDTO, LoginUserDTO loginUserDTO);

    /**
     * 查询字典数据
     */
    DictionaryDataDTO queryDictionaryDataSingle(DictionaryDataDTO dataDTO);

    /**
     * 判断 字典数据 是否存在
     */
    boolean judgeDictDataExist(String typeKey, String dataKey);

    /**
     * 分页 查询字典数据列表
     */
    PageDTO<DictionaryDataDTO> queryDictDataPageLikes(DictionaryDataDTO dataDTO);

    /**
     * 查询字典数据列表
     */
    List<DictionaryDataDTO> queryDictDataLikes(DictionaryDataDTO dataDTO);

    /**
     * 判断省份是否在字典中
     */
    Boolean isProvinceExist(String provinceCode);

    /**
     * 判断城市是否在字典中
     */
    Boolean isCityExist(String provinceCode, String cityCode);

    /**
     * 判断地区是否在字典中
     */
    Boolean isDistrictExist(String provinceCode, String cityCode, String districtCode);

    /**
     * 查询省份列表
     */
    List<ProvinceDTO> findProvinces(String provinceCode);

    /**
     * 查询城市列表
     */
    List<CityDTO> findCitys(String provinceCode, String cityCode);

    /**
     * 查询地区列表
     */
    List<DistrictDTO> findDistricts(String provinceCode, String cityCode, String districtCode);
}
