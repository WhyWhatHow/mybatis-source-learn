package DesignPattern.chain;

/**
 * @program: mybatis-source-learn
 * @description:
 * @author: WhyWhatHow
 * @create: 2021-03-14 13:43
 **/
public class TranslationInterceptor implements  Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws  Exception {
        System.out.println("------------- 事务 info start -----------");
        Object process = invocation.process();
        System.out.println("------------- 事务 info end --------------");
        return process;
    }
}
