package io.github.whywhathow.mybatissourcelearn.test;

import com.alibaba.druid.pool.DruidDataSource;
import io.github.whywhathow.mybatissourcelearn.Mapper.BlogMapper;
import io.github.whywhathow.mybatissourcelearn.entity.Blog;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;

/**
 * @program: mybatis-source-learn
 * @description:
 * @author: WhyWhatHow
 * @create: 2020-10-26 20:51
 **/
public class CreateSqlSession {

    public static void main(String[] args) {
//         1. 连接数据库,配置基本参数
        DataSource dataSource = getDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development",  transactionFactory, dataSource);
         Configuration configuration = new Configuration(environment);
//         2.  处理XXXMapper, 生成SqlSource(即处理xml 以及注解生成的SQL语句 与PreparedStatement绑定)
        configuration.addMapper(BlogMapper.class);
        // 3.  获取SqlSession
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        SqlSession sqlSession = sqlSessionFactory.openSession();
//         4. 获取mapper,执行方法
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        for (int i = 0; i < 10; i++) {
            Blog blog = mapper.selectBlog(1);
            System.out.println("i: "+blog);
        }
    }



    /**
     * @Author whywhathow
     * @Description: //TODO
     * @Date  2020/11/14
     * @Param []
     * @return javax.sql.DataSource
     **/

    private static DataSource getDataSource()  {
        DruidDataSource dataSource= new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("aa12321.");

//        dataSource.set` `
//       Driver driver =new Driver();
//       dataSource.setDriver( driver);
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true");
        return dataSource;

    }
}
