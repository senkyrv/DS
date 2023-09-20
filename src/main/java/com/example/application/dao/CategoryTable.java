package com.example.application.dao;

import com.example.application.model.Category;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryTable {
    public final String SQL_SELECT = "SELECT category_id, title FROM CATEGORY";

    private Database db = null;

    public CategoryTable() {
        db = new Database();
    }

    public List<Category> read(ResultSet rs) throws SQLException {
        List<Category> categories = new ArrayList<>();

        while (rs.next()) {
            int i = 1;
            Category category = new Category();
            category.setId(rs.getInt(1));
            category.setTitle(rs.getString(2));
            categories.add(category);
        }

        return categories;
    }
    public List<Category> select() throws SQLException {
        db.connect();
        Category category = null;
        PreparedStatement statement = db.createStatement(SQL_SELECT);
        ResultSet rs = db.select(statement);
        List <Category> categories = read(rs);
        db.close();
        return categories;
    }
}
