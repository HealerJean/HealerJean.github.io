package com.hlj.proj.controller.log.plugin;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * LogMqAppender
 *
 * @author zhangyujin
 * @date 2024/3/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MqAppender extends  UnsynchronizedAppenderBase<ILoggingEvent>{

    /**
     * layout
     */
    private Layout<ILoggingEvent> layout;

    /**
     * params
     */
    private String params;


    /**
     *
     */
    @Override
    public void start(){
        //这里可以做些初始化判断 比如layout不能为null ,
        if(layout == null) {
            addWarn("Layout was not defined");
        }
        //或者写入数据库 或者redis时 初始化连接等等
        super.start();
    }


    @Override
    public void stop()
    {
        //释放相关资源，如数据库连接，redis线程池等等
        System.out.println("logback-stop方法被调用");
        if(!isStarted()) {
            return;
        }
        super.stop();
    }

    @Override
    public void append(ILoggingEvent event) {
        if (event == null || !isStarted()){
            return;
        }
        // 此处自定义实现输出
        // 日志信息
        String logInfo = layout.doLayout(event);

        // 打印信息
        String message = event.getMessage();
        System.out.print(params + "：" + logInfo);
        System.out.print(params + "：" + message);

    }
}