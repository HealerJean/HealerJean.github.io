package com.healerjean.proj.qlexpress.main.operator;

import com.ql.util.express.ArraySwap;
import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.OperateData;
import com.ql.util.express.instruction.op.OperatorBase;

/**
 * @author zhangyujin
 * @date 2022/6/21  15:48.
 */
public class OperatorContextPut extends OperatorBase {


    public OperatorContextPut(String aName) {
        this.name = aName;
    }

    @Override
    public OperateData executeInner(InstructionSetContext parent, ArraySwap list) throws Exception {
        String key = list.get(0).toString();
        Object value = list.get(1);
        parent.put(key, value);

        String message = "name:".concat(name).concat(",")
                         .concat("key:").concat(key).concat(",")
                         .concat("value:").concat(value.toString());
        System.out.println(message);
        return new OperateData(message, String.class);
    }

}
