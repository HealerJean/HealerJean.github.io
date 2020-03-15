package com.healerjean.proj.controller.system;


import com.healerjean.proj.api.system.DictionaryService;
import com.healerjean.proj.controller.BaseController;
import com.healerjean.proj.dto.ResponseBean;
import com.healerjean.proj.dto.system.*;
import com.healerjean.proj.exception.ParameterErrorException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author HealerJean
 * @version 1.0v
 * @Description 系统管理-字典管理
 * @ClassName A1
 * @date 2019/4/12  17:45.
 */
@Api(description = "系统管理-字典管理")
@RestController
@RequestMapping("hlj/sys")
@Slf4j
public class DictionaryController extends BaseController {

    @Autowired
    private DictionaryService dictionaryService;

    @ApiOperation(value = "字典类型查询",
            notes = "字典类型查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryTypeDTO.class
    )
    @GetMapping(value = "dictType/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean getDictType(@PathVariable Long id) {
        log.info("系统管理-字典管理--------字典类型查询--------字典类型Id：{}", id);
        if (id == null) {
            throw new ParameterErrorException("字典类型Id不能为null");
        }
        DictionaryTypeDTO typeDTO = new DictionaryTypeDTO();
        typeDTO.setId(id);
        return ResponseBean.buildSuccess("字典类型查询成功", dictionaryService.queryDictionaryTypeSingle(typeDTO));
    }


    @ApiOperation(value = "字典类型列表查询",
            notes = "字典类型列表查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryTypeDTO.class
    )
    @GetMapping(value = "dictTypes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean getDictTypes(DictionaryTypeDTO type) {
        log.info("系统管理-字典管理--------字典类型列表查询--------请求参数：{}", type);
        if (type != null && type.getFlag() != null && !type.getFlag()) {
            return ResponseBean.buildSuccess("查询字典类型列表成功", dictionaryService.queryDictTypesLikes(type));
        }
        return ResponseBean.buildSuccess("字典类型列表查询成功", dictionaryService.queryDictTypesPageLikes(type));
    }


    @ApiOperation(value = "字典数据查询",
            notes = "字典数据查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryDataDTO.class
    )
    @GetMapping(value = "dictData/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean getDictData(@PathVariable Long id) {
        log.info("系统管理-字典管理--------字典数据查询--------字典数据Id：{}", id);

        DictionaryDataDTO dataDTO = new DictionaryDataDTO();
        dataDTO.setId(id);
        return ResponseBean.buildSuccess("字典数据查询成功", dictionaryService.queryDictionaryDataSingle(dataDTO));
    }


    @ApiOperation(value = "字典数据列表查询",
            notes = "字典数据列表查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryDataDTO.class
    )
    @GetMapping(value = "dictDatas", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean getDicDatas(DictionaryDataDTO dataDTO) {
        log.info("系统管理-字典管理--------字典数据列表查询--------请求参数：{}", dataDTO);
        if ((dataDTO.getTypeKeys() == null || dataDTO.getTypeKeys().isEmpty()) && StringUtils.isBlank(dataDTO.getTypeKey())) {
            throw new ParameterErrorException("typeKey不能为空 或 typeKeys不能为空");
        }
        Map map = new HashMap(2);
        if (StringUtils.isNotBlank(dataDTO.getTypeKey())) {
            if (dataDTO.getFlag() != null && !dataDTO.getFlag()) {
                map.put(dataDTO.getTypeKey(), dictionaryService.queryDictDataLikes(dataDTO));
                return ResponseBean.buildSuccess("字典数据列表查询成功", map);
            }
            return ResponseBean.buildSuccess("字典数据列表查询成功", dictionaryService.queryDictDataPageLikes(dataDTO));
        }

        DictionaryDataDTO dto = new DictionaryDataDTO();
        dataDTO.getTypeKeys().stream().forEach(typeKey -> {
            dto.setTypeKey(typeKey);
            map.put(typeKey, (dictionaryService.queryDictDataLikes(dto)));
        });
        return ResponseBean.buildSuccess("查询字典数据列表成功", map);
    }


    @ApiOperation(value = "省份数据列表查询",
            notes = "省份数据列表查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryDataDTO.class
    )
    @GetMapping(value = "provinces", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean getProvinces(DistrictDTO district) {
        log.info("系统管理-字典管理--------省份数据列表查询--------请求参数：{}", district);
        List<ProvinceDTO> provinces = null;
        if (district == null) {
            provinces = dictionaryService.findProvinces(null);
        } else {
            provinces = dictionaryService.findProvinces(district.getProvinceCode());
        }
        return ResponseBean.buildSuccess("省份数据列表查询成功", provinces);
    }


    @ApiOperation(value = "城市数据列表查询",
            notes = "城市数据列表查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryDataDTO.class
    )
    @GetMapping(value = "citys", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean getCitys(DistrictDTO district) {
        log.info("系统管理-字典管理--------城市数据列表查询--------请求参数：{}", district);
        List<CityDTO> citys = null;
        if (district == null) {
            citys = dictionaryService.findCitys(null, null);
        } else {
            citys = dictionaryService.findCitys(district.getProvinceCode(), district.getCityCode());
        }
        return ResponseBean.buildSuccess("城市数据列表查询成功", citys);
    }


    @ApiOperation(value = "地区数据列表查询",
            notes = "地区数据列表查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryDataDTO.class
    )
    @GetMapping(value = "districts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean getDistrict(DistrictDTO district) {
        log.info("系统管理-字典管理--------地区数据列表查询--------请求参数：{}", district);
        List<DistrictDTO> districts = null;
        if (district == null) {
            districts = dictionaryService.findDistricts(null, null, null);
        } else {
            districts = dictionaryService.findDistricts(district.getProvinceCode(), district.getCityCode(), district.getDistrictCode());
        }
        return ResponseBean.buildSuccess("地区数据列表查询成功", districts);
    }

}
