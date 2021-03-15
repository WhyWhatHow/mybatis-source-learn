package io.github.whywhathow.mybatissourcelearn.other;

import java.lang.ref.WeakReference;

/**
 * @program: mybatis-source-learn
 * @description:
 * @author: WhyWhatHow
 * @create: 2020-11-07 15:50
 **/
public class TestString {
    public static void main(String[] args) {
        String str = "a"+ "b";
        String str1 =new String("ab");
        String str2 =new String("a")+new String("b");
        String str3 ="ab";
        System.out.println(str==str3);
        System.out.println(str.intern()==str3);
        System.out.println(str1.intern()==str3);
        System.out.println(str ==str1);
        System.out.println(str1 ==str2);
        System.out.println(str==str2);

    }
    static void test(){
        Object o =new Object();
        WeakReference<Object> ref= new WeakReference<Object>(o);
        System.gc();
    }
}
