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

        System.out.println("******************************CALL 1 ************************************************");
        List<Director> directors = jdbc.query("SELECT id, name, surname, birth_date FROM directors",
                new BeanPropertyRowMapper2<>(Director.class)
        );

        directors.stream()
                .forEach(d -> System.out.println(d));

        System.out.println("******************************CALL 2 ************************************************");
        directors = jdbc.query("SELECT id, name, surname, birth_date FROM directors WHERE name=?",
                new Object[]{"Stephen"},
                new BeanPropertyRowMapper<>(Director.class)
        );

        directors.stream()
                .forEach(d -> System.out.println(d));


        System.out.println("******************************CALL 3 ************************************************");
        Director directorOne = jdbc.queryOne("SELECT id, name, surname, birth_date FROM directors",
                new BeanPropertyRowMapper<>(Director.class)
        );
        System.out.println(directorOne);


        System.out.println("******************************CALL 4 ************************************************");
        directorOne = jdbc.queryOne("SELECT id, name, surname, birth_date FROM directors WHERE name=?",
                new Object[]{"Stephen"},
                new BeanPropertyRowMapper<>(Director.class)
        );
        System.out.println(directorOne);

        jdbc.update(
                "INSERT INTO directors (name, surname, birth_date) VALUES (?,?,?)",
                new Object[]{"Stephen", "Spielberg", LocalDateTime.parse("1954-01-01T00:00")}
        );

        jdbc.update(
                "DELETE FROM directors WHERE id=?",
                new Object[]{15}
        );

        directors = jdbc.query("SELECT id, name, surname, birth_date FROM directors",
                new BeanPropertyRowMapper<>(Director.class)
        );

        System.out.println("******************************************************************************");
        directors.stream()
                .forEach(d -> System.out.println(d));
    }
}

//        public <T> List<T> query(String sql, Object[] params, RowMapper<T> mapper)
//        public <T> List<T> query(String sql, RowMapper<T> mapper)
//        public<T> T queryOne(String sql, Object[] params, RowMapper<T> mapper)
//        public<T> T queryOne(String sql, RowMapper<T> mapper)
//        public void update(String sql, Object[] params)
//        public void update(String sql)