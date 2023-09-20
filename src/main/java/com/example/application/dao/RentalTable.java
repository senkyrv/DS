package com.example.application.dao;

import com.example.application.model.Customer;
import com.example.application.model.Insurance;
import com.example.application.model.Rental;
import com.example.application.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class RentalTable {
    public final String SQL_SELECT = "SELECT * FROM RENTAL ORDER BY rental_start DESC";
    public final String SQL_SELECT_ID = "SELECT * FROM RENTAL WHERE rental_id=?";

    public final String SQL_INSERT = "INSERT INTO  RENTAL (rental_start, rental_end, user_id, customer_id, car_id) VALUES ( ?, ?, ?, ?, ?)";
    public final String SQL_DELETE_ID = "DELETE FROM RENTAL WHERE rental_id=?";


    private Database db = null;

    public RentalTable() {
        db = new Database();
    }

    public List<Rental> read(ResultSet rs) throws SQLException {
        List<Rental> rentals = new ArrayList<>();

        while (rs.next()) {
            Rental rental = new Rental();
            rental.setId(rs.getInt("rental_id"));
            rental.setRental_start(rs.getDate("rental_start"));
            rental.setRental_end(rs.getDate("rental_end"));
            rental.setUserID(rs.getInt("user_id"));
            rental.setCustomerID(rs.getInt("customer_id"));
            rental.setCarID(rs.getInt("car_id"));
            rentals.add(rental);
        }

        return rentals;
    }
    public List<Rental> select() throws SQLException {
        db.connect();
        Rental rental = null;
        PreparedStatement statement = db.createStatement(SQL_SELECT);
        ResultSet rs = db.select(statement);
        List <Rental> rentals = read(rs);
        db.close();
        return rentals;
    }
    public Rental findById(int id) throws SQLException {
        db.connect();
        Rental rental = null;
        PreparedStatement statement = db.createStatement(SQL_SELECT_ID);
        statement.setInt(1, id);
        ResultSet rs = db.select(statement);
        List<Rental> rentals = read(rs);
        if (rentals.size() > 0) {
            rental = rentals.get(0);
        }
        db.close();
        return rental;
    }
    public PreparedStatement prepareCommand(PreparedStatement statement, Rental rental) throws SQLException {
        statement.setDate(1, rental.getRental_start());
        statement.setDate(2, rental.getRental_end());
        statement.setInt(3, rental.getUserID());
        statement.setInt(4, rental.getCarID());
        statement.setInt(5, rental.getCarID());

        return statement;
    }

    /*public int insert(Rental rental) throws SQLException {
        db.connect();
        PreparedStatement statement = db.createStatement(SQL_INSERT);
        statement = prepareCommand(statement, rental);
        int res = db.createOrUpdateOrDelete(statement);
        db.close();
        return res;
    }*/
    public int insert(Rental rental) throws SQLException {
        db.connect();
        int res = 0;
        InsuranceTable i = new InsuranceTable();
        Date now = java.sql.Date.valueOf(java.time.LocalDate.now());
        try{
            db.beginTrasaction();
            System.out.println(rental.getRental_start());
            if (!now.before(rental.getRental_start())){
                throw new Exception("Nelze zvolit datum, které již proběhlo");
            }

            if (i.isValid(rental.getCarID()) || !i.isValid(rental.getCarID())){
                PreparedStatement statement = db.createStatement("INSERT INTO RENTAL (rental_start, rental_end, user_id, customer_id, car_id)\n" +
                        "VALUES (?, ?, ?, ?, ?)");
                statement.setDate(1, rental.getRental_start());
                statement.setDate(2, rental.getRental_end());
                statement.setInt(3, rental.getUserID());
                statement.setInt(4, rental.getCustomerID());
                statement.setInt(5, rental.getCarID());
                res = db.createOrUpdateOrDelete(statement);
                System.out.println("Vypujcka byla uspesne vytvorena");
            }
            else{
                throw new Exception("Auto nemá platné pojištění");
            }
            db.endTransaction();
        }catch(SQLException se){

            db.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return res;
    }

    public int extend(int id, Date extDate) throws SQLException {

        int res = 0;
        Date now = java.sql.Date.valueOf(java.time.LocalDate.now());
        List<Date> dates = getStartEnd(id);
        Date end = dates.get(1);
        System.out.println(end);
        System.out.println(extDate);
        db.connect();
        try{
            db.beginTrasaction();

            if (now.after(end)){
                throw new Exception("Výpůjčka již skončila");
            }
            if (extDate.before(end)){
                throw new Exception("Špatně zvolené datum");
            }
            PreparedStatement statement = db.createStatement("UPDATE Rental\n" +
                    "SET rental_end = ?\n" +
                    "WHERE rental_id = ?");
            statement.setDate(1, extDate);
            statement.setInt(2, id);
            res = db.createOrUpdateOrDelete(statement);
            db.endTransaction();
        }catch(SQLException se){

            db.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        System.out.println(res);
        return res;
    }

    public int delete(int id) throws SQLException {

        Date now = java.sql.Date.valueOf(java.time.LocalDate.now());
        List<Date> dates = getStartEnd(id);
        Date start = dates.get(0);
        Date end = dates.get(1);

        int res = 0;

        Rental rental = findById(id);
        start = rental.getRental_start();
        end = rental.getRental_end();
        db.connect();
        try{
            db.beginTrasaction();
            if ((start.before(now) && end.before(now)) || (start.after(now) && end.after(now))){

                PreparedStatement statement = db.createStatement("DELETE FROM PAYMENT WHERE rental_id =?");
                statement.setInt(1, id);
                res = db.createOrUpdateOrDelete(statement);

                statement = db.createStatement("DELETE FROM RENTAL WHERE rental_id =?");
                statement.setInt(1, id);
                res = db.createOrUpdateOrDelete(statement);
                System.out.println("Vypujcka byla smazana");
                db.endTransaction();
            }
            else{
                throw new Exception("Nelze odebrat výpůjčku, která právě probíhá");
            }
    }catch(SQLException se){

        db.rollback();
    } catch (Exception e) {
        e.printStackTrace();
    }
        db.close();

        return res;
    }

    public List<Date> getStartEnd(int id) throws SQLException {
        db.connect();
        PreparedStatement statement = db.createStatement("SELECT rental_start, rental_end FROM Rental WHERE rental_id=?");
        statement.setInt(1, id);
        ResultSet rs = db.select(statement);
        List<Date> dates = new ArrayList<>();
        Date start = null;
        Date end = null;
        while (rs.next()) {

            dates.add(rs.getDate("rental_start"));
            dates.add(rs.getDate("rental_end"));
        }

        db.close();
        return dates;
    }

}
