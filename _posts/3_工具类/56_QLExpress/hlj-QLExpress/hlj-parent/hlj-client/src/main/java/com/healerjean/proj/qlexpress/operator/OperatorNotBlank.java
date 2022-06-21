package com.healerjean.proj.qlexpress.operator;

import com.ql.util.express.Operator;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhangyujin
 * @date 2022/6/20  20:55.
 */
public class OperatorNotBlank extends Operator {

    @Override
    public Object executeInner(Object[] params) throws Exception {
        return isNotBlank(params[0]) ? true : false;
    }

    private boolean isNotBlank(Object param) {
        if (param == null) {
            return false;
        }
        if (param instanceof String) {
            return StringUtils.isNotEmpty((String) param);
        }
        return true;
    }
}

