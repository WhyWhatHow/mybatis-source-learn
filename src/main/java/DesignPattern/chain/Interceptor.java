package DesignPattern.chain;

// 拦截器
public interface Interceptor {
    /**
     *    拦截方法执行
     *
     */
        Object intercept(Invocation invocation) throws  Exception;
        // 将 target 与当前 interceptor 绑定
        default <T>T  plugin(Object target){
            return  TargetProxy.wrap(target,this);
        }
}
