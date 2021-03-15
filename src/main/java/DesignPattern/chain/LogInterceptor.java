package DesignPattern.chain;

import java.lang.reflect.Method;

/**
 * @program: mybatis-source-learn
 * @description:
 * @author: WhyWhatHow
 * @create: 2021-03-14 13:43
 **/
public class LogInterceptor implements  Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws  Exception {
        System.out.println("------------- log info start -----------");
        Object process = invocation.process();
        System.out.println("------------- log info end --------------");
        return process;
    }
}
