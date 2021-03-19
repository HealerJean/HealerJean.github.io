package com.healerjean.proj.mt.statemachine.enums;

/**
 * @author zhangyujin
 * @date 2021/3/5  4:59 下午.
 * @description
 */
public interface StrategyEnum {

     enum UserStrategyEnum implements StateEnum {

        SAME_USER(1,"同人"),
        UN_SAME_USER(2,"不同人"),
        NOT_DISTINGUISH(3,"不区分同人非同人");

        private Integer code;

        private String msg;

        UserStrategyEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }


     enum CommandExpireStrategyEnum implements StrategyEnum {

        NOT_EXPIRE(1,"指令无有效期"),
        IN_EXPIRE(2,"指令在有效期内"),
        OUT_EXPIRE(3,"指令在有效期外");

        private Integer code;

        private String msg;

        CommandExpireStrategyEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }



}
