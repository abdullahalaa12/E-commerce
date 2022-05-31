package com.example.e_commerce.customer;

import java.io.Serializable;
import java.util.Date;

public class Customer implements Serializable {
    /*db.execSQL("create table Customers (CustID integer primary key autoincrement, "+
                " CutName text not null , Email text not null,Password text not null, " +
                " Gender text not null, Birthdate text not null, Job text not null);");*/

    private final int ID;
    private String Name;
    private String Email;
    private String Password;
    private String Gender;
    private Date BOD;
    private String Job;


    public Customer(int ID, String name, String email, String password, String gender, Date BOD, String job) {
        this.ID = ID;
        Name = name;
        Email = email;
        Password = password;
        Gender = gender;
        this.BOD = BOD;
        Job = job;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getGender() {
        return Gender;
    }

    public Date getBOD() {
        return BOD;
    }

    public String getJob() {
        return Job;
    }

}
