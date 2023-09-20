package com.example.application.dao;

import com.example.application.model.Category;
import com.example.application.model.Payment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentTable {
    public final String SQL_SELECT = "SELECT payment_id, amount FROM PAYMENT";


    private Database db = null;

    public PaymentTable() {
        db = new Database();
    }

    public List<Payment> read(ResultSet rs) throws SQLException {
        List<Payment> payments = new ArrayList<>();

        while (rs.next()) {
            Payment payment = new Payment();
            payment.setId(rs.getInt(1));
            payment.setAmount(rs.getFloat(2));
            payments.add(payment);
        }

        return payments;
    }
    public List<Payment> select() throws SQLException {
        db.connect();
        Category category = null;
        PreparedStatement statement = db.createStatement(SQL_SELECT);
        ResultSet rs = db.select(statement);
        List <Payment> payments = read(rs);
        db.close();
        return payments;
    }
}
