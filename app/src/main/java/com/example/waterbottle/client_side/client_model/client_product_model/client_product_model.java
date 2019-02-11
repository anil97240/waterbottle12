package com.example.waterbottle.client_side.client_model.client_product_model;

public class client_product_model {

        private String Product_Price,Product_detail,Product_name;
        String image;

    public String getProduct_price() {
        return Product_Price;
    }

    public void setProduct_price(String product_price) {
        Product_Price = product_price;
    }

    public String getProduct_detail() {
        return Product_detail;
    }

    public void setProduct_detail(String product_detail) {
        Product_detail = product_detail;
    }

    public String getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public client_product_model() {
    }

    public client_product_model(String product_price, String product_detail, String product_name, String image) {
        Product_Price = product_price;
        Product_detail = product_detail;
        Product_name = product_name;
        this.image = image;
    }
}
