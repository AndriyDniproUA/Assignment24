package com.sav.dbprocessor;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BeanPropertyRowMapper2<T> implements RowMapper<T> {
    private final Class<T> resultClass;

    @Override
    public T map(ResultSet rs) throws SQLException {
        try {
            T obj = resultClass.getConstructor().newInstance();
            List<String> columns = getColumns(rs);
            List<String> fields = Arrays.stream(
                    resultClass.getDeclaredFields())
                    .map(f->f.getName())
                    .collect(Collectors.toList());

            for (String column : columns) {
                String columnCamelCase = toCamelCase(column);

                if(fields.stream().noneMatch(f->f.equals(columnCamelCase))) {
                    continue;
                }
                Field f = resultClass.getDeclaredField(columnCamelCase);

                f.setAccessible(true);
                f.set(obj, rs.getObject(column, f.getType()));
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException("Class must contain constructor without parameters ", e);
        }
    }

    private String toCamelCase(String column) {
        Pattern pattern = Pattern.compile("\\_([a-z])");
        Matcher matcher = pattern.matcher(column);
        return matcher.replaceAll(res ->  res.group(1).toUpperCase());
    }

    private List<String> getColumns(ResultSet rs) throws SQLException {

        int colCount = rs.getMetaData().getColumnCount();
        List<String> columns = new ArrayList<>(colCount);

        for (int i = 1; i <= colCount; i++) {
            columns.add(rs.getMetaData().getColumnName(i));
        }
        return columns;
    }
}
