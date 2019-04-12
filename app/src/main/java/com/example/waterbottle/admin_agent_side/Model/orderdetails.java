package com.example.waterbottle.admin_agent_side.Model;

public class orderdetails {
    String Bottle_price,Botttle_qty,Customer_name,Order_id,Product_id,Product_name;

    public orderdetails() {
    }

    public orderdetails(String bottle_price, String botttle_qty, String customer_name, String order_id, String product_id, String product_name) {
        Bottle_price = bottle_price;
        Botttle_qty = botttle_qty;
        Customer_name = customer_name;
        Order_id = order_id;
        Product_id = product_id;
        Product_name = product_name;
    }

    public String getBottle_price() {
        return Bottle_price;
    }

    public void setBottle_price(String bottle_price) {
        Bottle_price = bottle_price;
    }

    public String getBotttle_qty() {
        return Botttle_qty;
    }

    public void setBotttle_qty(String botttle_qty) {
        Botttle_qty = botttle_qty;
    }

    public String getCustomer_name() {
        return Customer_name;
    }

    public void setCustomer_name(String customer_name) {
        Customer_name = customer_name;
    }

    public String getOrder_id() {
        return Order_id;
    }

    public void setOrder_id(String order_id) {
        Order_id = order_id;
    }

    public String getProduct_id() {
        return Product_id;
    }

    public void setProduct_id(String product_id) {
        Product_id = product_id;
    }

    public String getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }
}
