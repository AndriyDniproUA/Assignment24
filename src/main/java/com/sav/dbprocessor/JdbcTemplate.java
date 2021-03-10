package com.sav.dbprocessor;

import lombok.RequiredArgsConstructor;
import org.w3c.dom.ls.LSOutput;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class JdbcTemplate {
    private final DataSource dataSource;

    //use for SELECT
    public <T> List<T> query(String sql, Object[] params, RowMapper<T> mapper) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            
            ResultSet rs = statement.executeQuery();

            List<T> result = new ArrayList<>();
            while (rs.next()) {
                T o = mapper.map(rs);
                result.add(o);
            }
            connection.close();
            return result;
        } catch (SQLException e) {
            return null;
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> mapper) {

        Object[] params = new Object[]{};
        return query(sql,params,mapper);
    }

    public<T> T queryOne(String sql, Object[] params, RowMapper<T> mapper){
        return query(sql,params,mapper).get(0);
    }

    public<T> T queryOne(String sql, RowMapper<T> mapper){
        Object[] params = new Object[]{};
        return query(sql,params,mapper).get(0);
    }

    //use for INSERT, UPDATE, DELETE
    public void update(String sql, Object[] params) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            statement.execute();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String sql) {
        Object[] params = new Object[]{};
        update(sql,params);
    }
}







