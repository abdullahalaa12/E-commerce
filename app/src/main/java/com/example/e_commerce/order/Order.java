package com.example.e_commerce.order;

import java.util.Date;

public class Order {
    private int id;
    Date date;
    private int custID;
    private String address;

    public Order(int id, Date date, int custID, String address) {
        this.id = id;
        this.date = date;
        this.custID = custID;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getCustID() {
        return custID;
    }

    public String getAddress() {
        return address;
    }
}
