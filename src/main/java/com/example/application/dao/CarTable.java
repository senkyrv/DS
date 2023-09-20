package com.example.application.dao;

import com.example.application.model.Car;
import com.example.application.model.Insurance;
import com.example.application.model.Rental;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarTable {
    public final String SQL_SELECT = "SELECT * FROM CAR";
    public final String SQL_SELECT_ID = "SELECT * FROM CAR WHERE car_id=?";

    public final String SQL_INSERT = "INSERT INTO  CAR (brand, model, spz, fuel_type, category_id) VALUES ( ?, ?, ?, ?, ?)";
    public final String SQL_DELETE_ID = "DELETE FROM CAR WHERE car_id=?";
    public final String SQL_UPDATE = "UPDATE CAR SET brand = ?, model = ?, spz = ?, fuel_type = ?, category_id = ? WHERE car_id= ?";

    private Database db = null;

    public CarTable() {
        db = new Database();
    }

    public List<Car> read(ResultSet rs) throws SQLException {
        List<Car> cars = new ArrayList<>();

        while (rs.next()) {
            Car car = new Car();
            car.setId(rs.getInt("car_id"));
            car.setBrand(rs.getString("brand"));
            car.setModel(rs.getString("model"));
            car.setSpz(rs.getString("spz"));
            car.setCategoryID(rs.getInt("category_id"));
            car.setFuel_type(rs.getString("fuel_type"));

            cars.add(car);
        }

        return cars;
    }
    public List<Car> select() throws SQLException {
        db.connect();
        Car car = null;
        PreparedStatement statement = db.createStatement(SQL_SELECT);
        ResultSet rs = db.select(statement);
        List <Car> cars = read(rs);
        db.close();
        return cars;
    }
    public Car findById(int id) throws SQLException {
        db.connect();
        Car car = null;
        PreparedStatement statement = db.createStatement(SQL_SELECT_ID);
        statement.setInt(1, id);
        ResultSet rs = db.select(statement);
        List<Car> cars = read(rs);
        if (cars.size() > 0) {
            car = cars.get(0);
        }
        db.close();
        return car;
    }
    public PreparedStatement prepareCommand(PreparedStatement statement, Car car) throws SQLException {
        statement.setString(1, car.getBrand());
        statement.setString(2, car.getModel());
        statement.setString(3, car.getSpz());
        statement.setString(4, car.getFuel_type());
        statement.setInt(5, car.getCategoryID());


        return statement;
    }

    public int insert(Car car) throws SQLException {
        db.connect();
        PreparedStatement statement = db.createStatement(SQL_INSERT);
        statement = prepareCommand(statement, car);
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
    public int update(Car car) throws SQLException {
        db.connect();
        PreparedStatement statement = db.createStatement(SQL_UPDATE);
        statement = prepareCommand(statement, car);
        statement.setInt(6, car.getId());
        int res = db.createOrUpdateOrDelete(statement);
        db.close();
        return res;
    }

    public List<Car> availableCars(int category_id) throws SQLException {
        Date now = java.sql.Date.valueOf(java.time.LocalDate.now());
        String SQL_AVAILABLE_CARS = "SELECT car_id, brand, model\n" +
                "FROM Car\n" +
                "WHERE Car.car_id NOT IN (SELECT car_id\n" +
                "FROM Rental\n" +
                "WHERE Car.car_id = Rental.car_id\n" +
                "AND CURRENT_TIMESTAMP BETWEEN rental_start AND rental_end)\n" +
                "AND Car.car_id IN(SELECT car_id\n" +
                "FROM Insurance\n" +
                "WHERE Car.car_id = Insurance.car_id AND insurance_end > CURRENT_TIMESTAMP)\n" +
                "AND category_id = ?";

        db.connect();
        PreparedStatement statement = db.createStatement(SQL_AVAILABLE_CARS);
        statement.setInt(1,category_id);
        ResultSet rs = db.select(statement);
        List<Car> cars = new ArrayList<>();

        while (rs.next()) {
            Car car = new Car();
            car.setId(rs.getInt("car_id"));
            car.setBrand(rs.getString("brand"));
            car.setModel(rs.getString("model"));
            cars.add(car);
        }
        db.close();
        int j;
        System.out.println("Aktualne dostupne vozy: ");
        for (j = 0; j < cars.size();j++){
            System.out.println(cars.get(j).getId() +" "+ cars.get(j).getBrand() + " "+ cars.get(j).getModel());
        }
        return cars;
    }
}
