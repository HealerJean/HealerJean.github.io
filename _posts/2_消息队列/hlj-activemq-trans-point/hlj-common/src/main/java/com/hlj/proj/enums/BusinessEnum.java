package com.hlj.proj.enums;

/**
 * @author HealerJean
 * @ClassName BusinessEnum
 * @date 2019/9/9  14:04.
 * @Description
 */
public interface BusinessEnum {

    /**
     * 事件类型
     */
    enum EventType {

        新增用户 ("New_User","新增用户"),
        新增积分 ("New_Point","新增积分"),

        ;

        public  String code ;
        public String desc ;

        EventType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }


    /**
     * 事件处理过程
     */
    enum EventProcess {

        已发布 ("Publish","已发布"),
        已处理 ("Processed","已处理"),
        ;

        public String code ;
        public String desc ;


        EventProcess(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }
}
