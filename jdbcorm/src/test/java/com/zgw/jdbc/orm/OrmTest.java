package com.zgw.jdbc.orm;

import com.zgw.jdbc.orm.dao.UserDao;
import com.zgw.jdbc.orm.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

/**
 * 〈〉
 *
 * @author gw.Zeng
 * @create 2019/4/27
 * @since 1.0.0
 */
@ContextConfiguration(locations = {"classpath:application-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class OrmTest {
        //ORM (对象关系映射 Object Relation Mapping)
        //Hibernate/Spring JDBC /MyBatis /JPA  一对多，多对多，一对一
        //Hibernate 全自动 不需要写sql
        //MyBatis 半自动   支持简单映射   复杂 自己手写
        // spring JDBC  所有的sql 自己写 一套标准  模板模式

    //为什么？什么情况下 要手写ORM框架？
    //1,用 MyBatis ,可控性？  Hibernate高级玩家  ，可以用spring JDBC 再升及改造


    @Autowired
    private UserDao userDao;
    @Test
    public void testSelectAllFromUser(){
        try {
            List<User> users = userDao.selectAll();

            System.out.println(Arrays.toString(users.toArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
