package com.example.application.dao;

import com.example.application.model.Category;
import com.example.application.model.Customer;
import com.example.application.model.Insurance;
import com.example.application.model.Payment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InsuranceTable {
    public final String SQL_SELECT = "SELECT * FROM INSURANCE";

    public final String SQL_INSERT = "INSERT INTO INSURANCE (insurance_start, insurance_end, car_id) VALUES (?, ?, ?)";
    public final String SQL_DELETE_ID = "DELETE FROM INSURANCE WHERE insurance_id=?";
    public final String SQL_UPDATE = "UPDATE INSURANCE SET insurance_start = ?, insurance_end = ?, car_id = ? WHERE insurance_id= ?";
    public final String SQL_VALID = "SELECT insurance_start, insurance_end FROM Insurance WHERE car_id = ?";

    private Database db = null;

    public InsuranceTable() {
        db = new Database();
    }

    public List<Insurance> read(ResultSet rs) throws SQLException {
        List<Insurance> insurances = new ArrayList<>();

        while (rs.next()) {
            Insurance insurance = new Insurance();
            insurance.setId(rs.getInt("insurance_id"));
            insurance.setInsurance_start(rs.getDate("insurance_start"));
            insurance.setInsurance_end(rs.getDate("insurance_end"));
            insurance.setCarID(rs.getInt("car_id"));
            insurances.add(insurance);
        }

        return insurances;
    }
    public List<Insurance> select() throws SQLException {
        db.connect();
        Insurance insurance = null;
        PreparedStatement statement = db.createStatement(SQL_SELECT);
        ResultSet rs = db.select(statement);
        List <Insurance> insurances = read(rs);
        db.close();
        return insurances;
    }

    public PreparedStatement prepareCommand(PreparedStatement statement, Insurance insurance) throws SQLException {
        statement.setDate(1, insurance.getInsurance_start());
        statement.setDate(2, insurance.getInsurance_end());
        statement.setInt(3, insurance.getCarID());


        return statement;
    }

    public int insert(Insurance insurance) throws SQLException {
        db.connect();
        PreparedStatement statement = db.createStatement(SQL_INSERT);
        statement = prepareCommand(statement, insurance);
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

    public int update(Insurance insurance) throws SQLException {
        db.connect();
        PreparedStatement statement = db.createStatement(SQL_UPDATE);
        statement = prepareCommand(statement, insurance);
        statement.setInt(4, insurance.getId());
        int res = db.createOrUpdateOrDelete(statement);
        db.close();
        return res;
    }
    public boolean isValid(int id) throws SQLException {
        db.connect();
        PreparedStatement statement = db.createStatement(SQL_VALID);
        statement.setInt(1, id);
        ResultSet rs = db.select(statement);
        Date start = null;
        Date end = null;
        while (rs.next()) {

            start = rs.getDate("insurance_start");
            end = rs.getDate("insurance_end");
        }
        Date now = java.sql.Date.valueOf(java.time.LocalDate.now());
        if (start.before(now) && end.after(now)){
            return true;
        }
        db.close();
        return false;
    }
}
