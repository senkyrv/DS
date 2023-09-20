package com.example.application.dao;

import com.example.application.model.Car;
import com.example.application.model.Customer;
import com.example.application.model.Rental;
import com.example.application.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerTable {
    public final String SQL_SELECT = "SELECT * FROM CUSTOMER";
    public final String SQL_SELECT_ID = "SELECT * FROM CUSTOMER WHERE customer_id=?";

    public final String SQL_INSERT = "INSERT INTO CUSTOMER (first_name, last_name, age, email, phone) VALUES ( ?, ?, ?, ?, ?)";
    public final String SQL_DELETE_ID = "DELETE FROM CUSTOMER WHERE customer_id=?";
    public final String SQL_UPDATE = "UPDATE CUSTOMER SET first_name = ?, last_name = ?, age = ?, email = ?, phone = ? WHERE customer_id= ?";


    private Database db = null;
    public CustomerTable() {
        db = new Database();
    }

    public List<Customer> select() throws SQLException {
        db.connect();
        Customer customer = null;
        PreparedStatement statement = db.createStatement(SQL_SELECT);
        ResultSet rs = db.select(statement);
        List <Customer> customers = read(rs);
        db.close();
        return customers;
    }

    public Customer findById(int id) throws SQLException {
        db.connect();
        Customer customer = null;
        PreparedStatement statement = db.createStatement(SQL_SELECT_ID);
        statement.setInt(1, id);
        ResultSet rs = db.select(statement);
        List<Customer> customers = read(rs);
        if (customers.size() > 0) {
            customer = customers.get(0);
        }
        db.close();
        return customer;
    }
    public List<Customer> read(ResultSet rs) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        while (rs.next()) {
            int i = 1;
            Customer customer = new Customer();
            customer.setId(rs.getInt(i++));
            customer.setFirst_name(rs.getString(i++));
            customer.setLast_name(rs.getString(i++));
            customer.setAge(rs.getInt(i++));
            customer.setEmail(rs.getString(i++));
            customer.setPhone(rs.getString(i++));
            customers.add(customer);
        }

        return customers;
    }

    public PreparedStatement prepareCommand(PreparedStatement statement, Customer customer) throws SQLException {
        statement.setString(1, customer.getFirst_name());
        statement.setString(2, customer.getLast_name());
        statement.setInt(3, customer.getAge());
        statement.setString(4, customer.getEmail());
        statement.setString(5, customer.getPhone());


        return statement;
    }

    public int insert(Customer customer) throws SQLException {
        db.connect();
        PreparedStatement statement = db.createStatement(SQL_INSERT);
        statement = prepareCommand(statement, customer);
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

    public int update(Customer customer) throws SQLException {
        db.connect();
        PreparedStatement statement = db.createStatement(SQL_UPDATE);
        statement = prepareCommand(statement, customer);
        statement.setInt(6, customer.getId());
        int res = db.createOrUpdateOrDelete(statement);
        db.close();
        return res;
    }

    public List<Rental> unpaidRentals(int customer_id) throws SQLException {
        List<Rental> rentals = new ArrayList<>();
        Date now = java.sql.Date.valueOf(java.time.LocalDate.now());
        String SQL_AVAILABLE_CARS = "SELECT RENTAL_ID\n" +
                "FROM Rental R\n" +
                "JOIN Customer C on C.customer_id = R.customer_id\n" +
                "JOIN Car C2 on C2.car_id = R.car_id\n" +
                "WHERE RENTAL_ID NOT IN\n" +
                "(SELECT rental_id FROM Payment where R.rental_id = payment.rental_id)\n" +
                "AND R.rental_end < CURRENT_TIMESTAMP\n" +
                "AND C.customer_id = ?";

        db.connect();
        PreparedStatement statement = db.createStatement(SQL_AVAILABLE_CARS);
        statement.setInt(1, customer_id);
        ResultSet rs = db.select(statement);

        while (rs.next()) {
            RentalTable rt = new RentalTable();
            Rental rental = rt.findById(rs.getInt("rental_id"));
            rentals.add(rental);
        }
        db.close();

        int j;
        System.out.println("Nezaplacene vypujcky zakaznika: ");
        CustomerTable custs = new CustomerTable();
        CarTable cars = new CarTable();

        for (j = 0; j < rentals.size();j++){
            int rental_id = rentals.get(j).getId();
            System.out.println(custs.findById(rentals.get(j).getCustomerID()).getFirst_name() + " " + custs.findById(rentals.get(j).getCustomerID()).getLast_name()
                    + " " + cars.findById(rentals.get(j).getCarID()).getBrand() + " "+ cars.findById(rentals.get(j).getCarID()).getModel()
            + " " + rentals.get(j).getRental_start() + " " + rentals.get(j).getRental_end());
        }
        return rentals;
    }
}
