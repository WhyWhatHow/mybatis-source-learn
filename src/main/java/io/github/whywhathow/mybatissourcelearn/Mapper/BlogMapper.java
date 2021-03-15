package io.github.whywhathow.mybatissourcelearn.Mapper;

import io.github.whywhathow.mybatissourcelearn.entity.Blog;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

/**
 * @program: mybatis-source-learn
 * @description:
 * @author: WhyWhatHow
 * @create: 2020-10-26 20:53
 **/
public interface BlogMapper {
//    @Select("SELECT * FROM blog WHERE id = #{id}")
    Blog selectBlog(int id);

    @Select("select * from blog where name = #{name}")
    Blog selectBlogByName(String name);
    @Select("select * from blog where name = #{name} and  id = #{id}")
    Blog selectBlogByNameAndId(String name,Integer id );

    Blog findByName(String name);
    

}