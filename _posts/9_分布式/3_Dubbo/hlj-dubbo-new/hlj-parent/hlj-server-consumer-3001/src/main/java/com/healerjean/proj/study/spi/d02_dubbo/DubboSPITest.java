package com.healerjean.proj.study.spi.d02_dubbo;

import com.healerjean.proj.study.spi.d01_java.api.Robot;
import com.healerjean.proj.study.spi.d02_dubbo.api.Animal;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.extension.SPI;
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
    public void test()  {
        ExtensionLoader<Animal> extensionLoader =  ExtensionLoader.getExtensionLoader(Animal.class);
        log.info("获取接口实现类的名称 {}", extensionLoader.getSupportedExtensions());
        log.info("----------------------------");

        Animal cat = extensionLoader.getExtension("dog");

        Animal adaptiveAnimal = extensionLoader.getExtension("adaptiveAnimal");

    }


    // 1、在类上加上`@Adaptive`注解的类，是最为明确的创建对应类型Adaptive类。所以他优先级最高
    // 2、如果作用在方法上，注解中的`value`与链接中的参数的`key`一致，链接中的`key`对应的`value`就是`spi`中的`name`,获取相应的实现类。，如果未设置 value，则根据接口名生成 value，比如接口 Animal生成 url地址参数名= “animal”。
    // 3、如果没有在某个实现类加`@@Adaptive`，则默认的扩展又  `@SPI`注解指定，此时如果URL中可以获取到真实的值（如果是错误的则会报错），就用URL中的，如果通过URL获取不到关于取哪个类作为Adaptive类的话，就使用这个`@SP`I注解指定的默认值，如果这个默认值也找不到则包异常


    /**
     * 1、 @SPI注解中有value值，URL中没有具体的值
     * 2 、@SPI注解中有value值，URL中有具体的值
     */
    @Test
    public void testAdaptive1()  {
        ExtensionLoader<Animal> annoimalExtensionLoader =  ExtensionLoader.getExtensionLoader(Animal.class);
        annoimalExtensionLoader.getSupportedExtensions();
        Animal adaptiveExtension = annoimalExtensionLoader.getAdaptiveExtension();

        //1、 @SPI(value = "adaptiveAnimal")，URL中没有具体的值，则获取的是适配类
        URL url = URL.valueOf("test://localhost/test");
        adaptiveExtension.call("哒哒哒", url);

        //2、@SPI(value = "adaptiveAnimal")，URL中有具体的值
        url = URL.valueOf("test://localhost/test?animal=cat");
        adaptiveExtension.call("喵喵喵", url);
        // 我是猫： 喵喵喵
    }


    /**
     * 3、SPI注解中有value(dog)值，URL中也有具体的值(cat),实现类AdaptiveAnimal上有@Adaptive注解
     */
    @Test
    public void testAdaptive2()  {
        ExtensionLoader<Animal> annoimalExtensionLoader =  ExtensionLoader.getExtensionLoader(Animal.class);
        Animal adaptiveExtension = annoimalExtensionLoader.getAdaptiveExtension();
        URL url = URL.valueOf("test://localhost/test?animal=cat");
        adaptiveExtension.call("哒哒哒", url);
    }


    /**
     * SPI注解中有value值,实现类上没有@Adaptive注解，方法上的@Adaptive注解，注解中的value与链接中的参数的key一致，
     */
    @Test
    public void testAdaptive3()  {
        ExtensionLoader<Animal> annoimalExtensionLoader =  ExtensionLoader.getExtensionLoader(Animal.class);
        Animal adaptiveExtension = annoimalExtensionLoader.getAdaptiveExtension();
        URL url = URL.valueOf("test://localhost/test?aname=dog");
        adaptiveExtension.call("汪汪汪", url);

         url = URL.valueOf("test://localhost/test?bname=dog");
        adaptiveExtension.call("汪汪汪", url);
    }








    @Test
    public void getActive() {
        ExtensionLoader<Animal> annoimalExtensionLoader = ExtensionLoader.getExtensionLoader(Animal.class);
        URL url = URL.valueOf("test://localhost/test");
        List<Animal> list = annoimalExtensionLoader.getActivateExtension(url, new String[]{}, "default_group");
        list.stream().forEach(System.out::println); //null

        log.info("-----------------");
        url = URL.valueOf("test://localhost/test?valueAc=fasdjafjdklj");
        list = annoimalExtensionLoader.getActivateExtension(url, new String[]{}, "default_group");
        list.stream().forEach(System.out::println);
    }
    // 1、`group`和搜索到此类型的实例进行比较，如果group能匹配到，就是我们选择的，也就是在此条件下需要激活的
    // 2、`value`是参数是第二层过滤参数（第一层是通过`group`），在group校验通过的前提下，如果URL中的参数（k）与值（v）中的参数名同`@Activate`中的`value`值一致或者包含，那么才会被选中。相当于加入了`value`后，条件更为苛刻点，需要URL中有此参数并且，参数必须有值。
    // 3、`order`参数对于同一个类型的多个扩展来说，order值越小，优先级越高。


}
