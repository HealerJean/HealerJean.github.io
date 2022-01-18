package com.healerjean.proj.spel;

import com.healerjean.proj.spel.dto.Account;
import com.healerjean.proj.spel.dto.Friend;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyujin
 * @date 2022/1/17  8:05 下午.
 * @description
 */
@Slf4j
public class TestMain {

    /**
     * 1、文本表达式
     */
    @Test
    public void test1(){
        ExpressionParser parser = new SpelExpressionParser();

        //字符串解析
        String str = (String) parser.parseExpression("'你好'").getValue();
        System.out.println(str);

        //整型解析
        int intVal = (Integer) parser.parseExpression("0x2F").getValue();
        System.out.println(intVal);

        //双精度浮点型解析
        double doubleVal = (Double) parser.parseExpression("4329759E+22").getValue();
        System.out.println(doubleVal);

        //布尔型解析
        boolean booleanVal = (boolean) parser.parseExpression("true").getValue();
        System.out.println(booleanVal);
    }


    /**
     * 2、对象属性表达式
     */
    @Test
    public void test2() {
        //初始化对象
        Account account = new Account("Deniro");
        account.setFootballCount(10);
        account.setFriend(new Friend("Jack"));

        //解析器
        ExpressionParser parser = new SpelExpressionParser();
        //解析上下文
        EvaluationContext context = new StandardEvaluationContext(account);

        //1、获取不同类型的属性
        String name = (String) parser.parseExpression("Name").getValue(context);
        System.out.println(name);
        int count = (Integer) parser.parseExpression("footballCount+1").getValue(context);
        System.out.println(count);

        //2、 获取嵌套类中的属性
        String friend = (String) parser.parseExpression("friend.name").getValue(context);
        System.out.println(friend);
    }

    /**
     * 3、数组、List 和 Map 表达式
     */
    @Test
    public void test3(){
        //解析器
        ExpressionParser parser  = new SpelExpressionParser();

        // 1、解析一维数组，不支持二维数组
        int[] oneArray = (int[]) parser.parseExpression("new int[]{3,4,5}").getValue();
        System.out.println("一维数组开始：");
        for (int i : oneArray) {
            System.out.println(i);
        }
        System.out.println("一维数组结束");
        //int[][] twoArray = (int[][]) parser.parseExpression("new int[][]{3,4,5}{3,4,5}") .getValue();
        //这里会抛出 SpelParseException

        // 2、解析 list
        List list = (List) parser.parseExpression("{3,4,5}").getValue();
        System.out.println("list:" + list);


        // 3、解析 Map
        Map map = (Map) parser.parseExpression("{account:'deniro',footballCount:10}") .getValue();
        System.out.println("map:" + map);

        // 4、解析对象中的 list
        Account account = new Account("Deniro");
        Friend friend1 = new Friend("Jack");
        Friend friend2 = new Friend("Rose");
        List<Friend> friends = new ArrayList<>();
        friends.add(friend1);
        friends.add(friend2);
        account.setFriends(friends);
        EvaluationContext context = new StandardEvaluationContext(account);
        String friendName = (String) parser.parseExpression("friends[0].name").getValue(context);
        System.out.println("friendName:" + friendName);
    }

    /**
     * 4、方法表达式
     */
    @Test
    public void test4(){
        //解析器
        ExpressionParser parser  = new SpelExpressionParser();

        //1、调用 String 方法
        boolean isEmpty = parser.parseExpression("'Hi,everybody'.contains('Hi')").getValue(Boolean.class);
        System.out.println("isEmpty:" + isEmpty);

        //2、调用对象相关方法
        Account account = new Account("Deniro");
        EvaluationContext context = new StandardEvaluationContext(account);

        //调用公开方法
        parser.parseExpression("setFootballCount(11)").getValue(context, Boolean.class);
        System.out.println("getFootballCount:" + account.getFootballCount());
    }


    /**
     * 5.1、关系操作符
     */
    @Test
    public void test51(){
        //解析器
        ExpressionParser parser = new SpelExpressionParser();

        //1、数值比较
        boolean result=parser.parseExpression("2>1").getValue(Boolean.class);
        System.out.println("2>1:"+result);

        // 2、字符串比较
        result=parser.parseExpression("'z'>'a'").getValue(Boolean.class);
        System.out.println("'z'>'a':"+result);

        // 3、instanceof 运算符
        result=parser.parseExpression("'str' instanceof T(String)").getValue(Boolean.class);
        System.out.println("'str' 是否为字符串 :"+result);

        result=parser.parseExpression("1 instanceof T(Integer)").getValue(Boolean.class);
        System.out.println("1 是否为整型 :"+result);

        //4、正则表达式
        result=parser.parseExpression("22 matches '\\d{2}'").getValue(Boolean.class);
        System.out.println("22 是否为两位数字 :"+result);
    }

