package com.healerjean.proj.task;

/**
 * TaskEnum
 *
 * @author zhangyujin
 * @date 2023/6/25$  21:29$
 */
public interface TaskEnum {

    /**
     * TaskType
     */
    enum TaskTypeEnum {
        /**
         * TaskType
         */
        DOWNLOAD("download", "下载"),
        UPLOAD_LOAD("upload", "上传"),
        ;

        /**
         * code
         */
        private final String code;

        /**
         * desc
         */
        private final String desc;


        /**
         * TaskType
         * @param code code
         * @param desc desc
         */
        TaskTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }


        public String getDesc() {
            return desc;
        }
    }

    /**
     * 业务类型
     */
    enum BusinessTypeEnum {
        /**
         * BusinessType
         */
        VENDOR_POLICY_DOWNLOAD("vendorPolicyDownload", "商家保单导出"),
        FACTORY_POLICY_DOWNLOAD("factoryPolicyDownload", "供应商保单导出"),
        SUPPLIER_POLICY_DOWNLOAD("supplierPolicyDownload", "供应商保单导出"),

        ;

        /**
         * code
         */
        private final String code;

        /**
         * desc
         */
        private final String desc;


        /**
         * BusinessType
         * @param code code
         * @param desc desc
         */
        BusinessTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }


    /**
     * TaskStatusEnum
     */
    enum TaskStatusEnum {
        /**
         * TaskStatusEnum
         */
        PROCESSING("processing", "处理中"),
        COMPLETED("completed", "完成"),
        FAIL("fail", "处理失败"),

        ;

        /**
         * code
         */
        private final String code;

        /**
         * desc
         */
        private final String desc;


        /**
         * TaskStatusEnum
         * @param code code
         * @param desc desc
         */
        TaskStatusEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }


        public String getDesc() {
            return desc;
        }
    }

}
