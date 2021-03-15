package DesignPattern.chain;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @program: mybatis-source-learn
 * @description: target对象的代理类
 * @author: WhyWhatHow
 * @create: 2021-03-14 13:30
 **/

public class TargetProxy  implements InvocationHandler {

    public TargetProxy(Interceptor interceptor, Object target) {
        this.interceptor = interceptor;
        this.target = target;
    }

    Interceptor interceptor;
    // 方法的实现体
//    Invocation invocation;
    // 代理对象
    Object target;
    //
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        method.invoke(target,args);
//        interceptor.intercept(target);
        Invocation invocation =new Invocation(method,args,target);
        return  interceptor.intercept(invocation);

    }
    // 包装类
    public static <T>T wrap(Object target, Interceptor interceptor){
        TargetProxy targetProxy = new TargetProxy(interceptor, target);
        Object o = Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),targetProxy );
        return (T) o;
    }
}
