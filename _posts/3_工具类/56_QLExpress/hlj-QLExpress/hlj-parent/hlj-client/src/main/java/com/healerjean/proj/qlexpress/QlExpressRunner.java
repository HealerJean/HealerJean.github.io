package com.healerjean.proj.qlexpress;

import com.google.common.collect.Sets;
import com.healerjean.proj.qlexpress.dto.UserDTO;
import com.healerjean.proj.qlexpress.operator.OperatorDeparmentBelong;
import com.healerjean.proj.qlexpress.operator.OperatorNotBlank;
import com.healerjean.proj.qlexpress.service.UserService;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.instruction.op.OperatorBase;
import com.ql.util.express.instruction.op.OperatorMinMax;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 语法分析和计算的入口类
 *
 * @author zhangyujin
 * @date 2022/6/20  20:51.
 */
public class QlExpressRunner extends ExpressRunner {

    /**
     * 类是否已经加载
     */
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    /**
     * 函数名称集合
     */
    private Set<String> allFunctionNames = Sets.newHashSet();

    public QlExpressRunner() {
        super(true, false);
        init();
    }

    private void init() {
        // 保证之初始化一次
        if (!isRunning.compareAndSet(false, true)) {
            return;
        }

        try {
            initFunctions();
            initFunctionOfClassMethods();
            initOperatorWithAlias();
            initMacros();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }
    /**
     * 初始化函数
     */
    private void initFunctions() {
        addFunction("是否不为空", new OperatorNotBlank());
        addFunction("最小", new OperatorMinMax("min"));
        addFunction("最大", new OperatorMinMax("max"));
        addFunction("部门归属", new OperatorDeparmentBelong("部门归属"));
    }

    @Override
    public void addFunction(String name, OperatorBase op) {
        super.addFunction(name, op);
        if (isRunning != null && isRunning.get()) {
            allFunctionNames.add(name);
        }
    }

    /**
     * 初始化类中已知存在的方法
     *
     * @throws Exception 异常抛出
     */
    private void initFunctionOfClassMethods() throws Exception {
        addFunctionOfClassMethod("取绝对值", Math.class.getName(), "abs", new String[]{"double"}, null);
        addFunctionOfClassMethod("registerCheck", UserService.class.getName(), "registerCheck", new String[]{UserDTO.class.getName(), "boolean"}, null);
    }

    /**
     * 替换关键字初始化
     *
     * @throws Exception 异常抛出
     */
    private void initOperatorWithAlias() throws Exception {
        addOperatorWithAlias("如果", "if", null);
        addOperatorWithAlias("则", "then", null);
        addOperatorWithAlias("否则", "else", null);
    }

    /**
     * 初始化 自定义宏
     */
    private void initMacros() throws Exception {
        addMacro("用户是否注册", "registerCheck(userInfo, login)");
    }



}
