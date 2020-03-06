package com.healerjean.proj.enums;

/**
 * @author HealerJean
 * @ClassName BusinessEnum
 * @date 2019/9/30  14:39.
 * @Description
 */
public interface BusinessEnum {


    /**
     * 操作系统枚举
     */
    enum EPlatformEnum {

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


    /**
     * 菜单类型
     */
    enum MenuTypeEnum {

        后端菜单("0", "后端菜单"),
        前端菜单("1", "前端菜单");

        MenuTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code;
        public String desc;


        public static MenuTypeEnum toEnum(String code) {
            for (MenuTypeEnum value : MenuTypeEnum.values()) {
                if (value.code == code) {
                    return value;
                }
            }
            return null;
        }
    }


    /**
     * 用户类型
     */
    enum UserTypeEnum {

        管理人员("manager", "管理人员"),
        网站用户("webuser", "网站用户");

        UserTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code;
        public String desc;


        public static MenuTypeEnum toEnum(String code) {
            for (MenuTypeEnum value : MenuTypeEnum.values()) {
                if (value.code == code) {
                    return value;
                }
            }
            return null;
        }
    }


}
