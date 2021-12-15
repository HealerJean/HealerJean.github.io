package com.healerjean.proj.util.mail;

/**
 * @author zhangyujin
 * @date 2021/12/15  6:04 下午.
 * @description
 */
public enum WeekOfDateEnum {
    MONDAY("星期一", "Monday", "Mon.", 1),
    TUESDAY("星期二", "Tuesday", "Tues.", 2),
    WEDNESDAY("星期三", "Wednesday", "Wed.", 3),
    THURSDAY("星期四", "Thursday", "Thur.", 4),
    FRIDAY("星期五", "Friday", "Fri.", 5),
    SATURDAY("星期六", "Saturday", "Sat.", 6),
    SUNDAY("星期日", "Sunday", "Sun.", 7);

    String name_cn;
    String name_en;
    String name_enShort;
    int number;

     WeekOfDateEnum(String name_cn, String name_en, String name_enShort, int number) {
        this.name_cn = name_cn;
        this.name_en = name_en;
        this.name_enShort = name_enShort;
        this.number = number;
    }

    public String getChineseName() {
        return this.name_cn;
    }

    public String getName() {
        return this.name_en;
    }

    public String getShortName() {
        return this.name_enShort;
    }

    public int getNumber() {
        return this.number;
    }
}
