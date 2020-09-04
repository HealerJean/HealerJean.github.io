package com.healerjean.proj.enmus;

/**
 * @author HealerJean
 * @ClassName SystemEmum
 * @date 2020/9/4  10:45.
 * @Description
 */
public interface SystemEmum {


    enum SexEnum implements SystemEmum {

        man(1, "男"),
        woman(0, "女");

        private Integer code;
        private String name;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        SexEnum(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public static SexEnum to(Integer code) {
            for (SexEnum sexEnum : SexEnum.values()) {
                if (sexEnum.code.equals(code)) {
                    return sexEnum;
                }
            }
            return null;
        }
    }
}
