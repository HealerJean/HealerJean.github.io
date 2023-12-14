package com.healerjean.proj.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * FileTaskEnum
 *
 * @author zhangyujin
 * @date 2023/6/26$  19:55$
 */
public interface FileTaskEnum {

    /**
     * TaskType
     */
    @AllArgsConstructor
    @Getter
    enum TaskTypeEnum {
        /**
         * TaskType
         */
        EXPORT("export", "导出"),
        IMPORT("import", "导入"),
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
         * 获取taskType
         *
         * @param code code
         * @return return
         */
        public static TaskTypeEnum toTaskTypeEnum(String code) {
            return Arrays.stream(TaskTypeEnum.values()).filter(item -> item.getCode().equals(code)).findFirst().orElse(null);
        }
    }

    /**
     * 业务类型
     */
    @AllArgsConstructor
    @Getter
    enum BusinessTypeEnum {
        /**
         * BusinessType
         */
        IMPORt_DEMO("importDemo", "导入Demo"),
        EXPORT_DEMO("exportDemo", "导出Demo"),

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
         * toBusinessTypeEnum
         *
         * @param code code
         * @return BusinessTypeEnum
         */
        public static BusinessTypeEnum toBusinessTypeEnum(String code) {
            return Arrays.stream(BusinessTypeEnum.values()).filter(item -> item.getCode().equals(code)).findFirst().orElse(null);
        }
    }


    /**
     * TaskStatusEnum
     */
    @AllArgsConstructor
    @Getter
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

    }


    /**
     * TaskErrorEnum
     */
    @AllArgsConstructor
    @Getter
    enum TaskErrorEnum {
        /**
         * TaskErrorEnum
         */
        SUCCESS("Success", "处理成功"),
        EXCEPTION("Exception", "任务异常"),
        ;

        /**
         * code
         */
        private final String code;

        /**
         * desc
         */
        private final String desc;

    }

}
