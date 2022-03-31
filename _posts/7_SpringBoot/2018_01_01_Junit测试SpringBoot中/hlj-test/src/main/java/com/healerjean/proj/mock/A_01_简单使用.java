package com.healerjean.proj.mock;

import com.healerjean.proj.dto.PersonDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zhangyujin
 * @date 2022/3/29  21:36.
 * @description
 */
@RunWith(MockitoJUnitRunner.class)
public class A_01_简单使用 {


    /**
     * 1、mock出一个虚假的对象
     */
    @Test
    public void test_1() {
        PersonDTO person = Mockito.mock(PersonDTO.class);

    }

    /**
     * 2.1、如何验证方法调没调用
     * times(n)：方法被调用n次
     * never()：没有被调用
     * atLeast(n)：至少被调用n次
     * atLeastOnce()：至少被调用1次，相当于atLeast(1)
     * atMost()：最多被调用n次
     */
    @Test
    public void test2_1() {
        PersonDTO person = Mockito.mock(PersonDTO.class);

        // 1、验证person的getSex得到了调用
        person.getSex(1);
        Mockito.verify(person).getSex(1);
        Mockito.verify(person, Mockito.times(1)).getSex(1);

    }

    /**
     * 2.2验证行为的的调用顺序
     */
    @Test
    public void test2_2(){
        PersonDTO person = Mockito.mock(PersonDTO.class);
        person.getSex(1);
        person.isMan(1);
        InOrder inOrder = Mockito.inOrder(person);
        inOrder.verify(person).getSex(1);
        inOrder.verify(person).isMan(1);
    }



    /**
     * 3、在不关心参数为什么值的情况下，如何通过Mock的语法给出方法所需要的参数对应类型
     * 3.1、anyString()：表示任何一个字符串都可以。下面我列举一些常用的参数适配方法
     * 3.2、anyInt
     * 3.3、anyLong
     * 3.4、anyDouble
     * 3.5、anyObject 表示任何对象
     * 3.6、any(clazz)表示任何属于clazz的对象
     * 3.7、anyCollection
     * 3.8、anyCollectionOf(clazz)
     * 3.9、anyList(Map, set)
     * 3.10、anyListOf(clazz)等等
     */
    @Test
    public void test_3() {
        PersonDTO person = Mockito.mock(PersonDTO.class);
        person.printing("healerjean");
        // 1、只关心打印方法走没走，而不关心他的参数是什么的时候，我们就要用到Mock的any方法
        Mockito.verify(person).printing(Mockito.anyString());
    }


    /**
     * 4、指定某个方法的返回值
     * 4.1、Mockito.when(mockObject.targetMethod(args)).thenReturn(desiredReturnValue);
     *      说明：该方法是执行mockObject.targetMethod(）使其返回特定的值desiredReturnValue，据需用我们的person来举例：
     *
     */
    @Test
    public void test_4_1() {
        PersonDTO person = Mockito.mock(PersonDTO.class);

        // 4.1、当调用person的isMan方法，同时传入"0"时，返回true
        // （注意这个时候我们调用person.isMan(0);的时候值为true而调用其他数字则为false，
        //   如果我们忽略数字，传任何值都返回true时，就可以用到我们上面讲的any()参数适配方法）
        Mockito.when(person.isMan(0)).thenReturn(true);
        // true
        System.out.println(person.isMan(0));
        // false
        System.out.println(person.isMan(1));

        //当调用person的isMan方法，同时传入"0"时，返回false，其他默认也都是 false
        Mockito.when(person.isMan(0)).thenReturn(false);
        // false
        System.out.println(person.isMan(0));
        // false
        System.out.println(person.isMan(1));


        Mockito.when(person.isMan(Mockito.anyInt())).thenReturn(true);
        // true
        System.out.println(person.isMan(0));
        // true
        System.out.println(person.isMan(1));
    }


    /**
     * 4.2、进行异常测试
     */
    @Test
    public void test_4_2() {
        List list = Mockito.mock(List.class);
        list.add("123");
        //1、当list调用clear()方法时会抛出异常
        Mockito.doThrow(new RuntimeException()).when(list).clear();
        list.clear();
    }

    /**
     * 4.3、指定返回值
     */
    @Test
    public void test_4_3() {
        List list = Mockito.mock(List.class);
        Mockito.doReturn("123").when(list).get(Mockito.anyInt());
        System.out.println(list.get(0));
    }

    /**
     * 4.4、
     * ⬤ doNothing() ：指定void方法什么都不做
     * ⬤ doCallRealMethod()：指定方法调用内部的真实逻辑
     */
    @Test
    public void test_4_4(){
        Foo foo = Mockito.mock(Foo.class);

        //1、什么信息也不会打印, mock对象并不会调用真实逻辑
        foo.doFoo();

        //2、啥也不会打印出来
        Mockito.doNothing().when(foo).doFoo();
        foo.doFoo();
        //不会调用真实逻辑，但是int默认值就是0，所以打印0
        // 打印0
        System.out.println(foo.getCount());

        //3、这里会调用真实逻辑, 打印出信息
        Mockito.doCallRealMethod().when(foo).doFoo();
        // 打印："method doFoo called."
        foo.doFoo();

        Mockito.doCallRealMethod().when(foo).getCount();
        // 打印 0
        System.out.println(foo.getCount());

    }


    class Foo {
        public void doFoo() {
            System.out.println("method doFoo called.");
        }
        public int getCount() {
            return 1;
        }
    }


    /**
     * 5、模拟创建@mock注解
     */
    //和 List list = Mockito.mock(List.class);作用一致
    @Mock
    private List list;
    @Test
    public void test_5(){
        list.add("one");
        Mockito.verify(list).add("one");
    }

    /**
     * 6、@Spy注解使用呢
     */
    @Spy
    List<String> spiedList = new ArrayList<>();
    @Test
    public void test_6_1(){
        spiedList.add("one");
        spiedList.add("two");

        // 打印 one
        System.out.println(spiedList.get(0));
        Mockito.verify(spiedList).add("one");
        Mockito.verify(spiedList).add("two");

        //输出 2
        System.out.println(spiedList.size());


        Mockito.doReturn(100).when(spiedList).size();
        //输出 100
        System.out.println(spiedList.size());

    }




}
