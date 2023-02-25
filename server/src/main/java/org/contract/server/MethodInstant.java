package org.contract.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.contract.common.JsonUtils;
import org.contract.common.StringUtils;

import java.lang.reflect.Method;

public class MethodInstant {
    private final static Logger log = LogManager.getLogger(MethodInstant.class);

    private Method method;

    private Object instant;

    public MethodInstant(Method method, Object instant) {
        this.method = method;
        this.instant = instant;
    }

    public String call(Object parameter) {
        try {
            Object result = method.invoke(instant, parameter);
            if(String.class.isAssignableFrom(result.getClass())) {
                return (String) result;
            } else {
                return JsonUtils.toJson(result);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return StringUtils.EMPTY;
        }
    }
}
