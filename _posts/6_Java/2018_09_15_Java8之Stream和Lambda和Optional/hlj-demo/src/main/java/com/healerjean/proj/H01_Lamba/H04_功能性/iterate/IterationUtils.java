package com.healerjean.proj.H01_Lamba.H04_功能性.iterate;


import org.junit.Test;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * IterationUtils
 *
 * @author zhangyujin
 * @date 2025/9/11
 */
public final class IterationUtils {


    /**
     * 执行一个从 0 到 limit-1 的循环，并在每次迭代时应用处理逻辑。
     *
     * @param iterateData 包含处理逻辑和上下文的列表
     * @return 实际执行的迭代次数
     */
    @SuppressWarnings("unchecked")
    public static <I> long executeIterate(List<IterateData<I, ?>> iterateData) {
        long count = 0;
        for (long i = 0; i < 100; i++) {
            for (IterateData<I, ?> data : iterateData) {
                DemoBO demo = new DemoBO();
                demo.setId(i);
                demo.setName(new StringBuilder("name").append(i).toString());

                IterateContext<Object, ?> context = (IterateContext<Object, ?>) data.getIterateContext();
                context.setCurrentItem(demo);
                Consumer<?> rawConsumer = data.getConsumer();
                Consumer<IterateContext<Object, ?>> consumer = (Consumer<IterateContext<Object, ?>>) rawConsumer;
                consumer.accept(context);
            }
            count++;
        }
        return count;
    }


    @Test
    public void test() {
        IterateContext<DemoBO, Map<Long, DemoBO>> oneIterateContext = new IterateContext<DemoBO, Map<Long, DemoBO>>().setResult(new HashMap<>());
        IterateData<DemoBO, Map<Long, DemoBO>> one = new IterateData<>();
        one.setType("one1");
        one.setConsumer((r) -> getPut(oneIterateContext));
        one.setIterateContext(oneIterateContext);

        IterateContext<DemoBO, List<DemoBO>> twoIterateContext = new IterateContext<DemoBO,List<DemoBO>>().setResult(new ArrayList<>());
        IterateData<DemoBO, List<DemoBO>> two = new IterateData<>();
        two.setType("test");
        two.setConsumer((r) -> twoIterateContext.getResult().add(twoIterateContext.getCurrentItem()));
        two.setIterateContext(twoIterateContext);


        List<IterateData<DemoBO,?>> iterateConsumers = new ArrayList<>();
        iterateConsumers.add(one);
        iterateConsumers.add(two);
        Long size = IterationUtils.executeIterate(iterateConsumers);

        System.out.println(MessageFormat.format("size={0}", size));
        iterateConsumers.forEach(item -> System.out.println(item.getIterateContext().getResult()));
    }

    private static DemoBO getPut(IterateContext<DemoBO, Map<Long, DemoBO>> oneIterateContext) {
        return oneIterateContext.getResult().put(oneIterateContext.getCurrentItem().getId(), oneIterateContext.getCurrentItem());
    }


}
