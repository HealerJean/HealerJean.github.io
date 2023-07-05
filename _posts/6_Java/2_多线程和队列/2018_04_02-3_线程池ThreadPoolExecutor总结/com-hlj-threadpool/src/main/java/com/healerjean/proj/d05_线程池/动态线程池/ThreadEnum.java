package com.healerjean.proj.d05_线程池.动态线程池;

import java.util.Arrays;

/**
 * ThreadEnum
 *
 * @author zhangyujin
 * @date 2023/6/13  21:46.
 */
public interface ThreadEnum {

    /**
     * RejectPolicyEnum
     *
     * @author zhangyujin
     * @date 2023/4/14  11:33.
     */
    enum RejectPolicyEnum implements ThreadEnum {

        /**
         * RejectPolicyEnum
         */
        ABORT_POLICY("AbortPolicy", "丢弃任务并抛出RejectedExecutionException异常"),
        DISCARD_POLICY("DiscardPolicy", "丢弃任务，但是不抛出异常。"),
        DISCARD_OLDEST_POLICY("DiscardOldestPolicy", "丢弃队列最前面的任务，然后重新提交被拒绝的任务。"),
        CALLER_RUNS_POLICY("CallerRunsPolicy", "由调用线程处理该任务"),
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
         * RejectPolicyEnum
         *
         * @param code code
         * @param desc desc
         */
        RejectPolicyEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * RejectPolicyEnum
         *
         * @param code code
         * @return RejectPolicyEnum
         */
        public static RejectPolicyEnum toRejectPolicyEnum(String code) {
            return Arrays.stream(RejectPolicyEnum.values()).filter(item -> item.getCode().equals(code)).findAny().orElse(null);
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }


    }


    /**
     * QueueEnum
     *
     * @author zhangyujin
     * @date 2023/4/14  11:33.
     */
    enum QueueEnum implements ThreadEnum {

        /**
         * QueueEnum
         */
        SYNCHRONOUS_QUEUE("SynchronousQueue", "无缓冲等待队列"),
        RESIZEABLE_CAPACITY_LINKED_BLOCKING_QUEUE("LinkedBlockingQueue", "无界缓存等待队列"),
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
         * QueueEnum
         *
         * @param code code
         * @param desc desc
         */
        QueueEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * QueueEnum
         *
         * @param code code
         * @return QueueEnum
         */
        public static QueueEnum toQueueEnum(String code) {
            return Arrays.stream(QueueEnum.values()).filter(item -> item.getCode().equals(code)).findAny().orElse(null);
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }


    }


    /**
     * QueueEnum
     *
     * @author zhangyujin
     * @date 2023/4/14  11:33.
     */
    enum ThreadPoolEnum implements ThreadEnum {

        /**
         * QueueEnum
         */
        DEFAULT("default", "默认线程池名称"),
        RPC("rpc", "rpc线程池名称"),
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
         * QueueEnum
         *
         * @param code code
         * @param desc desc
         */
        ThreadPoolEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * ThreadPoolEnum
         *
         * @param code code
         * @return ThreadPoolEnum
         */
        public static ThreadPoolEnum toQueueEnum(String code) {
            return Arrays.stream(ThreadPoolEnum.values()).filter(item -> item.getCode().equals(code)).findAny().orElse(null);
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

    }


}
