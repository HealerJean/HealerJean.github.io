package com.healerjean.proj.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.healerjean.proj.dao.mapper.SysDistrictMapper;
import com.healerjean.proj.dto.DistrictData;
import com.healerjean.proj.pojo.SysDistrict;
import com.healerjean.proj.service.DictionaryService;
import com.healerjean.proj.utils.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HealerJean
 * @ClassName DictionaryServiceImpl
 * @date 2020/4/1  10:03.
 * @Description
 */
@Service
@Slf4j
public class DictionaryServiceImpl implements DictionaryService {

    @Resource
    private SysDistrictMapper sysDistrictMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importDistrictJson(String districtJson) {
        List<DistrictData> districtDatas = JsonUtils.toObject(districtJson, new TypeReference<List<DistrictData>>() {
        });
        for (DistrictData districtData : districtDatas) {
            List<DistrictData.CityDTO> citys = districtData.getC();
            for (DistrictData.CityDTO city : citys) {
                List<DistrictData.CityDTO.DistrictDTO> districts = city.getC();
                for (DistrictData.CityDTO.DistrictDTO district : districts) {
                    SysDistrict sysDistrict = new SysDistrict();
                    sysDistrict.setProvinceCode(districtData.getV());
                    sysDistrict.setProvinceName(districtData.getN());
                    sysDistrict.setCityCode(city.getV());
                    sysDistrict.setCityName(city.getN());
                    sysDistrict.setDistrictCode(district.getV());
                    sysDistrict.setDistrictName(district.getN());
                    sysDistrict.setStatus("10");
                    log.info("导入的地区是：【{}】", sysDistrict);
                    sysDistrictMapper.insert(sysDistrict);
                }
            }
        }
    }


    @Override
    public String exportDistrictJson() {

        List<DistrictData> districtDataList = new ArrayList<>();

        //1、获取省
        Wrapper<SysDistrict> districtWrapper = new QueryWrapper<SysDistrict>().lambda()
                .select(SysDistrict::getProvinceCode, SysDistrict::getProvinceName)
                .groupBy(SysDistrict::getProvinceCode, SysDistrict::getProvinceName);
        List<SysDistrict> provinceDistrictList = sysDistrictMapper.selectList(districtWrapper);
        log.info("省一共有【{}】", provinceDistrictList);
        provinceDistrictList.stream().forEach(item -> {
            DistrictData districtData = new DistrictData();
            List<DistrictData.CityDTO> cityList = new ArrayList<>();
            districtData.setV(item.getProvinceCode());
            districtData.setN(item.getProvinceName());
            districtData.setC(cityList);
            districtDataList.add(districtData);
            //1、获取城市
            Wrapper<SysDistrict> cityWrapper = new QueryWrapper<SysDistrict>().lambda()
                    .select(SysDistrict::getProvinceCode, SysDistrict::getCityCode, SysDistrict::getCityName)
                    .eq(SysDistrict::getProvinceCode, item.getProvinceCode())
                    .groupBy(SysDistrict::getCityCode, SysDistrict::getCityName);
            List<SysDistrict> cityDistrictList = sysDistrictMapper.selectList(cityWrapper);
            // log.info("【{}】省一共有【{}】个市", item.getProvinceName(), cityDistrictList.size());
            cityDistrictList.stream().forEach(item2 -> {
                List<DistrictData.CityDTO.DistrictDTO> districtList = new ArrayList<>();
                DistrictData.CityDTO cityDTO = new DistrictData.CityDTO();
                cityDTO.setV(item2.getCityCode());
                cityDTO.setN(item2.getCityName());
                cityDTO.setC(districtList);
                cityList.add(cityDTO);


                //1、获取地区
                Wrapper<SysDistrict> dictrictWrapper = new QueryWrapper<SysDistrict>().lambda()
                        .select(SysDistrict::getDistrictCode, SysDistrict::getDistrictName)
                        .eq(SysDistrict::getProvinceCode, item.getProvinceCode())
                        .eq(SysDistrict::getCityCode, item2.getCityCode())
                        .groupBy(SysDistrict::getProvinceCode, SysDistrict::getCityCode, SysDistrict::getDistrictCode, SysDistrict::getDistrictName);
                List<SysDistrict> dictrictDistrictList = sysDistrictMapper.selectList(dictrictWrapper);
                // log.info("【{}】省,【{}】市一共有【{}】个地区", item.getProvinceName(), item2.getCityName(), dictrictDistrictList.size());
                dictrictDistrictList.stream().forEach(item3 -> {
                    DistrictData.CityDTO.DistrictDTO districtDTO = new DistrictData.CityDTO.DistrictDTO();
                    districtDTO.setV(item3.getDistrictCode());
                    districtDTO.setN(item3.getDistrictName());
                    districtList.add(districtDTO);
                });
            });
        });
        return JsonUtils.toJsonString(districtDataList);
    }
}
