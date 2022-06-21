package com.healerjean.proj;


import com.healerjean.proj.operator.JoinOperator;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import org.junit.Test;

import java.util.HashMap;

public class TestMain_1 {


    /**
     * 1、普通java语法
     */
    @Test
    public void test() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("a", 1);
        context.put("b", 2);
        context.put("c", 3);
        String express = "a + b *c ";
        Object r = runner.execute(express, context, null, true, true);
        System.out.println(r);
    }

    /**
     * 2、脚本中定义function
     */
    @Test
    public void test_functionStatement() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        String functionStatement = "function add(int a,int b){\n" +
                "  return a+b;\n" +
                "};\n" +
                "\n" +
                "function sub(int a,int b){\n" +
                "    \n" +
                "  return a - b;\n" +
                "};\n" +
                "\n" +
                "a=10;\n" +
                "return add(a,4) + sub(a,9);";

        Object result = runner.execute(functionStatement, new DefaultContext<>(), null, false, false);
        System.out.println(result);
    }

    /**
     * 3、对Java对象的操作
     * 系统自动会import java.lang.*,import java.util.*;
     */
    @Test
    public void test_objectStatement() throws Exception {
        ExpressRunner runner = new ExpressRunner();

//        TradeEvent tradeEvent = new TradeEvent();
//        tradeEvent.setPrice(20.0);
//        tradeEvent.setName("购物");
//        tradeEvent.setId(UUID.randomUUID().toString());//
//
        String objectStatement = "import me.aihe.demo.trade.TradeEvent;\n" +
                "        tradeEvent = new TradeEvent();\n" +
                "        tradeEvent.setPrice(20.0);\n" +
                "        tradeEvent.id=UUID.randomUUID().toString();\n" +
                "        System.out.println(tradeEvent.getId());\n" +
                "        System.out.println(tradeEvent.price);";
        runner.execute(objectStatement, new DefaultContext<>(), null, false, false);
    }


    /**
     * 4、扩展操作符：Operator
     * 4.1、替换if then else 等关键字
     */
    @Test
    public void test_extendOperatorStatement() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        runner.addOperatorWithAlias("如果", "if", null);
        runner.addOperatorWithAlias("则", "then", null);
        runner.addOperatorWithAlias("否则", "else", null);

        IExpressContext<String, Object> context = new DefaultContext<>();
        context.put("语文", 88);
        context.put("数学", 99);
        context.put("英语", 95);

        String exp = "如果  (语文 + 数学 + 英语 > 270) 则 {return 1;} 否则 {return 0;}";
        Object result = runner.execute(exp, context, null, false, false, null);
        System.out.println(result);
    }


    /**
     * 4、扩展操作符：Operator
     * 4.2.1、addOperator 添加操作符
     */
    @Test
    public void test_add_operator() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        String express = "1 join 2 join 3";
        runner.addOperator("join", new JoinOperator());
        Object result = runner.execute(express, context, null, true, false);
        System.out.println(result);
        // [1, 2, 3]
    }

    /**
     * 4、扩展操作符：Operator
     * 4.2.2、replaceOperator 替换操作符
     */
    @Test
    public void test_replace_operator() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        String express = "1 + 2 + 3";
        runner.replaceOperator("+", new JoinOperator());
        Object result = runner.execute(express, context, null, true, false);
        System.out.println(result);
        // [1, 2, 3]
    }

    /**
     * 4、扩展操作符：Operator
     * 4.2.3、addFunction 添加函数
     */
    @Test
    public void test_add_function() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        String express = "join(1,2,3)";
        runner.addFunction("join", new JoinOperator());
        Object result = runner.execute(express, context, null, true, false);
        System.out.println(result);
        // [1, 2, 3]
    }



    /**
     * 5、绑定java类或者对象的method
     */
    @Test
    public void test_workWithJavaStatement() throws Exception {
        ExpressRunner runner = new ExpressRunner();

        // 1、在使用的时候会创建对象
        runner.addFunctionOfClassMethod("取绝对值", Math.class.getName(), "abs", new String[]{"double"}, null);

        // 2、对象已经存在，直接调用对象中的方法
        runner.addFunctionOfServiceMethod("打印", System.out, "println", new String[]{"String"}, null);

        String exp = "a = 取绝对值(-100); 打印(\"Hello World\"); 打印(a.toString())";
        DefaultContext<String, Object> context = new DefaultContext<>();
        runner.execute(exp, context, null, false, false);
        System.out.println(context);
    }



    /**
     * 6、macro 宏定义
     * 即预先定义一些内容，在使用的时候直接替换Macro中的变量为上下文的内容
     */
    @Test
    public void test_macronStatement() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        //没有顺序限制
        runner.addMacro("是否优秀", "计算平均成绩 > 90");
        runner.addMacro("计算平均成绩", "(语文 + 数学 + 英语 ) /3.0");

        IExpressContext<String, Object> context = new DefaultContext<>();
        context.put("语文", 88);
        context.put("数学", 99);
        context.put("英语", 95);
        Object result = runner.execute("是否优秀", context, null, false, false);
        System.out.println(result);
    }


    /**
     * 7、编译脚本，查询外部需要定义的变量和函数。
     */
    @Test
    public void test_getOutVarNames() throws Exception {
        String express = "int 平均分 = (语文+数学+英语+综合考试.科目2)/4.0;return 平均分";
        ExpressRunner runner = new ExpressRunner(true,true);
        String[] names = runner.getOutVarNames(express);
        for(String s:names){
            System.out.println("var : " + s);
        }

        // var : 数学
        // var : 综合考试
        // var : 英语
        // var : 语文

    }



    /**
     * 8、集合的快捷写法
     */
    @Test
    public void test_fast() throws Exception {
        ExpressRunner runner = new ExpressRunner(false,false);
        DefaultContext<String, Object> context = new DefaultContext<>();
        String express = "abc = NewMap(1:1,2:2); return abc.get(1) + abc.get(2);";
        Object r = runner.execute(express, context, null, false, false);
        System.out.println(r);
        //3

        express = "abc = NewList(1,2,3); return abc.get(1)+abc.get(2)";
        r = runner.execute(express, context, null, false, false);
        System.out.println(r);
        //5

        express = "abc = [1,2,3]; return abc[1]+abc[2];";
        r = runner.execute(express, context, null, false, false);
        System.out.println(r);
        //5

    }

    /**
     * 9、集合的遍历
     */
    @Test
    public void test_collectionStatement() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> defaultContext = new DefaultContext<>();
        HashMap<String, String> mapData = new HashMap() {{
            put("a", "hello");
            put("b", "world");
            put("c", "!@#$");
        }};
        defaultContext.put("map", mapData);
        //ql不支持for(obj:list){}的语法，只能通过下标访问。
        String mapTraverseStatement = " keySet = map.keySet();\n" +
                "  objArr = keySet.toArray();\n" +
                "  for (i=0;i<objArr.length;i++) {\n" +
                "  key = objArr[i];\n" +
                "   System.out.println(map.get(key));\n" +
                "  }";
        runner.execute(mapTraverseStatement, defaultContext, null, false, false);
        System.out.println(defaultContext);

        // hello
        // world
        // !@#$
        // {i=3, objArr=[Ljava.lang.Object;@6b419da, map={a=hello, b=world, c=!@#$}, keySet=[a, b, c], key=c}

    }



}
