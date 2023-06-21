package com.healerjean.proj.beanmap.transfer;

import com.healerjean.proj.enmus.MapperNamedConstant;
import com.healerjean.proj.enmus.SystemEmum;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * BeanTransfer
 *
 * @author HealerJean
 * @date 2023-06-19 06:06:56
 */
public interface BeanTransfer {

    /**
     * Date和LocalDateTime互转
     */
    @Named(MapperNamedConstant.CLASS_TRANSFER_DATE)
    class TransferDateAndLocalDateTime implements BeanTransfer {

        @Named(MapperNamedConstant.METHOD_DATE_TO_LOCAL_DATE_TIME)
        public static LocalDateTime toLocalDateTime(Date date) {
            if (date == null) {
                return null;
            }
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            return instant.atZone(zoneId).toLocalDateTime();
        }


        @Named(MapperNamedConstant.METHOD_LOCAL_DATE_TIME_TO_DATE)
        public static Date toDate(LocalDateTime localDateTime) {
            if (localDateTime == null) {
                return null;
            }
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = localDateTime.atZone(zone).toInstant();
            return Date.from(instant);
        }
    }


    /**
     * Code和枚举互转
     */
    @Named(MapperNamedConstant.CLASS_TRANSFER_ENUM_SEX)
     class TransferSexEnum  implements BeanTransfer{

        @Named(MapperNamedConstant.METHOD_SEX_CODE_TO_ENUM)
        public SystemEmum.SexEnum sexCodeToEnum(Integer code) {
            return SystemEmum.SexEnum.to(code);
        }

        @Named(MapperNamedConstant.METHOD_SEX_ENUM_TO_CODE)
        public Integer sexEnumToCode(SystemEmum.SexEnum sexEnum) {
            return sexEnum.getCode();
        }
    }


}
