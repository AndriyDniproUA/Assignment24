package com.sav.dbprocessor;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class BeanPropertyRowMapper<T> implements RowMapper<T> {
    private final Class<T> resultClass;


    @Override
    public T map(ResultSet rs) throws SQLException {
        try {
            T obj = resultClass.getConstructor().newInstance();
            for (Field field : resultClass.getDeclaredFields()) {
                field.setAccessible(true);
                field.set(obj, extractValue(rs, field));
            }
            return obj;

        } catch (Exception e) {
            throw new RuntimeException("Class must contain constructor without parameters ", e);
        }
    }

    private Object extractValue(ResultSet rs, Field field) throws SQLException {
        String fieldName = field.getName();
        if (!containsColumn(rs, fieldName)) {
            fieldName = toSnakeCase(fieldName);
            if (!containsColumn(rs, fieldName)){
                return null;
            }
        }
        return rs.getObject(fieldName,field.getType());
    }

    private String toSnakeCase(String fieldName) {
        Pattern pattern=Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(fieldName);
        return matcher.replaceAll(res->"_"+res.group().toLowerCase());
    }

    private boolean containsColumn(ResultSet rs, String name) throws SQLException {
        int colCount = rs.getMetaData().getColumnCount();
        for (int i =1; i<=colCount; i++ ){
            if (rs.getMetaData().getColumnName(i).equals(name)) return true;
        }
        return false;
    }
}
