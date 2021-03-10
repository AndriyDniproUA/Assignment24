package com.sav.dbprocessor;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String dsn = "jdbc:postgresql://localhost:5432/films2";
        String user = "postgres";
        String password = "1234";

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dsn);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(8);
        config.setMinimumIdle(4);

        DataSource dataSource = new HikariDataSource(config);

        JdbcTemplate jdbc = new JdbcTemplate(dataSource);

//        jdbc.update(
//                "INSERT INTO directors (name, surname, birth_date)" +
//                        " VALUES (?,?,?)",
//                new Object[]{"Stephen", "Spielberg", LocalDateTime.parse("1954-01-01T00:00")}
//        );

        List<Director> directors = jdbc.query("SELECT name, surname, birth_date FROM directors",
                (rs -> {
                    Director director = new Director();
                    director.setId(rs.getLong("id"));
                    director.setName(rs.getString("name"));
                    director.setSurname(rs.getString("surname"));
                    director.setBirth_date(rs.getObject("birth_date", LocalDateTime.class));
                    return director;
                })
        );

        directors = jdbc.query("SELECT name, surname, birth_date FROM directors WHERE name=?",
                new Object[]{"Stephen"},
                (rs -> {
                    Director director = new Director();
                    director.setId(rs.getLong("id"));
                    director.setName(rs.getString("name"));
                    director.setSurname(rs.getString("surname"));
                    director.setBirth_date(rs.getObject("birth_date", LocalDateTime.class));
                    //System.out.println(director);
                    return director;
                })
        );





        directors.stream()
                .forEach(d-> System.out.println(d));





        //public <T> List<T> query(String sql, Object[] params, RowMapper<T> mapper)
        //public <T> List<T> query(String sql, RowMapper<T> mapper)
        //public<T> T queryOne(String sql, Object[] params, RowMapper<T> mapper)
        //public<T> T queryOne(String sql, RowMapper<T> mapper)

        //public void update(String sql, Object[] params)
        //public void update(String sql)


    }
}
