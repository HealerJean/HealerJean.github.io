package com.healerjean.proj.data.bo.batch.stream;

import com.healerjean.proj.service.RedisService;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 批量消费
 *
 * @author zhangyujin
 * @date 2024/6/14
 */
@Accessors(chain = true)
@Data
public class BatchFunctionBO implements Serializable {


    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7922977872203781755L;

    /**
     * 请求
     */
    private Function<Object, Object> function;

    /**
     * 请求对象
     */
    private ReqBusinessBO reqBusiness;

    /**
     * 返回对象
     */
    private ResBusinessBO resBusiness;


    /**
     * instance
     *
     */
    public static  BatchFunctionBO instance() {
        return new BatchFunctionBO()
                .setReqBusiness(new ReqBusinessBO())
                .setResBusiness(new ResBusinessBO());
    }

    @Accessors(chain = true)
    @Data
    public static class ReqBusinessBO{

        /**
         * 任务名
         */
        private String name;

        /**
         * 请求对象
         */
        private Object req;

        /**
         * 幂等对象
         */
        private IdempotentBO idempotent;

        /**
         * 幂等对象
         */
        @Accessors(chain = true)
        @Data
        public static class IdempotentBO{
            /**
             * 幂等唯一Id
             */
            private String uuid;

            /**
             * 多久幂等（单位s）
             */
            private Integer expireSec;

            /**
             * 幂等操作类
             */
            private RedisService redisService;
        }

    }

    @Accessors(chain = true)
    @Data
    public static class ResBusinessBO{

        /**
         * 执行结果
         */
        private Boolean invokeFlag;

        /**
         * 返回 对象
         */
        private Object res;

        /**
         * 执行异常信息
         */
        private Exception exception;

        /**
         * 消费时间
         */
        private Long cost;
    }

}


