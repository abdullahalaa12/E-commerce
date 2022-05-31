package com.example.e_commerce.category;

import java.io.Serializable;
import java.util.Date;

public class Category implements Serializable{
    private final int ID;
    private String Name;

    public Category(int ID, String name) {
        this.ID = ID;
        Name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }
}
