package com.example.application.dao;

import com.example.application.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserTable {
    public final String SQL_SELECT = "SELECT * FROM \"USER\"";
    public final String SQL_SELECT_ID = "SELECT * FROM \"USER\" WHERE user_id=?";

    public final String SQL_INSERT = "INSERT INTO \"USER\" (first_name, last_name, role, email, phone) VALUES ( ?, ?, ?, ?, ?)";
    public final String SQL_DELETE_ID = "DELETE FROM \"USER\" WHERE user_id=?";


    private Database db = null;
    public UserTable() {
        db = new Database();
    }

    public List<User> select() throws SQLException {
        db.connect();
        User user = null;
        PreparedStatement statement = db.createStatement(SQL_SELECT);
        ResultSet rs = db.select(statement);
        List <User> users = read(rs);
        db.close();
        return users;
    }

    public User findById(int id) throws SQLException {
        db.connect();
        User user = null;
        PreparedStatement statement = db.createStatement(SQL_SELECT_ID);
        statement.setInt(1, id);
        ResultSet rs = db.select(statement);
        List<User> users = read(rs);
        if (users.size() > 0) {
            user = users.get(0);
        }
        db.close();
        return user;
    }
    public List<User> read(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            int i = 1;
            User user = new User();
            user.setId(rs.getInt(i++));
            user.setFirst_name(rs.getString(i++));
            user.setLast_name(rs.getString(i++));
            user.setRole(rs.getString(i++));
            user.setEmail(rs.getString(i++));
            user.setPhone(rs.getString(i++));
            users.add(user);
        }

        return users;
    }
    public PreparedStatement prepareCommand(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getFirst_name());
        statement.setString(2, user.getLast_name());
        statement.setString(3, user.getRole());
        statement.setString(4, user.getEmail());
        statement.setString(5, user.getPhone());

        return statement;
    }

    public int insert(User user) throws SQLException {
        db.connect();
        PreparedStatement statement = db.createStatement(SQL_INSERT);
        statement = prepareCommand(statement, user);
        int res = db.createOrUpdateOrDelete(statement);
        db.close();
        return res;
    }
    public int delete(int id) throws SQLException {
        db.connect();
        PreparedStatement statement = db.createStatement(SQL_DELETE_ID);
        statement.setInt(1, id);
        int res = db.createOrUpdateOrDelete(statement);
        db.close();
        return res;
    }
}
