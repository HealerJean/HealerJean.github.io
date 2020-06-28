package com.healerjean.proj.study.spi.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.healerjean.proj.study.spi.Animal;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

/**
 * @author HealerJean
 * @ClassName DubboSPITest
 * @date 2020-06-26  16:03.
 * @Description
 */
@Slf4j
public class DubboSPITest {

    @Test
    public void load()  {
        ExtensionLoader<Animal> annoimalExtensionLoader =  ExtensionLoader.getExtensionLoader(Animal.class);
        log.info("#######################################");
        Animal dog = annoimalExtensionLoader.getExtension("dog");
        dog.call("旺旺旺", null);
        Animal cat = annoimalExtensionLoader.getExtension("cat");
        cat.call("我是一只猫", null);
        log.info("#######################################");
    }


    /**
     * 1、 @SPI注解中有value值，URL中没有具体的值
     * 2、@SPI注解中有value值，URL中也有具体的值
     */
    @Test
    public void test1()  {
        ExtensionLoader<Animal> annoimalExtensionLoader =  ExtensionLoader.getExtensionLoader(Animal.class);
        Animal adaptiveExtension = annoimalExtensionLoader.getAdaptiveExtension();
        //1、 @SPI注解中有value值，URL中没有具体的值
        URL url = URL.valueOf("test://localhost/test");
        adaptiveExtension.call("哒哒哒", url); //我是适配动物，发出叫声：哒哒哒

        //2、@SPI注解中有value值，URL中也有具体的值
        url = URL.valueOf("test://localhost/test?animal=cat");
        adaptiveExtension.call("喵喵喵", url);  //  我是猫，发出叫声： 喵喵喵
    }

    /**
     * 3、SPI注解中有value(dog)值，URL中也有具体的值(cat),实现类AdaptiveAnimal上有@Adaptive注解
     */
    @Test
    public void test3()  {
        ExtensionLoader<Animal> annoimalExtensionLoader =  ExtensionLoader.getExtensionLoader(Animal.class);
        Animal adaptiveExtension = annoimalExtensionLoader.getAdaptiveExtension();
        URL url = URL.valueOf("test://localhost/test?animal=cat");
        adaptiveExtension.call("哒哒哒", url); //我是适配动物，发出叫声：哒哒哒
    }


    /**
     * SPI注解中有value值,实现类上没有@Adaptive注解，方法上的@Adaptive注解，注解中的value与链接中的参数的key一致，链接中的key对应的value就是spi中的name,获取相应的实现类。
     */
    @Test
    public void test4()  {
        ExtensionLoader<Animal> annoimalExtensionLoader =  ExtensionLoader.getExtensionLoader(Animal.class);
        Animal adaptiveExtension = annoimalExtensionLoader.getAdaptiveExtension();
        URL url = URL.valueOf("test://localhost/test?name=dog");
        adaptiveExtension.call("汪汪汪", url);
    }

    // 1. 在类上加上@Adaptive注解的类，是最为明确的创建对应类型Adaptive类。所以他优先级最高。
    // 2. @SPI注解中的value是默认值，如果通过URL获取不到关于取哪个类作为Adaptive类的话，就使用这个默认值，当然如果URL中可以获取到，就用URL中的。
    // 3. 方法上的@Adaptive注解，注解中的value与链接中的参数的key一致，链接中的key对应的value就是spi中的name,获取相应的实现类。


    @Test
    public void getActive() {
        ExtensionLoader<Animal> annoimalExtensionLoader = ExtensionLoader.getExtensionLoader(Animal.class);
        URL url = URL.valueOf("test://localhost/test");
        List<Animal> list = annoimalExtensionLoader.getActivateExtension(url, new String[]{}, "default_group");
        list.stream().forEach(System.out::println);
        log.info("-----------------");
        url = URL.valueOf("test://localhost/test?valueAc=fasdjafjdklj");
        list = annoimalExtensionLoader.getActivateExtension(url, new String[]{}, "default_group");
        list.stream().forEach(System.out::println);
    }
    // 1. 根据loader.getActivateExtension中的group和搜索到此类型的实例进行比较，如果group能匹配到，就是我们选择的，也就是在此条件下需要激活的。
    //  2. @Activate中的value是参数是第二层过滤参数（第一层是通过group），在group校验通过的前提下，如果URL中的参数（k）与值（v）中的参数名同@Activate中的value值一致或者包含，那么才会被选中。相当于加入了value后，条件更为苛刻点，需要URL中有此参数并且，参数必须有值。
    //  3.@Activate的order参数对于同一个类型的多个扩展来说，order值越小，优先级越高。


}
