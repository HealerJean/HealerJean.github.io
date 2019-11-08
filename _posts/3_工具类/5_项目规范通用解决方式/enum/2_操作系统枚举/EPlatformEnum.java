package com.healerjean.proj.common.enums;

/**
 * @author HealerJean
 * @ClassName EPlatformEnum
 * @date 2019/11/8  11:38.
 * @Description
 */
public enum EPlatformEnum {

    Any("any"),
    Linux("Linux"),
    Mac_OS("Mac OS"),
    Mac_OS_X("Mac OS X"),
    Windows("Windows"),
    OS2("OS/2"),
    Solaris("Solaris"),
    SunOS("SunOS"),
    MPEiX("MPE/iX"),
    HP_UX("HP-UX"),
    AIX("AIX"),
    OS390("OS/390"),
    FreeBSD("FreeBSD"),
    Irix("Irix"),
    Digital_Unix("Digital Unix"),
    NetWare_411("NetWare"),
    OSF1("OSF1"),
    OpenVMS("OpenVMS"),
    Others("Others"),
    ;

    EPlatformEnum(String code) {
        this.code = code;
    }

    public String code;

}
