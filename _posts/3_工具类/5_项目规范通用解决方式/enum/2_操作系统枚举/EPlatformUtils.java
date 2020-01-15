package com.healerjean.proj.utils;

import com.healerjean.proj.enums.BusinessEnum.EPlatformEnum;

/**
 * @author HealerJean
 * @ClassName EPlatformEnumUtils
 * @date 2019-10-17  23:36.
 * @Description
 */
public class EPlatformUtils {

    private static String OS = System.getProperty("os.name").toLowerCase();


    /**
     * 获取操作系统名字
     */
    public static EPlatformEnum getOSname() {
        EPlatformEnum platform = null;
        if (isAix()) {
            platform = EPlatformEnum.AIX;
        } else if (isDigitalUnix()) {
            platform = EPlatformEnum.Digital_Unix;
        } else if (isFreeBSD()) {
            platform = EPlatformEnum.FreeBSD;
        } else if (isHPUX()) {
            platform = EPlatformEnum.HP_UX;
        } else if (isIrix()) {
            platform = EPlatformEnum.Irix;
        } else if (isLinux()) {
            platform = EPlatformEnum.Linux;
        } else if (isMacOS()) {
            platform = EPlatformEnum.Mac_OS;
        } else if (isMacOSX()) {
            platform = EPlatformEnum.Mac_OS_X;
        } else if (isMPEiX()) {
            platform = EPlatformEnum.MPEiX;
        } else if (isNetWare()) {
            platform = EPlatformEnum.NetWare_411;
        } else if (isOpenVMS()) {
            platform = EPlatformEnum.OpenVMS;
        } else if (isOS2()) {
            platform = EPlatformEnum.OS2;
        } else if (isOS390()) {
            platform = EPlatformEnum.OS390;
        } else if (isOSF1()) {
            platform = EPlatformEnum.OSF1;
        } else if (isSolaris()) {
            platform = EPlatformEnum.Solaris;
        } else if (isSunOS()) {
            platform = EPlatformEnum.SunOS;
        } else if (isWindows()) {
            platform = EPlatformEnum.Windows;
        } else {
            platform = EPlatformEnum.Others;
        }
        return platform;
    }


    public static boolean isLinux() {
        return OS.indexOf("linux") >= 0;
    }

    public static boolean isMacOS() {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") < 0;
    }

    public static boolean isMacOSX() {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") > 0;
    }

    public static boolean isWindows() {
        return OS.indexOf("windows") >= 0;
    }

    public static boolean isOS2() {
        return OS.indexOf("os/2") >= 0;
    }

    public static boolean isSolaris() {
        return OS.indexOf("solaris") >= 0;
    }

    public static boolean isSunOS() {
        return OS.indexOf("sunos") >= 0;
    }

    public static boolean isMPEiX() {
        return OS.indexOf("mpe/ix") >= 0;
    }

    public static boolean isHPUX() {
        return OS.indexOf("hp-ux") >= 0;
    }

    public static boolean isAix() {
        return OS.indexOf("aix") >= 0;
    }

    public static boolean isOS390() {
        return OS.indexOf("os/390") >= 0;
    }

    public static boolean isFreeBSD() {
        return OS.indexOf("freebsd") >= 0;
    }

    public static boolean isIrix() {
        return OS.indexOf("irix") >= 0;
    }

    public static boolean isDigitalUnix() {
        return OS.indexOf("digital") >= 0 && OS.indexOf("unix") > 0;
    }

    public static boolean isNetWare() {
        return OS.indexOf("netware") >= 0;
    }

    public static boolean isOSF1() {
        return OS.indexOf("osf1") >= 0;
    }

    public static boolean isOpenVMS() {
        return OS.indexOf("openvms") >= 0;
    }


}
