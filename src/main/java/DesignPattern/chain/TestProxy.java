package DesignPattern.chain;



//import DesignPattern.demo2.InvocationInterceptor;

import DesignPattern.Target;
import DesignPattern.TargetImpl;

/**
 * @program: mybatis-source-learn
 * @description:
 * @author: WhyWhatHow
 * @create: 2021-03-13 23:04
 **/
public class TestProxy {

    public static void main(String[] args) {

        //testWithoutChain();
        testWithChain();

    }

    private static void testWithoutChain() {
        System.out.println("------------no chain --------");
        LogInterceptor logInterceptor = new LogInterceptor();
        TranslationInterceptor translationInterceptor = new TranslationInterceptor();
//        InterceptorChain interceptorChain = new InterceptorChain();
        Target target =new TargetImpl();
        target = logInterceptor.plugin(translationInterceptor.plugin(target));
//        target = (Target) invocationInterceptorChain.pluginAll(target);
        target.execute(" HelloWord ");
    }
    private static void testWithChain() {
        System.out.println(" ==================chain========================");
        LogInterceptor logInterceptor = new LogInterceptor();
        TranslationInterceptor translationInterceptor = new TranslationInterceptor();
        InterceptorChain chain = new InterceptorChain();
        chain.add(translationInterceptor).add(logInterceptor);
        Target target =new TargetImpl();
        target = chain.pluginAll(target);
//        target = logInterceptor.plugin(translationInterceptor.plugin(target));
//        target = (Target) invocationInterceptorChain.pluginAll(target);
        target.execute(" HelloWord ");
    }
}

