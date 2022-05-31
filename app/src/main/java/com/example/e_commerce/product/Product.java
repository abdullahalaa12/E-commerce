package com.example.e_commerce.product;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private int price;
    private int quantity;
    private byte[] image;
    private int categoryid;

    public Product(int id, String name, int price, int quantity, byte[] image, int categoryid) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.categoryid = categoryid;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public byte[] getImage() {
        return image;
    }

    public int getCategoryid() {
        return categoryid;
    }
}
