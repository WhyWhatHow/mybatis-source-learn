package io.github.whywhathow.mybatissourcelearn.proxy;

import io.github.whywhathow.mybatissourcelearn.entity.Blog;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.Reflector;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @program: mybatis-source-learn
 * @description: java.lang.reflect 测试 clas 构造器生成.
 * @author: WhyWhatHow
 * @create: 2021-03-24 11:16
 **/
public class Test {
    void testFinalWithUnsafe() throws Exception {
        System.out.println(" get unsafe");
        Constructor<Unsafe> declaredConstructor = Unsafe.class.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        Unsafe unsafe = declaredConstructor.newInstance();
        System.out.println( "get Blog ");
        Blog blog = (Blog)unsafe.allocateInstance(Blog.class);

        System.out.println(blog);
    }
    public static void main(String[] args) throws Exception {
        Test test =new Test();
        test.testFinalWithUnsafe();
//        testConstructor(test);
    }

    private static void testConstructor(Test test) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        System.out.println("==========Blog.instance========");
        Blog instance = test.instance(Blog.class);
        System.out.println(instance);
        System.out.println("========================");
        Blog blog = test.instantiateClass(Blog.class, null, null);
        System.out.println( blog);
        System.out.println("-===============");
        Blog instant = test.instant(Blog.class);// 没有无参构造器, 返回值为null
        System.out.println(instant);
    }

    <T>T instance(Class<T> type){
        try {
            return type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null ;
    }
    // 无参构造
    private  <T> T instant(Class<T> type) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<T> declaredConstructor = null;
        try {
//            declaredConstructor = type.getDeclaredConstructor(type);
            declaredConstructor = type.getDeclaredConstructor();
            return declaredConstructor.newInstance();
//            Constructor<T> constructor = type.getConstructor();
//            T t = declaredConstructor.newInstance();
//            return constructor.newInstance();

        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
            System.out.println(" 没有无参构造方法");
            declaredConstructor.setAccessible(true);
            T t = declaredConstructor.newInstance(type);
            return t;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;

    }

    //类实例化 1. 获取constructor ,2 . new instance
    private  <T> T instantiateClass(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        try {
            Constructor<T> constructor;//1. 无参构造
            if (constructorArgTypes == null || constructorArgs == null) {
                constructor = type.getDeclaredConstructor();
                try {
                    return constructor.newInstance();// 1.1 尝试调用默认的构造方法
                } catch (IllegalAccessException e) {
                    if (Reflector.canControlMemberAccessible()) {
                        constructor.setAccessible(true); // 1.2 访问权限被拒 ,然后强制跳过访问权限生成实例, // TODO: 2021/3/13 应该抛异常, 不应该这么 处理
                        return constructor.newInstance();
                    } else {
                        throw e;
                    }
                }
            }//2. 处理有参构造
            constructor = type.getDeclaredConstructor(constructorArgTypes.toArray(new Class[0]));
            try {
                return constructor.newInstance(constructorArgs.toArray(new Object[0]));
            } catch (IllegalAccessException e) {
                if (Reflector.canControlMemberAccessible()) {
                    constructor.setAccessible(true);
                    return constructor.newInstance(constructorArgs.toArray(new Object[0]));
                } else {
                    throw e;
                }
            }
        } catch (Exception e) {
            String argTypes = Optional.ofNullable(constructorArgTypes).orElseGet(Collections::emptyList)
                    .stream().map(Class::getSimpleName).collect(Collectors.joining(","));
            String argValues = Optional.ofNullable(constructorArgs).orElseGet(Collections::emptyList)
                    .stream().map(String::valueOf).collect(Collectors.joining(","));
            throw new ReflectionException("Error instantiating " + type + " with invalid types (" + argTypes + ") or values (" + argValues + "). Cause: " + e, e);
        }
    }
}
