package com.example.application.model;

import java.sql.Date;

public class Rental {
    private int id;
    private Date rental_start;
    private Date rental_end;
    private int userID;
    private int customerID;
    private int carID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getRental_start() {
        return rental_start;
    }

    public void setRental_start(Date rental_start) {
        this.rental_start = rental_start;
    }

    public Date getRental_end() {
        return rental_end;
    }

    public void setRental_end(Date rental_end) {
        this.rental_end = rental_end;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }
}
