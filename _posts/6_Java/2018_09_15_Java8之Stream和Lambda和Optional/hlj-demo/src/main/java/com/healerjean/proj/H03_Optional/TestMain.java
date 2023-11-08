package com.healerjean.proj.H03_Optional;

import com.healerjean.proj.H02_Stream.H03_collect.H04_groupby分组.dto.Person;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */

public class TestMain {

    /**
     * 1、get 解释：如果Optional有值则将其返回，否则抛出NoSuchElementException。
     * 2、of  解释：通过工厂方法创建Optional类，创建对象时传入的参数不能为null。如果传入参数为null，则抛出NullPointerException 。
     */
    @Test
    public void test1() {
        //调用工厂方法创建Optional实例
        Optional<String> name = Optional.of("Sanaulla");
        System.out.println("name:" + name.get());         //name:Sanaulla
        //传入参数为null，抛出NullPointerException.
        Optional<String> someNull = Optional.of(null);// java.lang.NullPointerException
        System.out.println("someNull" + someNull);
    }

    /**
     * 2、 ofNullable
     * 解释： (ofNullable与of方法相似，唯一的区别是可以接受参数为null的情况)，为指定的值创建一个Optional，如果指定的值为null，则返回一个空的Optional。
     */
    @Test
    public void test2() {
        //下面创建了一个不包含任何值的Optional实例
        //例如，值为'null'
        Optional<String> empty = Optional.ofNullable(null);
        System.out.println(empty.get()); //java.util.NoSuchElementException: No value present
    }

    /**
     * 3 、isPresent、如果值存在返回true，否则返回false。
     */
    @Test
    public void isPresent() {

        Optional<String> name = Optional.ofNullable("HealerJean");

        //isPresent方法用来检查Optional实例中是否包含值
        if (name.isPresent()) {
            System.out.println(name.get());
        }
        name.ifPresent((value) -> {
            System.out.println("The length of the value is: " + value.length());
        });

        Optional<String> namenull = Optional.ofNullable(null);
        if (!namenull.isPresent()) {
            System.out.println("namenull 为 null");
        }
    }

    /**
     * 4、 orElse：如果有值则将其返回，否则返回指定的其它值。
     */
    @Test
    public void orElse() {
        Optional<String> empty = Optional.empty();
        //如果为null，返回传入的消息。
        //输出：There is no value present!
        System.out.println(empty.orElse("There is no value present!"));


        Optional<String> name = Optional.of("HealerJean");
        //如果值不为null，orElse方法返回Optional实例的值。
        //输出：HealerJean
        System.out.println(name.orElse("There is some value!"));
    }


    /**
     * 5、 orElseGet
     * orElseGet与orElse方法类似，区别在于得到的默认值。
     * orElse方法将传入的字符串作为默认值，orElseGet方法可以接受Supplier接口的实现用来生成默认值。示例如下：
     */
    @Test
    public void orElseGet() {
        //orElseGet与orElse方法类似，区别在于orElse传入的是默认值，
        //orElseGet可以接受一个lambda表达式生成默认值。
        Optional<String> empty = Optional.empty();
        System.out.println(empty.orElseGet(() -> "Default Value"));
        //输出：Default Value

        Optional<String> name = Optional.of("HealerJean");
        System.out.println(name.orElseGet(() -> "Default Value"));
        //输出：HealerJean

    }

    /**
     * 比较  orElse  orElseGet null一致
     */
    @Test
    public void test3() {
        Person person = null;
        System.out.println("Using orElse");
        Person result = Optional.ofNullable(person).orElse(createNewUser());
        System.out.println("Using orElseGet");
        Person result2 = Optional.ofNullable(person).orElseGet(() -> createNewUser());
    }

    /**
     * 比较  orElse  orElseGet  非null时候
     * 解释：orElse 即使不打印自身也会执行orElse里面的东西，但是orElseGet 就不会执行，而是直接返回值该有的值
     */
    @Test
    public void test4() {
        Person person = new Person(1L, "1234");
        System.out.println("Using orElse");
        Person result = Optional.ofNullable(person).orElse(createNewUser());
        System.out.println("Using orElseGet");
        Person result2 = Optional.ofNullable(person).orElseGet(() -> createNewUser());
    }

    private Person createNewUser() {
        System.out.println("Creating New User");
        return new Person(1L, "HealerJean");
    }



    @Test
    public void map() {
        Optional<String> name = Optional.ofNullable("HealerJean");
        Optional<String> upperName = name.map((value) -> value.toUpperCase());
    }

    @Test
    public void filter() {
        Optional<String> name = Optional.of("HealerJean");
        Optional<String> longName = name.filter((value) -> value.length() > 6);
    }


    @Test
    public void test() {
        // String key = "info";
        // Map<String, Object> map = new HashMap<>();
        // map.put(key, "1,2");
        // String value = Optional.ofNullable(map).map(m -> m.getOrDefault(key, "no_active").toString().split(",")[0]).orElse("no_active");
        // System.out.println("map有数据" + value);


        // String key = "info";
        // Map<String, Object> map = new HashMap<>();
        // String value = Optional.ofNullable(map).map(m -> m.getOrDefault(key, "no_active").toString().split(",")[0]).orElse("no_active");
        // System.out.println("map没数据" + value);

        // String key = "info";
        // Map<String, Object> map = new HashMap<>();
        // String value = Optional.ofNullable(map).map(m -> m.getOrDefault(key, "no_active").toString().split(",")[0]).orElse("no_active");
        // System.out.println("map为空" + value);


        String key = "info";
        Map<String, Object> map = null;
        String value = Optional.ofNullable(map).map(m -> m.getOrDefault(key, "no_active").toString().split(",")[0]).orElse("no_active");
        System.out.println("map为null" + value);
    }


}
