package DesignPattern.chain;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: mybatis-source-learn
 * @description: // 拦截器链
 * @author: WhyWhatHow
 * @create: 2021-03-14 13:52
 **/
public class InterceptorChain {
    // 拦截器链
    List<Interceptor> list =new ArrayList<>();
    // 为target对象添加拦截器
    <T> T pluginAll(T target){
        for (Interceptor interceptor : list) {
            target =interceptor.plugin(target);
        }
        return target;
    }
    // 添加拦截器
    InterceptorChain add(Interceptor interceptor){
        list.add(interceptor);
        return this ;
    }

}
