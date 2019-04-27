package com.zgw.jdbc.orm.dao;

import com.zgw.jdbc.orm.domain.User;
import com.zgw.jdbc.orm.framework.BaseDaoSupport;
import com.zgw.jdbc.orm.framework.QueryRule;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

/**
 * 〈〉
 * @author gw.Zeng
 * @create 2019/4/27
 * @since 1.0.0
 */
@Repository
public class UserDao extends BaseDaoSupport<User,Integer> {
//        JdbcTemplate jdbcTemplate;

    @Override
    protected String getPKColumn() {
        return "id";

    }

    @Resource(name = "dataSource")
    public  void setDataSource(DataSource dataSource){

//        jdbcTemplate = new JdbcTemplate(dataSource);
        super.setDataSourceReadOnly(dataSource);
        super.setDataSourceWrite(dataSource);
        }

        public List<User> selectAll() throws Exception {
            QueryRule queryRule = QueryRule.getInstance();
            queryRule.andEqual("id","2");

            List select = super.select(queryRule);
            return select;
        }

}
