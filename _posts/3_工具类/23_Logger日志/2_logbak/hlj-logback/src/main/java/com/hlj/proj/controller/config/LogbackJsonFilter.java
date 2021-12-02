package com.hlj.proj.controller.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.hlj.proj.controller.utils.JsonUtils;


/**
 * @author HealerJean
 * @ClassName LogbackJsonFilter
 * @date 2020/3/19  11:41.
 * @Description
 */
public class LogbackJsonFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        // if (event.getLoggerName().startsWith("com.hlj")) {
        //     Object[] params = event.getArgumentArray();
        //     for (int index = 0; index < params.length; index++) {
        //         Object param = params[index];
        //         // class.isPrimitive() 8种基本类型的时候为 true，其他为false
        //         if (!param.getClass().isPrimitive()) {
        //             params[index] = JsonUtils.toJsonString(param);
        //         }
        //     }
        // }
        return FilterReply.ACCEPT;
    }
}
