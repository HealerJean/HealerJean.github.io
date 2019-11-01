package com.healerjean.proj.enums;

/**
 * @author HealerJean
 * @ClassName BusinessEnum
 * @date 2019/9/30  14:39.
 * @Description
 */
public interface BusinessEnum {

    enum WechatTypeEnum {

        订阅号("Subscribe", "订阅号"),
        服务号("Service", "服务号");

        WechatTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code;
        public String desc;

    }

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
     * 验证码枚举
     */
    enum VerifyCodeTypeEnum {

        图片验证码("captcha", "图片验证码"),
        注册邮箱验证码("RegistEmail", "注册邮箱验证码"),
        找回密码邮箱验证码("RetrievePasswordEmail", "找回密码邮箱验证码"),
        ;

        VerifyCodeTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code;
        public String desc;

        public static VerifyCodeTypeEnum toEnum(String code) {
            for (VerifyCodeTypeEnum item : VerifyCodeTypeEnum.values()) {
                if (item.code.equals(code)) {
                    return item;
                }
            }
            return null;
        }
    }


    /**
     * 模板类型
     */
    enum TemplateTypeEnum {

        邮件("Email", "邮件"),
        ;
        public String code;
        public String desc;

        TemplateTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }



    /**
     * 模板名字
     */
    enum TempleNameEnum  {
        邮箱验证("VerifyEmail", "邮箱验证"),
        找回密码邮箱验证("PasswordVerifyEmail", "找回密码邮箱验证"),
        手机号验证("VerifyPhone", "手机号验证"),
        ;
        TempleNameEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code;
        public String desc;


        public static TempleNameEnum toEnum(String code) {
            for (TempleNameEnum item : TempleNameEnum.values()) {
                if (item.code.equals(code)) {
                    return item;
                }
            }
            return null;
        }
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
                if (value.code .equals( code)) {
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

    /**
     * 用户类型
     */
    enum DomainTypeEnum {

        网站("web", "网站"),

        ;

        DomainTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String code;
        public String desc;

    }








}
