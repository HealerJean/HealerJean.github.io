package com.healerjean.proj.beanmap;

import com.healerjean.proj.beanmap.transfer.BeanTransfer;
import com.healerjean.proj.dto.DictionaryType;
import com.healerjean.proj.pojo.DictionaryTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author HealerJean
 * @ClassName DictTypeMapper
 * @date 2020/9/4  10:13.
 * @Description
 */
@Mapper(uses = {
        BeanTransfer.TransferSexEnum.class,
        BeanTransfer.TransferDateAndLocalDateTime.class
})
public interface BeanUtils {

    BeanUtils MAPPER = Mappers.getMapper(BeanUtils.class);
    String TRANSFER_OF_SEX_ENUM = "sexEnumTransfer";
    String TRANSFER_OF_DATE_AND_LOCAL_DATE_TIME = "DateAndLocalDateTimeTransfer";


    /**
     * 方法名称可任意
     *
     * @param dictionaryType 入参对应要被转化的对象
     * @return 返回值对应转化后的对象
     */
    @Mappings({
            /** 名字不同转化 */
            @Mapping(source = "dictionaryTypeId", target = "id"),
            /** 类型和名字都不同转化 */
            @Mapping(source = "sex", target = "sexEnum", qualifiedByName = {TRANSFER_OF_SEX_ENUM, BeanTransfer.TransferSexEnum.CODE_TO_SEX_ENUM}),
            @Mapping(source = "createTime", target = "createTime", qualifiedByName = {TRANSFER_OF_DATE_AND_LOCAL_DATE_TIME, BeanTransfer.TransferDateAndLocalDateTime.DateToLocalDateTime}),
            @Mapping(source = "status", target = "status", ignore = true),
    })
    DictionaryTypeDTO dictionaryType2DTO(DictionaryType dictionaryType);

}
