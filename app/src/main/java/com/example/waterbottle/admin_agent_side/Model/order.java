package com.example.waterbottle.admin_agent_side.Model;

public class order {
    private String Customer_id, Order_date, Botttle_qty, bottle_price, total_cost, Bottle_type, Customer_name;

    public order(String customer_id, String order_date, String botttle_qty, String bottle_price, String total_cost, String bottle_type, String customer_name) {
        Customer_id = customer_id;
        Order_date = order_date;
        Botttle_qty = botttle_qty;
        this.bottle_price = bottle_price;
        this.total_cost = total_cost;
        Bottle_type = bottle_type;
        Customer_name = customer_name;
    }

    public order() {
    }

    public String getCustomer_id() {
        return Customer_id;
    }

    public void setCustomer_id(String customer_id) {
        Customer_id = customer_id;
    }

    public String getOrder_date() {
        return Order_date;
    }

    public void setOrder_date(String order_date) {
        Order_date = order_date;
    }

    public String getBotttle_qty() {
        return Botttle_qty;
    }

    public void setBotttle_qty(String botttle_qty) {
        Botttle_qty = botttle_qty;
    }

    public String getBottle_price() {
        return bottle_price;
    }

    public void setBottle_price(String bottle_price) {
        this.bottle_price = bottle_price;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    public String getBottle_type() {
        return Bottle_type;
    }

    public void setBottle_type(String bottle_type) {
        Bottle_type = bottle_type;
    }

    public String getCustomer_name() {
        return Customer_name;
    }

    public void setCustomer_name(String customer_name) {
        Customer_name = customer_name;
    }
}
