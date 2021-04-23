package com.hlj.util.z028_灰度工具;

/**
 * @author healejean
 * @date 2021/4/14  2:24 下午.
 * @description
 */
public interface GrayEnum {


    /**
     * 灰度业务
     */
    enum GrayBusinessEnum implements GrayEnum {

        TOPIC_CHANGE("TopicChange", true, true, "主题变更"),
        HELMET_ONLINE("HelmetOnline", true, true, "头盔上线"),
        ;

        private final String code;
        private final boolean defaultSwitch;
        private final boolean whiteSupport;
        private final String desc;

        GrayBusinessEnum(String code, boolean defaultSwitch, boolean whiteSupport, String desc) {
            this.code = code;
            this.defaultSwitch = defaultSwitch;
            this.whiteSupport = whiteSupport;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public boolean getDefaultSwitch() {
            return defaultSwitch;
        }

        public boolean isWhiteSupport() {
            return whiteSupport;
        }
    }


    /**
     * 灰度基数
     */
    enum GrayPercentEnum implements GrayEnum {

        ONE_HUNDRED(100, "百"),

        ONE_THOUSAND(1000, "千"),

        TEN_THOUSAND(10000, "万"),

        ONE_HUNDRED_THOUSAND(100000, "十万"),

        ONE_MILLION(1000000, "百万"),

        TWN_MILLION(10000000, "千万"),

        ONE_HUNDRED_MILLION(100000000, "亿"),

        ;


        private final int code;
        private final String desc;


        GrayPercentEnum(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }
    }


}