    /**
     * 5.2、逻辑操作符
     */
    @Test
    public void test52(){
        //解析器
        ExpressionParser parser  = new SpelExpressionParser();

        //1、与操作
        boolean result=parser.parseExpression("true && true").getValue(Boolean.class);
        System.out.println("与操作:"+result);

        //2、或操作
        result=parser.parseExpression("true || false").getValue(Boolean.class);
        System.out.println("或操作:"+result);

        parser.parseExpression("true or false").getValue(Boolean.class);
        System.out.println("或操作(or 关键字）:"+result);

        //3、非操作
        result=parser.parseExpression("!false").getValue(Boolean.class);
        System.out.println("非操作:"+result);
    }

    /**
     * 5.3、运算操作符
     */
    @Test
    public void test53(){
        ExpressionParser parser  = new SpelExpressionParser();

        //1、加法运算
        Integer iResult = parser.parseExpression("2+3").getValue(Integer.class);
        System.out.println("加法运算：" + iResult);

        String sResult = parser.parseExpression("'Hi,'+'everybody'").getValue(String.class);
        System.out.println("字符串拼接运算：" + sResult);

        //2、减法运算
        iResult = parser.parseExpression("2-3").getValue(Integer.class);
        System.out.println("减法运算：" + iResult);

        //3、乘法运算
        iResult = parser.parseExpression("2*3").getValue(Integer.class);
        System.out.println("乘法运算：" + iResult);

        //4、除法运算
        iResult = parser.parseExpression("4/2").getValue(Integer.class);
        System.out.println("除法运算：" + iResult);

        Double dResult = parser.parseExpression("4/2.5").getValue(Double.class);
        System.out.println("除法运算：" + dResult);

        //5、求余运算
        iResult = parser.parseExpression("5%2").getValue(Integer.class);
        System.out.println("求余运算：" + iResult);

    }

    /**
     * 6、安全导航操作符
     */
    @Test
    public void test6(){
        Account account = new Account("Deniro");
        account.setFriend(new Friend("Jack"));

        //解析器
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext(account);

        String friendName=parser.parseExpression("friend?.name").getValue(context,String.class);
        System.out.println("friendName:"+friendName);

        //设置为 null
        account.setFriend(null);
        friendName=parser.parseExpression("friend?.name").getValue(context,String.class);

        //打印出 null
        System.out.println("friendName:" + friendName);
    }

    /**
     * 7、三元操作符
     */
    @Test
    public void test7(){
        ExpressionParser parser  = new SpelExpressionParser();

        boolean result=parser.parseExpression("(1+2) == 3?true:false").getValue(Boolean.class);
        System.out.println("result:"+result);
    }

    /**
     * 8、变量表达式
     * 1、可以通过 #变量名 来引用在 EvaluationContext 中定义的变量。通过 EvaluationContext#setVariable(name, val) 即可定义新的变量；name 表示变量名，val 表示变量值。
     * 2、如果变量是集合，比如 list，那么可以通过 #scores.[#this] 来引用集合中的元素。
     */
    @Test
    public void test8(){

        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();

         //定义一个新变量，名为 newVal
        context.setVariable("newVal", "Jack");
        String newVal = (String) parser.parseExpression(" #newVal").getValue(context);
        System.out.println("newVal:" + newVal);

        //this 操作符表示集合中的某个元素
        List<Double> scores = new ArrayList<>();
        scores.addAll(Arrays.asList(23.1, 82.3, 55.9));
        context.setVariable("scores", scores);//在上下文中定义 scores 变量
        List<Double> scoresGreat80 = (List<Double>) parser.parseExpression("#scores.?[#this>80]").getValue(context);
        System.out.println("scoresGreat80:" + scoresGreat80);
    }


    /**
     * 9、自定义函数
     */
    @Test
    public void test9() throws NoSuchMethodException {
        StandardEvaluationContext context = new StandardEvaluationContext();
        Method method = Account.class.getDeclaredMethod("method", String.class);
        context.registerFunction("method", method);

        ExpressionParser parser = new SpelExpressionParser();
        String value = parser.parseExpression("#method('healerjean')").getValue(context, String.class);
        System.out.println(value);
    }


    /**
     * 10、类型操作符
     */
    @Test
    public void test10(){
        ExpressionParser parser   = new SpelExpressionParser();

        //加载 java.lang.Integer
        Class integerClass=parser.parseExpression("T(Integer)").getValue(Class.class);
        System.out.println(integerClass==java.lang.Integer.class);

        //调用类静态方法
        double result = (double) parser.parseExpression("T(Math).abs(-2.5)").getValue();
        System.out.println("result:" + result);
    }

}
