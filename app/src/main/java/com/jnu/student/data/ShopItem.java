package com.jnu.student.data;

import java.io.Serializable;

public class ShopItem implements Serializable {
    //private final double price;
    private String name;
    private int imageId;

    private double price;
    public ShopItem(String name, int imageId, double price){
        this.name = name;
        this.price = price;
        this.imageId = imageId;
    }

   public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setPrice(Double price){this.price = price;}

    public void setName(String name){this.name = name;}
}
