package com.healerjean.proj.qlexpress.operator;

import com.healerjean.proj.qlexpress.QlExpressContext;
import com.ql.util.express.ArraySwap;
import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.OperateData;

import java.util.List;

/**
 * @author zhangyujin
 * @date 2022/6/21  17:05.
 */
public class OperatorDeparmentBelong extends AbstractOperator {

    public final static String SYSTEM_VARIABLE_DEPARTMENT = "DEPARTMENT_BELONG";

    public OperatorDeparmentBelong(String name) {
        this.name = name;
    }

    @Override
    public OperateData executeInner(InstructionSetContext parent, ArraySwap list) throws Exception {
        if (list == null || list.length != 1){
            throw new IllegalArgumentException("[OperatorDeparmentBelong] 数据错误");
        }
        QlExpressContext context = super.getQlExpressContext(parent);
        if (!context.containsKey(SYSTEM_VARIABLE_DEPARTMENT)) {
            throw new IllegalArgumentException("[OperatorDeparmentBelong] 缺少 department_belong");
        }
        List<String> departments = (List<String>)context.get(SYSTEM_VARIABLE_DEPARTMENT);
        String department = (String)list.get(0).getObject(parent);
        boolean contains = departments.contains(department);
        return new OperateData(contains ? "是" : "否", String.class);
    }
}
