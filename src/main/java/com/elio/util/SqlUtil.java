package com.elio.util;

import com.elio.bean.Info;
import com.elio.bean.Offset;
import com.elio.bean.Persistence;
import com.elio.constant.Constants;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

/**
 * created by elio on 30/11/2022
 */
public class SqlUtil {
    public static <T> T getBean(Class<T> clazz) {
        Connection conn = null;
        try {
            String tableName = clazz.getSimpleName().toLowerCase();
            String sql = "select * from " + tableName + " where id = (select count(id) from " + tableName + ")";
            conn = getConnection();
            return getObjectFromResultSet(conn.createStatement().executeQuery(sql), clazz);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return null;
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(Constants.SQL_URL, Constants.SQL_USER, Constants.SQL_PWD);
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
            closeConnection(conn);
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String lowerCamel2Snake(String lowerCamelCase) {
        char[] resArray = new char[lowerCamelCase.length() * 2];
        int j = 0;
        for (int i = 0; i < lowerCamelCase.length(); i++, j++) {
            char c = lowerCamelCase.charAt(i);
            if (c > 64 && c < 91) {
                c += 32;
                resArray[j++] = '_';
            }
            resArray[j] = c;
        }
        return new String(resArray, 0, j);
    }

    public static String field2Getter(String fieldName) {
        char[] fChar = fieldName.toCharArray();
        fChar[0] -= 32;
        return "get" + new String(fChar);
    }

    public static String field2Setter(String fieldName) {
        char[] fChar = fieldName.toCharArray();
        fChar[0] -= 32;
        return "set" + new String(fChar);
    }

    public static void main(String[] args) {
        try {
            Info info = new Info(2, "jack5", "123", "3432423", "ldlkafs/asdfasdf", "sdfa/dfa", "dsf", null, null);
            editObject(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void editObject(Persistence obj) {
        if (getObjectById(obj.getId(), obj.getClass()) == null) {
            saveObject(obj);
        } else {
            updateObject(obj);
        }
    }

    public static void updateObject(Persistence obj) {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Connection conn = null;
        try {
            conn = getConnection();
            String sql = getUpdateSql(obj.getClass());
            PreparedStatement ps = conn.prepareStatement(sql);
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                String fieldName = f.getName();
                Method method = clazz.getDeclaredMethod(field2Getter(fieldName));
                if (method.getReturnType() == Date.class) {
                    ps.setDate(i, new java.sql.Date(System.currentTimeMillis()));
                } else {
                    if (fieldName.equals("id")) {
                        ps.setObject(fields.length, method.invoke(obj));
                    } else {
                        ps.setObject(i, method.invoke(obj));
                    }
                }
            }
            ps.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            rollback(conn);
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    public static void saveObject(Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Connection conn = null;
        try {
            conn = getConnection();
            String sql = getInsertSql(obj.getClass());
            PreparedStatement ps = conn.prepareStatement(sql);
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                String fieldName = f.getName();
                if (fieldName.equals("id")) continue;
                Method method = clazz.getDeclaredMethod(field2Getter(fieldName));
                if (method.getReturnType() == Date.class) {
                    ps.setDate(i, new java.sql.Date(System.currentTimeMillis()));
                } else {
                    ps.setObject(i, method.invoke(obj));
                }
            }
            ps.execute();
            conn.commit();
        } catch (Exception e) {
            rollback(conn);
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    public static <T> T getObjectById(Integer id, Class<T> clazz) {
        String tableName = clazz.getSimpleName().toLowerCase();
        String sql = "select * from " + tableName + " where id = ?";
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return getObjectFromResultSet(rs, clazz);
        } catch (Exception e) {
            return null;
        } finally {
            closeConnection(conn);
        }
    }

    private static <T> T getObjectFromResultSet(ResultSet resultSet, Class<T> clazz) throws InstantiationException, IllegalAccessException, SQLException, InvocationTargetException, NoSuchMethodException {

        if (resultSet.next()) {
            Object bean = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                String fieldName = f.getName();
                String columnName = lowerCamel2Snake(fieldName);
                char[] fChar = fieldName.toCharArray();
                fChar[0] -= 32;
                String methodName = "set" + new String(fChar);
                Method method = clazz.getDeclaredMethod(methodName, f.getType());
                Object arg = resultSet.getObject(columnName);
                if (arg instanceof LocalDateTime) { // 暂不取时间
                    continue;
                }
                method.invoke(bean, arg);
            }
            return (T) bean;
        }
        return null;
    }

    private static String getInsertSql(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        String tableName = clazz.getSimpleName().toLowerCase();
        String sql = null;
        StringBuilder res = new StringBuilder("insert into " + tableName + "(");
        StringBuilder vals = new StringBuilder("values(");
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            String fieldName = f.getName();
            if (fieldName.equals("id")) continue;
            if (i != fields.length - 1) {
                vals.append("?,");
                res.append(lowerCamel2Snake(fieldName)).append(",");
            } else {
                res.append(lowerCamel2Snake(fieldName)).append(") ");
                vals.append("?)");
            }
        }
        return res.toString() +  vals;
    }
    private static String getUpdateSql(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        String tableName = clazz.getSimpleName().toLowerCase();
        String sql = null;
        StringBuilder res = new StringBuilder("update " + tableName + " set ");
        for (Field f : fields) {
            String fieldName = f.getName();
            if (fieldName.equals("id")) continue;
            res.append(lowerCamel2Snake(fieldName)).append("=?,");
        }
        return res.toString().substring(0, res.toString().length() - 1) + " where id=?";
    }
}