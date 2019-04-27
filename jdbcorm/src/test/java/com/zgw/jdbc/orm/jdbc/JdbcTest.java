package com.zgw.jdbc.orm.jdbc;

import com.zgw.jdbc.orm.domain.Agent;
import com.zgw.jdbc.orm.domain.User;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * 〈原始链接mysql数据〉
 * orm框架的基本思路（不考虑代码规范）
 * @author gw.Zeng
 * @create 2019/4/27
 * @since 1.0.0
 */
public class JdbcTest<select> {

    public static void main(String[] args) {
        User user = new User();
//        List<?> select = select(user,"select id,name from c_user_tbl where id >= 20000099 and id<= 20000101");
//        List<?> select = select(new Agent(),"select id,user_id,agent_name from c_agent_tbl where id >= 3 and id<= 5");
        Agent agent = new Agent();
        agent.setId(3);

        user.setAge(3);
        List<?> select = select(user );
        System.out.println(Arrays.toString(select.toArray()));

    }

    public static List<?> select(Object condition){
        Class<?> entityClass = condition.getClass();

        List<Object> result = new ArrayList<>();
        Connection con =null;
        PreparedStatement preparedStatement =null;
        ResultSet rs =null;
        try{
            //1.加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            //2.建立链接
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jdbc?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&useSSL=false",
                    "root", "123456");

            //传入一个实体类，拿取字段
            Field[] fields = entityClass.getDeclaredFields();
            //取出属性数据库名
            Map<String,String> mapper = new HashMap<>();//字段别名与属性映射
            Map<String,String> getColumnNameByFileName = new HashMap<>();//用于条件查询
            for(Field field:fields){
                field.setAccessible(true);
                String name = field.getName();
                if (field.isAnnotationPresent(Column.class)){
                    Column co = field.getAnnotation(Column.class);
                    mapper.put(co.name(),name);
                    getColumnNameByFileName.put(name,co.name());
                }else {
                    mapper.put(name,name);//默认字段
                    getColumnNameByFileName.put(name,name);
                }

            }

            StringBuffer where = new StringBuffer(" where 1=1 ");
            for(Field field:fields){
                Object value = field.get(condition);
                if (null != value){
                    if (String.class == field.getType()){
                        where.append(" and "+getColumnNameByFileName.get(field.getName())+"= '"+value +"'");
                    }else {
                        where.append(" and "+getColumnNameByFileName.get(field.getName())+"= "+value );
                    }
                    //其他类型......
                }
            }
            //获取表名
            Table t = entityClass.getAnnotation(Table.class);
            String tableName = t.name();
            String sql = "select * from  " + tableName +where.toString();
            System.out.println(sql);
            //3.创建语句集
            preparedStatement = con.prepareStatement(sql);
            //4.执行语句集
            rs = preparedStatement.executeQuery();
            //5.获取结果集
            //结果集的元数据  获取列数
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()){
//                实体类  属性名，对应数据库表名字段
//                 通过反射机制拿到实体类的字段
                Object instance = entityClass.newInstance();
                for (int i = 1; i <=columnCount ; i++) {
                    //获取游标下的列名
                    String columnName = rs.getMetaData().getColumnName(i);
                    //实体类 获取属性
                    Field field = entityClass.getDeclaredField(mapper.get(columnName));
                    field.setAccessible(true);
                    //给对象赋值
                    field.set(instance,rs.getObject(columnName));

                }
//                User user=new User();
//                user.setId(rs.getInt("id"));
//                user.setName(rs.getString("name"));
                //上面对象怎么封装？可复用
//                User user = packageUser(rs, rs.getRow());
                result.add(instance);



            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //6.关闭结果集、关闭链接
            try {
                con.close();
                preparedStatement.close();
                rs.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;

    }

    public  static User packageUser(ResultSet rs, int index) throws SQLException {
        User user=new User();
        Object object = rs.getObject(index);
        user.setId((Integer)object);
//        user.setId(rs.getInt("id"));
//                user.setName(rs.getString("name"));
        return user;
    }


}
