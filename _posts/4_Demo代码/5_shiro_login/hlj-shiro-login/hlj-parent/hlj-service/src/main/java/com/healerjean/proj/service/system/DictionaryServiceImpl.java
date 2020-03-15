package com.healerjean.proj.service.system;

import com.healerjean.proj.api.system.DictionaryService;
import com.healerjean.proj.common.page.PageDTO;
import com.healerjean.proj.data.manager.system.SysDictionaryDataManager;
import com.healerjean.proj.data.manager.system.SysDictionaryTypeManager;
import com.healerjean.proj.data.manager.system.SysDistrictManager;
import com.healerjean.proj.data.pojo.system.*;
import com.healerjean.proj.dto.system.*;
import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.enums.ResponseEnum;
import com.healerjean.proj.enums.StatusEnum;
import com.healerjean.proj.exception.BusinessException;
import com.healerjean.proj.utils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author HealerJean
 * @version 1.0v
 * @Description
 * @ClassName DictionaryServiceImpl
 * @date 2019/4/12  11:04.
 */

@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private SysDictionaryDataManager sysDictionaryDataManager;

    @Autowired
    private SysDictionaryTypeManager sysDictionaryTypeManager;

    @Autowired
    private SysDistrictManager sysDistrictManager;

    /**
     * 添加字典类型
     *
     * @return
     */
    @Override
    public void addDictType(DictionaryTypeDTO typeDTO, LoginUserDTO loginUserDTO) {
        SysDictionaryTypeQuery query = new SysDictionaryTypeQuery();
        query.setTypeKey(typeDTO.getTypeKey());
        query.setStatus(StatusEnum.生效.code);
        SysDictionaryType type = sysDictionaryTypeManager.findByQueryContion(query);
        if (type != null) {
            throw new BusinessException(ResponseEnum.字典类型已存在);
        }
        type = new SysDictionaryType();
        type.setCreateUser(loginUserDTO.getUserId());
        type.setCreateName(loginUserDTO.getRealName());
        type.setTypeKey(typeDTO.getTypeKey());
        type.setTypeDesc(typeDTO.getTypeDesc());
        type.setStatus(StatusEnum.生效.code);
        type.setUpdateUser(loginUserDTO.getUserId());
        type.setUpdateName(loginUserDTO.getRealName());
        sysDictionaryTypeManager.insertSelective(type);
    }

    /**
     * 删除字典类型
     */
    @Override
    public void deleteDictType(Long id, LoginUserDTO loginUserDTO) {
        SysDictionaryType type = sysDictionaryTypeManager.findById(id);
        if (type == null) {
            throw new BusinessException(ResponseEnum.字典类型不存在);
        }
        type.setStatus(StatusEnum.废弃.code);
        type.setUpdateUser(loginUserDTO.getUserId());
        type.setUpdateName(loginUserDTO.getRealName());
        sysDictionaryTypeManager.updateSelective(type);
    }


    /**
     * 修改字典类型
     */
    @Override
    public void updateDictType(DictionaryTypeDTO typeDTO, LoginUserDTO loginUserDTO) {

        SysDictionaryTypeQuery query = new SysDictionaryTypeQuery();
        query.setTypeKey(typeDTO.getTypeKey());
        query.setStatus(StatusEnum.生效.code);
        SysDictionaryType typeExist = sysDictionaryTypeManager.findByQueryContion(query);
        //判断是是否已经存在数据
        if (typeExist != null && !typeExist.getId().equals(typeDTO.getId())) {
            throw new BusinessException(ResponseEnum.字典类型已存在);
        }
        SysDictionaryType type = BeanUtils.dtoToDictionaryType(typeDTO);
        type.setUpdateUser(loginUserDTO.getUserId());
        type.setUpdateName(loginUserDTO.getRealName());
        sysDictionaryTypeManager.updateSelective(type);
    }


    /**
     * 查询字典类型
     * 1、根据主键Id查询
     * 2、id为null，根据typeKey查询 （typeKey唯一）
     */
    @Override
    public DictionaryTypeDTO queryDictionaryTypeSingle(DictionaryTypeDTO typeDTO) {
        SysDictionaryType type = null;
        if (typeDTO.getId() != null) {
            type = sysDictionaryTypeManager.findById(typeDTO.getId());
        } else if (StringUtils.isNotBlank(typeDTO.getTypeKey())) {
            SysDictionaryTypeQuery query = new SysDictionaryTypeQuery();
            query.setTypeKey(typeDTO.getTypeKey());
            query.setStatus(StatusEnum.生效.code);
            type = sysDictionaryTypeManager.findByQueryContion(query);
        }
        return type == null ? null : BeanUtils.dictionaryTypeToDTO(type);
    }

    /**
     * 分页查询字典类型列表
     *
     * @param type
     * @return
     */
    @Override
    public PageDTO<DictionaryTypeDTO> queryDictTypesPageLikes(DictionaryTypeDTO type) {
        List<DictionaryTypeDTO> collect = null;
        SysDictionaryTypeQuery query = new SysDictionaryTypeQuery();
        if (type != null) {
            query.setTypeKey(type.getTypeKey());
            if (type.getPageNo() != null) {
                query.setPageNo(type.getPageNo());
            }
            if (type.getPageSize() != null) {
                query.setPageSize(type.getPageSize());
            }
        }
        query.setStatus(StatusEnum.生效.code);
        SysDictionaryTypePage page = sysDictionaryTypeManager.queryDictTypePageLikes(query);

        List<SysDictionaryType> types = page.getValues();
        if (types != null) {
            collect = types.stream().map(item -> BeanUtils.dictionaryTypeToDTO(item)).collect(toList());
        }
        return BeanUtils.toPageDTO(page, collect);
    }

    /**
     * 查询字典类型列表
     *
     * @param typeDTO
     * @return
     */
    @Override
    public List<DictionaryTypeDTO> queryDictTypesLikes(DictionaryTypeDTO typeDTO) {
        List<DictionaryTypeDTO> collect = null;
        SysDictionaryTypeQuery query = new SysDictionaryTypeQuery();
        query.setTypeKey(typeDTO.getTypeKey());
        query.setStatus(StatusEnum.生效.code);
        List<SysDictionaryType> types = sysDictionaryTypeManager.queryDictTypeLikes(query);
        if (types != null && !types.isEmpty()) {
            collect = types.stream().map(item -> BeanUtils.dictionaryTypeToDTO(item)).collect(toList());
        }
        return collect;
    }


    /**
     * 添加字典
     */
    @Override
    public void addDictionary(DictionaryDataDTO dataDTO, LoginUserDTO loginUserDTO) {
        SysDictionaryDataQuery query = new SysDictionaryDataQuery();
        query.setDataKey(dataDTO.getDataKey());
        query.setRefTypeKey(dataDTO.getTypeKey());
        query.setStatus(StatusEnum.生效.code);
        SysDictionaryData data = sysDictionaryDataManager.findByQueryContion(query);
        if (data != null) {
            throw new BusinessException(ResponseEnum.字典数据项已存在);
        }
        data = new SysDictionaryData();
        data.setDataKey(dataDTO.getDataKey());
        data.setDataValue(dataDTO.getDataValue());
        data.setRefTypeKey(dataDTO.getTypeKey());
        data.setStatus(StatusEnum.生效.code);
        data.setCreateUser(loginUserDTO.getUserId());
        data.setCreateName(loginUserDTO.getRealName());
        data.setUpdateUser(loginUserDTO.getUserId());
        data.setUpdateName(loginUserDTO.getRealName());
        sysDictionaryDataManager.save(data);
    }

    /**
     * 删除字典
     */
    @Override
    public void deleteDictionary(Long id, LoginUserDTO loginUserDTO) {
        SysDictionaryData data = sysDictionaryDataManager.findById(id);
        if (data == null) {
            throw new BusinessException(ResponseEnum.字典数据项不存在);
        }
        data.setStatus(StatusEnum.废弃.code);
        data.setUpdateUser(loginUserDTO.getUserId());
        data.setUpdateName(loginUserDTO.getRealName());
        sysDictionaryDataManager.update(data);
    }

    /**
     * 修改字典
     */
    @Override
    public void updateDictionary(DictionaryDataDTO dataDTO, LoginUserDTO loginUserDTO) {
        SysDictionaryData data = sysDictionaryDataManager.findById(dataDTO.getId());
        if (data == null) {
            throw new BusinessException(ResponseEnum.字典数据项不存在);
        }
        SysDictionaryDataQuery query = new SysDictionaryDataQuery();
        query.setDataKey(dataDTO.getDataKey());
        query.setRefTypeKey(dataDTO.getTypeKey());
        query.setStatus(StatusEnum.生效.code);
        SysDictionaryData result = sysDictionaryDataManager.findByQueryContion(query);
        if (result != null && !result.getId().equals(data.getId())) {
            throw new BusinessException(ResponseEnum.字典数据项已存在);
        }
        data.setDataKey(dataDTO.getDataKey());
        data.setDataValue(dataDTO.getDataValue());
        data.setUpdateUser(loginUserDTO.getUserId());
        data.setUpdateName(loginUserDTO.getRealName());
        sysDictionaryDataManager.updateSelective(data);
    }


    /**
     * 查询字典数据
     */
    @Override
    public DictionaryDataDTO queryDictionaryDataSingle(DictionaryDataDTO dataDTO) {
        SysDictionaryData data = null;
        if (dataDTO.getId() != null) {
            data = sysDictionaryDataManager.findById(dataDTO.getId());
        } else if (StringUtils.isNotBlank(dataDTO.getTypeKey()) && StringUtils.isNotBlank(dataDTO.getDataKey())) {
            SysDictionaryDataQuery query = new SysDictionaryDataQuery();
            query.setDataKey(dataDTO.getDataKey());
            query.setRefTypeKey(dataDTO.getTypeKey());
            query.setStatus(StatusEnum.生效.code);
            data = sysDictionaryDataManager.findByQueryContion(query);
        }
        return data == null ? null : BeanUtils.dictionaryDataToDTO(data);
    }


    /**
     * 判断 字典数据 是否存在
     *
     * @param typeKey
     * @param dataKey
     * @return
     */
    @Override
    public boolean judgeDictDataExist(String typeKey, String dataKey) {
        DictionaryDataDTO dataDTO = new DictionaryDataDTO();
        dataDTO.setTypeKey(typeKey);
        dataDTO.setDataKey(dataKey);
        dataDTO = queryDictionaryDataSingle(dataDTO);
        return dataDTO == null ? false : StringUtils.equals(StatusEnum.生效.code, dataDTO.getStatus());
    }


    /**
     * 查询字典数据列表
     *
     * @param dataDTO
     * @return
     */
    @Override
    public PageDTO<DictionaryDataDTO> queryDictDataPageLikes(DictionaryDataDTO dataDTO) {
        List<DictionaryDataDTO> collect = null;

        SysDictionaryDataQuery query = new SysDictionaryDataQuery();
        if (dataDTO != null) {
            query.setRefTypeKey(dataDTO.getTypeKey());
            query.setDataKey(dataDTO.getDataKey());
            if (dataDTO.getPageNo() != null) {
                query.setPageNo(dataDTO.getPageNo());
            }
            if (dataDTO.getPageSize() != null) {
                query.setPageSize(dataDTO.getPageSize());
            }
        }
        query.setStatus(StatusEnum.生效.code);
        SysDictionaryDataPage page = sysDictionaryDataManager.getDictDataPageLikes(query);

        List<SysDictionaryData> datas = page.getValues();
        if (datas != null) {
            collect = datas.stream().map(item -> BeanUtils.dictionaryDataToDTO(item)).collect(toList());
        }
        return BeanUtils.toPageDTO(page, collect);

    }

    /**
     * 查询字典数据列表
     *
     * @param dataDTO
     * @return
     */
    @Override
    public List<DictionaryDataDTO> queryDictDataLikes(DictionaryDataDTO dataDTO) {
        List<DictionaryDataDTO> collect = null;
        SysDictionaryDataQuery query = new SysDictionaryDataQuery();
        query.setRefTypeKey(dataDTO.getTypeKey());
        query.setDataKey(dataDTO.getDataKey());
        query.setStatus(StatusEnum.生效.code);
        List<SysDictionaryData> datas = sysDictionaryDataManager.getDictDataLikes(query);
        if (datas != null) {
            collect = datas.stream().map(item -> BeanUtils.dictionaryDataToDTO(item)).collect(toList());
        }
        return collect;
    }

    @Override
    public Boolean isProvinceExist(String provinceCode) {
        List<ProvinceDTO> provinces = this.findProvinces(provinceCode);
        return provinces != null && provinces.size() > 0;
    }

    @Override
    public Boolean isCityExist(String provinceCode, String cityCode) {
        List<CityDTO> provinces = this.findCitys(provinceCode, cityCode);
        return provinces != null && provinces.size() > 0;
    }

    @Override
    public Boolean isDistrictExist(String provinceCode, String cityCode, String districtCode) {
        List<DistrictDTO> districts = this.findDistricts(provinceCode, cityCode, districtCode);
        return districts != null && districts.size() > 0;
    }

    @Override
    public List<ProvinceDTO> findProvinces(String provinceCode) {
        SysDistrictQuery query = new SysDistrictQuery();
        query.setProvinceCode(provinceCode);
        query.setStatus(StatusEnum.生效.code);
        List<SysDistrict> SysDistricts = sysDistrictManager.queryList(query);
        if (SysDistricts == null || SysDistricts.isEmpty()) {
            return null;
        }
        return SysDistricts.stream().map(item -> BeanUtils.toProvinceDTO(item)).distinct().collect(toList());
    }

    @Override
    public List<CityDTO> findCitys(String provinceCode, String cityCode) {
        SysDistrictQuery query = new SysDistrictQuery();
        query.setProvinceCode(provinceCode);
        query.setCityCode(cityCode);
        query.setStatus(StatusEnum.生效.code);
        List<SysDistrict> SysDistricts = sysDistrictManager.queryList(query);
        if (SysDistricts == null || SysDistricts.isEmpty()) {
            return null;
        }
        return SysDistricts.stream().map(item -> BeanUtils.toCityDTO(item)).distinct().collect(toList());
    }

    @Override
    public List<DistrictDTO> findDistricts(String provinceCode, String cityCode, String districtCode) {
        SysDistrictQuery query = new SysDistrictQuery();
        query.setProvinceCode(provinceCode);
        query.setCityCode(cityCode);
        query.setDistrictCode(districtCode);
        query.setStatus(StatusEnum.生效.code);
        List<SysDistrict> SysDistricts = sysDistrictManager.queryList(query);
        if (SysDistricts == null || SysDistricts.isEmpty()) {
            return null;
        }
        return SysDistricts.stream().map(item -> BeanUtils.toDistrictDTO(item)).distinct().collect(toList());
    }


}
