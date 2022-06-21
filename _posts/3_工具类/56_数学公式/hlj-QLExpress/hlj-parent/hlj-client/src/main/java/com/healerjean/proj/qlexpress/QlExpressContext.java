package com.healerjean.proj.qlexpress;

import com.ql.util.express.IExpressContext;
import lombok.Getter;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangyujin
 * @date 2022/6/20  20:48.
 */
public class QlExpressContext extends HashMap<String, Object> implements IExpressContext<String, Object> {

    @Getter
    private ApplicationContext applicationContext;

    public QlExpressContext() {
    }

    public QlExpressContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public QlExpressContext(Map<String, Object> aProperties, ApplicationContext applicationContext) {
        super(aProperties);
        this.applicationContext = applicationContext;
    }

    public Object get(String key) {
        Object result = super.get(key);
        if (result == null && applicationContext != null && applicationContext.containsBean(key)) {
            result = applicationContext.getBean(key);
        }
        return result;
    }

    /**
     * 把key-value放到容器里面去
     *
     * @param key
     * @param value
     */
    @Override
    public Object put(String key, Object value) {
        return super.put(key, value);
    }
}
