package com.example.waterbottle.admin_agent_side.product_model;

public class Product {
    private int image, status;
    private String Product_name, Product_Price, Product_detail;

    public Product(int image, int status, String product_name, String product_Price, String product_detail) {
        this.image = image;
        this.status = status;
        Product_name = product_name;
        Product_Price = product_Price;
        Product_detail = product_detail;
    }

    public Product() {
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

    public String getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }

    public String getProduct_Price() {
        return Product_Price;
    }

    public void setProduct_Price(String product_Price) {
        Product_Price = product_Price;
    }

    public String getProduct_detail() {
        return Product_detail;
    }

    public void setProduct_detail(String product_detail) {
        Product_detail = product_detail;
    }
}
