package io.github.whywhathow.mybatissourcelearn.other;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.sql.visitor.functions.If;
import io.github.whywhathow.mybatissourcelearn.entity.other.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: mybatis-source-learn
 * @description:
 * @author: WhyWhatHow
 * @create: 2020-10-26 15:12
 **/
interface UserMapper {
    @Select("select * from user where id =#{id}")
//    List<User> selectUser(int id);
    User selectUserById(int id);
}

public class MyMybatis {
    public static void main(String[] args) {
        MyMybatis sol = new MyMybatis();
        UserMapper usermapper =
                sol.getUsermapper();
        User user = usermapper.selectUserById(1);
        System.out.println(user);
    }

    /**
     * 手动实现mybatis select 查询方法 注解方式
     * 1. 通过反射获取 usermapper
     * 1. 获取SQL 命令, param 参数列表, 结果集类型
         * 1-1. 解析注解
         * 1-2 . 处理方法请求参数
         * 1-3. 封装SQL语句
         * 1-4. 获取方法的返回值, 并且处理 (未做)
     * 2. 与数据库进行交互, 获取结果集
     * 3. 处理结果集, 与返回值绑定, ()未做
     * 4. 返回给用户
     *
     * @return
     */
    UserMapper getUsermapper() {
        UserMapper mapper = (UserMapper) Proxy.newProxyInstance(MyMybatis.class.getClassLoader(),
                new Class<?>[]{UserMapper.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Select annotation = method.getAnnotation(Select.class);
                        if (annotation != null) {
                            String[] value = annotation.value();
                            String sql = value[0];
                            // 1, 处理方法参数
//                            method.invoke()
                            Map map = parseMethodArgs(args, method);
                            //2. 处理SQL语句 ,将参数 与SQL 语句绑定 即通过这个方法获取到一条完整的sql命令
                            sql = parseSQL(sql, map);
                            System.out.println(sql);
                            // 3. 获取方法的返回类型
                            Class<?> returnType = method.getReturnType();
                            //4 执行SQL方法, 处理结果集
                            return  runSQL(sql, returnType);
                        }
                        return null;
                    }


                });
        return mapper;

    }

    private Map<String, Object> parseMethodArgs(Object[] args, Method method) {
        Map<String, Object> map = new HashMap<String, Object>();
        Parameter[] parameters = method.getParameters();
        int[] cnt = {0};
        Arrays.asList(parameters).forEach(parameter -> {
            String name = parameter.getName();
            map.put(name, args[cnt[0]++]);
        });
        return map;
    }

    /**
     * 执行SQL 命令返回结果集
     * // TODO: 2021/3/15 未处理 方法的返回值
     *
     * @param sql        sql 命令
     * @param returnType 结果的返回类型
     * @return 结果集
     */
    private Object runSQL(String sql, Class<?> returnType) {
        DataSource dataSource = getDataSource();
        Object result = null;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.set
            ResultSet resultSet = preparedStatement.executeQuery();
            // 处理结果集, 对结果集 与xml方法 返回值做映射,
            result = resolveResultSet(resultSet, returnType);

//            preparedStatement
        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }

    /**
     * // TODO: 2021/3/15  暂时不知道如何处理结果集 , 先通过打印的方式进行处理
     * 处理结果集, 并将结果集与 method 返回值绑定
     *
     * @param resultSet
     * @param returnType
     * @return
     */
    private Object resolveResultSet(ResultSet resultSet, Class<?> returnType) throws SQLException, IllegalAccessException, InstantiationException {
        // returnType.getMethods();
        // 1. 获取 方法的返回值 对象实例化, // TODO: 2021/3/15  暂不处理
        User user = new User();
        //2. 获取结果集列表,并将参数绑定
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            System.out.println(id + "," + name);
            user.setId(id);
            user.setName(name);
            user.setUsername(username);
            user.setPassowrd(password);
        }// 3. 关闭结果集
        if (!resultSet.isClosed()) {
            resultSet.close();
        }
        return user;
    }


    private static DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("aa12321.");

        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true");
        return dataSource;
    }

    /**
     * 解析SQL语句
     * // TODO: 2021/3/15 存在问题, 应该把 #{}  更改为 ? 并且保存参数名列表,
     *
     * @param sql 初始sql语句  带#{}
     * @param map 参数map
     * @return sql
     */
    private String parseSQL(String sql, Map map) {
        StringBuilder sqlB = new StringBuilder();
        char[] chars = sql.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '#') {
                StringBuilder partB = new StringBuilder();
                i = parsePartSql(sql, partB, i);
                Object o = map.get(partB.toString());
                sqlB.append(o.toString());
            } else {
                sqlB.append(c);
            }

        }
        return sqlB.toString();
    }

    private int parsePartSql(String sql, StringBuilder partB, Integer i) {
        i++;
        for (; i < sql.length(); i++) {
            if (sql.charAt(i) != '{') {
                throw new RuntimeException("sql error without #{}in");
            } else {

                while (true) {
                    i++;
                    if (i == sql.length() || sql.charAt(i) == '}') {

                        return ++i;

                    } else {
                        partB.append(sql.charAt(i));
                    }
                }
            }


        }
        return ++i;
    }
}
