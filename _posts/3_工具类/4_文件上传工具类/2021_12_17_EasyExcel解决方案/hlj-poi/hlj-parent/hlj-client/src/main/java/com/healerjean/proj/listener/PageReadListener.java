package com.healerjean.proj.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author zhangyujin
 * @date 2023/6/25$  10:24$
 */
@Slf4j
public class GeoPageReadListener<T> implements ReadListener<T> {
    /**
     * 单个处理数据量，经测试1000条数据效果较好
     */
    public final static int BATCH_COUNT = 1000;

    /**
     * 数据的临时存储
     */
    private List<T> cachedDataList = new ArrayList<>(BATCH_COUNT);

    /**
     * consumer
     */
    private final Consumer<List<T>> consumer;

    /**
     * GeoPageReadListener
     * @param consumer consumer
     */
    public GeoPageReadListener(Consumer<List<T>> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            log.info("读取数据量:{}", cachedDataList.size());
            consumer.accept(cachedDataList);
            cachedDataList = new ArrayList(BATCH_COUNT);
        }
    }

    /**
     * 所有数据读取完成之后调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (CollectionUtils.isNotEmpty(cachedDataList)) {
            //处理剩余的数据
            log.info("读取数据量:{}", cachedDataList.size());
            consumer.accept(cachedDataList);
        }
    }


}