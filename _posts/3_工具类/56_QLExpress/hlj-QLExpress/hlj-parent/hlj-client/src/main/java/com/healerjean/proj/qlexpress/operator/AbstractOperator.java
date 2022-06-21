package com.healerjean.proj.qlexpress.operator;

import com.healerjean.proj.qlexpress.QlExpressContext;
import com.ql.util.express.IExpressContext;
import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.instruction.op.OperatorBase;

/**
 * @author zhangyujin
 * @date 2022/6/20  20:53.
 */
public abstract class AbstractOperator extends OperatorBase {

    public QlExpressContext getQlExpressContext(InstructionSetContext instructionSetContext){
        IExpressContext<String, Object> parent = instructionSetContext.getParent();
        if (parent instanceof InstructionSetContext) {
            return (QlExpressContext)((InstructionSetContext) parent).getParent();
        }
        return (QlExpressContext)parent;
    }
}

