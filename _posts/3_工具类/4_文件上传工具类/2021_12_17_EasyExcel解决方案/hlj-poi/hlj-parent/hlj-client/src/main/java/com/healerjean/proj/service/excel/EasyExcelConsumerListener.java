package com.healerjean.proj.service.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import io.swagger.models.auth.In;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * EasyExcel消费监听.
 *
 * @param <T>
 * @author www@yiynx.cn
 */
public class EasyExcelConsumerListener<T> extends AnalysisEventListener<T> {

    /**
     * pageSize
     */
    private Integer pageSize;

    /**
     * list
     */
    private List<T> list;

    /**
     * consumer
     */
    private Consumer<List<T>> consumer;

    /**
     * EasyExcelConsumerListener
     *
     * @param pageSize pageSize
     * @param consumer consumer
     */
    public EasyExcelConsumerListener(int pageSize, Consumer<List<T>> consumer) {

        this.pageSize = pageSize;
        this.consumer = consumer;
        list = new ArrayList<>(pageSize);
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        list.add(data);
        if (list.size() >= pageSize) {
            consumer.accept(list);
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        consumer.accept(list);
    }
}
