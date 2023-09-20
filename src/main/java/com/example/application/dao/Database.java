package com.example.application.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    private static final String DATABASE_DRIVER = XXXXX;
    private static final String CONNECTION_STRING = XXXXX;
    private static final String USERNAME = XXXXX;
    private static final String PASSWORD = XXXX;

    private Connection connection;

    public Connection connect() {
        if (connection == null) {
            try {
                Class.forName(DATABASE_DRIVER);

                connection = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();

                connection = null;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void beginTrasaction() throws SQLException {
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    }

    public void endTransaction() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    public void rollback() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    public CallableStatement createCallableStatement(String sql) throws SQLException {
        return connection.prepareCall(sql);
    }

    public PreparedStatement createStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public ResultSet select(PreparedStatement statement) throws SQLException {

        ResultSet resultSet = statement.executeQuery();

        return resultSet;
    }

    public int createOrUpdateOrDelete(PreparedStatement statement) throws SQLException {

        int result = statement.executeUpdate();

        return result;
    }

    public boolean callProcedure(CallableStatement statement) throws SQLException {
        return statement.execute();
    }

}