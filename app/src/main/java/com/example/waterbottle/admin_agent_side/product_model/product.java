package com.example.waterbottle.admin_agent_side.product_model;


public class product {
    int image,status;
    String name, price,details;

    public product(int image, int status, String name, String price, String details) {
        this.image = image;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
