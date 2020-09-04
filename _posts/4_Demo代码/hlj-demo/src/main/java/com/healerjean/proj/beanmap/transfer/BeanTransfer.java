package com.healerjean.proj.beanmap.transfer;

import com.healerjean.proj.beanmap.BeanUtils;
import com.healerjean.proj.enmus.SystemEmum;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author HealerJean
 * @ClassName BeanTransfer
 * @date 2020/9/4  14:39.
 * @Description
 */
public interface BeanTransfer {

    /**
     * Date和LocaDateTime互转
     */
    @Named(BeanUtils.TRANSFER_OF_DATE_AND_LOCAL_DATE_TIME)
    class TransferDateAndLocalDateTime implements BeanTransfer {

        public static final String DateToLocalDateTime = "DateToLocalDateTime";
        public static final String LocalDateTimeToDate = "LocalDateTimeToDate";

        @Named(DateToLocalDateTime)
        public static LocalDateTime toLocalDateTime(Date date) {
            if (date == null) {
                return null;
            }
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            return instant.atZone(zoneId).toLocalDateTime();
        }


        @Named(LocalDateTimeToDate)
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
    @Named(BeanUtils.TRANSFER_OF_SEX_ENUM)
     class TransferSexEnum  implements BeanTransfer{
        public static final String CODE_TO_SEX_ENUM = "CodeToSexEnum";
        public static final String SEX_ENUM_TO_CODE = "SexEnumToCode";

        @Named(CODE_TO_SEX_ENUM)
        public SystemEmum.SexEnum codeToSexEnum(Integer code) {
            return SystemEmum.SexEnum.to(code);
        }

        @Named(SEX_ENUM_TO_CODE)
        public Integer sexEnumToCode(SystemEmum.SexEnum sexEnum) {
            return sexEnum.getCode();
        }
    }


}
