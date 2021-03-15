package DesignPattern.chain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @program: mybatis-source-learn
 * @description: 方法的实现体
 * @author: WhyWhatHow
 * @create: 2021-03-14 13:27
 **/
public class Invocation  {

    private Method method;
    // 参数列表
    private Object[] args;

    public Invocation(Method method, Object[] args, Object target) {
        this.setMethod(method);
        this.setArgs(args);
        this.setTarget(target);
    }

    private Object target;
    // 执行目标方法
    Object process() throws InvocationTargetException, IllegalAccessException {
        return getMethod().invoke(getTarget(), getArgs());
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
