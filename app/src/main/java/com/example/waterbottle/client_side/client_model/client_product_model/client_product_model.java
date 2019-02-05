package com.example.waterbottle.client_side.client_model.client_product_model;

public class client_product_model {
    int image;
    String name, price,details;

    public client_product_model(int image, String name, String price, String details) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.details = details;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
