package com.example.application.model;

import java.sql.Date;

public class Insurance {
    private int id;
    private Date insurance_start;
    private Date insurance_end;
    private int carID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getInsurance_start() {
        return insurance_start;
    }

    public void setInsurance_start(Date insurance_start) {
        this.insurance_start = insurance_start;
    }

    public Date getInsurance_end() {
        return insurance_end;
    }

    public void setInsurance_end(Date insurance_end) {
        this.insurance_end = insurance_end;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }
}
