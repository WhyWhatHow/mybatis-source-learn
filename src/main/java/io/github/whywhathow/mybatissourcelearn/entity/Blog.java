package io.github.whywhathow.mybatissourcelearn.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: mybatis-source-learn
 * @description:
 * @author: WhyWhatHow
 * @create: 2020-10-26 20:54
 **/
@Data


public class Blog {
    String id;
    final int price;
    String name;
}
