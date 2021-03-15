package io.github.whywhathow.mybatissourcelearn.other;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @program: mybatis-source-learn
 * @description:
 * @author: WhyWhatHow
 * @create: 2020-10-26 23:06
 **/
interface Interaptor {
    Object plugin(Object target, InteraptorChain chain);
}

class A implements Interaptor {
    @Override
    public Object plugin(Object target, InteraptorChain chain) {
        System.out.println("--a--");
//        return target;
        return chain.plugin(target);
    }
}

class B implements Interaptor {
    @Override
    public Object plugin(Object target, InteraptorChain chain) {
        System.out.println("--b--");
        return chain.plugin(target );
//        return target;
    }

}

class C implements Interaptor {
    @Override
    public Object plugin(Object target, InteraptorChain chain) {
        System.out.println("--c--");
        return chain.plugin(target );
//        return target;
    }

}

class InteraptorChain {
    List<Interaptor> list = new ArrayList<Interaptor>();
    Iterator<Interaptor> iterator;
    public Object plugin(Object target) {
        if (iterator==null) {
            iterator= list.iterator();
        }
        if (iterator.hasNext()) {
            Interaptor next = iterator.next();
            next.plugin(target, this);
        }

        return  target;
    }

    public void addInteraptor(Interaptor interaptor) {
        this.list.add(interaptor);
    }

//    public void pluginAll() {
////        Iterator<Interaptor> iterator = list.iterator();
////        if (iterator.hasNext()) {
////
////            iterator.next().plugin();
////        }
//        list.forEach(a->a.plugin(a));
//    }
}

public class InteraptorDemo {
    public static void main(String[] args) {
        InteraptorChain chain = new InteraptorChain();
        A a = new A();
        B b = new B();
        C c = new C();
        chain.addInteraptor(a);
        chain.addInteraptor(b);
        chain.addInteraptor(c);
        chain.plugin(new Object());
//        chain.pluginAll();
    }
}
