package com.healerjean.proj.controller.system;


import com.healerjean.proj.api.system.DictionaryService;
import com.healerjean.proj.common.group.ValidateGroup;
import com.healerjean.proj.constant.CommonConstants;
import com.healerjean.proj.controller.BaseController;
import com.healerjean.proj.dto.ResponseBean;
import com.healerjean.proj.dto.system.*;
import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.exception.ParameterErrorException;
import com.healerjean.proj.util.UserUtils;
import com.healerjean.proj.utils.validate.ValidateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author HealerJean
 * @version 1.0v
 * @Description 字典数据的维护
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


    @ApiOperation(value = "字典管理-字典类型添加",
            notes = "字典管理-字典类型添加",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryTypeDTO.class
    )
    @PostMapping(value = "dictType/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean addDictType(@RequestBody(required = false) DictionaryTypeDTO typeDTO) {
        log.info("字典管理--------字典类型添加--------字典类型信息：{}", typeDTO);
        String validate = ValidateUtils.validate(typeDTO, ValidateGroup.AddDictType.class);
        if (!CommonConstants.COMMON_SUCCESS.equals(validate)) {
            log.info("字典管理--------字典类型添加--------参数不完整：{}", validate);
            throw new ParameterErrorException(validate);
        }
        LoginUserDTO loginUserDTO = UserUtils.getLoginUser();
        dictionaryService.addDictType(typeDTO, loginUserDTO);
        return ResponseBean.buildSuccess("字典类型添加成功");
    }


    @ApiOperation(value = "字典管理-字典类型修改",
            notes = "字典管理-字典类型修改",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryTypeDTO.class
    )
    @PutMapping(value = "dictType/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean updateDictType(@PathVariable Long id, @RequestBody(required = false) DictionaryTypeDTO typeDTO) {
        log.info("字典管理--------字典类型修改--------id{} 字典类型信息：{}", id, typeDTO);
        String validate = ValidateUtils.validate(typeDTO, ValidateGroup.UpdateDictType.class);
        if (!CommonConstants.COMMON_SUCCESS.equals(validate)) {
            log.info("字典管理--------字典类型修改--------参数不完整：{}", validate);
            throw new ParameterErrorException(validate);
        }
        if (id == null) {
            throw new ParameterErrorException("字典类型Id不能为null");
        }
        typeDTO.setId(id);
        LoginUserDTO loginUserDTO = UserUtils.getLoginUser();
        dictionaryService.updateDictType(typeDTO, loginUserDTO);
        return ResponseBean.buildSuccess("字典类型修改成功");
    }


    @ApiOperation(value = "字典管理-字典类型删除",
            notes = "字典管理-字典类型删除",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryTypeDTO.class
    )
    @DeleteMapping(value = "dictType/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean deleteDictType(@PathVariable Long id) {
        log.info("字典管理--------字典类型删除--------字典类型Id：{}", id);
        if (id == null) {
            throw new ParameterErrorException("字典类型Id不能为null");
        }
        LoginUserDTO loginUserDTO = UserUtils.getLoginUser();
        dictionaryService.deleteDictType(id, loginUserDTO);
        return ResponseBean.buildSuccess("字典类型删除成功");
    }


    @ApiOperation(value = "字典管理-字典类型查询",
            notes = "字典管理-字典类型查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryTypeDTO.class
    )
    @GetMapping(value = "dictType/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean getDictType(@PathVariable Long id) {
        log.info("字典管理--------字典类型查询--------字典类型Id：{}", id);
        if (id == null) {
            throw new ParameterErrorException("字典类型Id不能为null");
        }
        DictionaryTypeDTO typeDTO = new DictionaryTypeDTO();
        typeDTO.setId(id);
        return ResponseBean.buildSuccess("字典类型查询成功", dictionaryService.queryDictionaryTypeSingle(typeDTO));
    }


    @ApiOperation(value = "字典管理-字典类型列表查询",
            notes = "字典管理-字典类型列表查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryTypeDTO.class
    )
    @GetMapping(value = "dictTypes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean getDictTypes(DictionaryTypeDTO type) {
        log.info("字典管理--------字典类型列表查询--------字典类型信息：{}", type);
        if (type != null && type.getFlag() != null && !type.getFlag()) {
            return ResponseBean.buildSuccess("查询字典类型列表成功", dictionaryService.queryDictTypesLikes(type));
        }
        return ResponseBean.buildSuccess("字典类型列表查询成功", dictionaryService.queryDictTypesPageLikes(type));
    }


    @ApiOperation(value = "字典管理-字典数据添加",
            notes = "字典管理-字典数据添加",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryDataDTO.class
    )
    @PostMapping(value = "dictData/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean addDictData(@RequestBody(required = false) DictionaryDataDTO data) {
        log.info("字典管理--------字典数据添加--------字典信息：{}", data);
        String validate = ValidateUtils.validate(data, ValidateGroup.AddDictData.class);
        if (!CommonConstants.COMMON_SUCCESS.equals(validate)) {
            log.info("字典管理--------字典数据添加--------参数不完整：{}", validate);
            throw new ParameterErrorException(validate);
            //判断typeKey是否可用
        } else {
            DictionaryTypeDTO typeDTO = new DictionaryTypeDTO();
            typeDTO.setTypeKey(data.getTypeKey());
            DictionaryTypeDTO type = dictionaryService.queryDictionaryTypeSingle(typeDTO);
            if (type == null) {
                throw new ParameterErrorException("字典类型不存在");
            }
        }
        LoginUserDTO loginUserDTO = UserUtils.getLoginUser();
        dictionaryService.addDictionary(data, loginUserDTO);
        return ResponseBean.buildSuccess("字典数据添加成功");
    }


    @ApiOperation(value = "字典管理-字典数据删除",
            notes = "字典管理-字典数据删除",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryDataDTO.class
    )
    @DeleteMapping(value = "dictData/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean deleteDictData(@PathVariable Long id) {
        log.info("字典管理--------字典数据删除--------字典数据Id：{}", id);
        if (id == null) {
            throw new ParameterErrorException("字典数据Id不能为null");
        }
        LoginUserDTO loginUserDTO = UserUtils.getLoginUser();
        dictionaryService.deleteDictionary(id, loginUserDTO);
        return ResponseBean.buildSuccess("字典数据删除成功");
    }


    @ApiOperation(value = "字典管理-字典数据修改",
            notes = "字典管理-字典数据修改",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryDataDTO.class
    )
    @PutMapping(value = "dictData/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean updateDictData(@PathVariable Long id, @RequestBody(required = false) DictionaryDataDTO dataDTO) {
        log.info("字典管理--------字典数据修改--------字典信息：{}", dataDTO);
        String validate = ValidateUtils.validate(dataDTO, ValidateGroup.UpdateDictData.class);
        if (!CommonConstants.COMMON_SUCCESS.equals(validate)) {
            log.info("字典管理--------字典数据修改--------参数不完整：{}", validate);
            throw new ParameterErrorException(validate);
        }
        if (id == null) {
            throw new ParameterErrorException("字典数据Id不能为null");
        }
        dataDTO.setId(id);
        LoginUserDTO loginUserDTO = UserUtils.getLoginUser();
        dictionaryService.updateDictionary(dataDTO, loginUserDTO);
        return ResponseBean.buildSuccess("字典数据修改成功");
    }


    @ApiOperation(value = "字典管理-字典数据查询",
            notes = "字典管理-字典数据查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryDataDTO.class
    )
    @GetMapping(value = "dictData/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean getDictData(@PathVariable Long id) {
        log.info("字典管理--------字典数据查询--------字典数据Id：{}", id);

        DictionaryDataDTO dataDTO = new DictionaryDataDTO();
        dataDTO.setId(id);
        return ResponseBean.buildSuccess("字典数据查询成功", dictionaryService.queryDictionaryDataSingle(dataDTO));
    }


    @ApiOperation(value = "字典管理-字典数据列表查询",
            notes = "字典管理-字典数据列表查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryDataDTO.class
    )
    @GetMapping(value = "dictDatas", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean getDicDatas(DictionaryDataDTO dataDTO) {
        log.info("字典管理--------字典数据列表查询--------字典信息：{}", dataDTO);
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


    @ApiOperation(value = "字典管理-省份数据列表查询",
            notes = "字典管理-省份数据列表查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryDataDTO.class
    )
    @GetMapping(value = "provinces", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean getProvinces(DistrictDTO district) {
        log.info("字典管理--------省份数据列表查询--------参数信息：{}", district);
        List<ProvinceDTO> provinces = null;
        if (district == null) {
            provinces = dictionaryService.findProvinces(null);
        } else {
            provinces = dictionaryService.findProvinces(district.getProvinceCode());
        }
        return ResponseBean.buildSuccess("省份数据列表查询成功", provinces);
    }


    @ApiOperation(value = "字典管理-城市数据列表查询",
            notes = "字典管理-城市数据列表查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryDataDTO.class
    )
    @GetMapping(value = "citys", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean getCitys(DistrictDTO district) {
        log.info("字典管理--------城市数据列表查询--------参数信息：{}", district);
        List<CityDTO> citys = null;
        if (district == null) {
            citys = dictionaryService.findCitys(null, null);
        } else {
            citys = dictionaryService.findCitys(district.getProvinceCode(), district.getCityCode());
        }
        return ResponseBean.buildSuccess("城市数据列表查询成功", citys);
    }


    @ApiOperation(value = "字典管理-地区数据列表查询",
            notes = "字典管理-地区数据列表查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = DictionaryDataDTO.class
    )
    @GetMapping(value = "districts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean getDistrict(DistrictDTO district) {
        log.info("字典管理--------地区数据列表查询--------参数信息：{}", district);
        List<DistrictDTO> districts = null;
        if (district == null) {
            districts = dictionaryService.findDistricts(null, null, null);
        } else {
            districts = dictionaryService.findDistricts(district.getProvinceCode(), district.getCityCode(), district.getDistrictCode());
        }
        return ResponseBean.buildSuccess("地区数据列表查询成功", districts);
    }
}
