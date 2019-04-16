package com.example.waterbottle.admin_agent_side.Model;

public class order {
    private String Customer_id, Order_date, Order_id, Status, Botttle_qty, total_cost, Customer_name, Customer_qrcode;

    public order() {
    }

    public order(String customer_id, String order_date, String order_id, String status, String botttle_qty, String total_cost, String customer_name, String customer_qrcode) {
        Customer_id = customer_id;
        Order_date = order_date;
        Order_id = order_id;
        Status = status;
        Botttle_qty = botttle_qty;
        this.total_cost = total_cost;
        Customer_name = customer_name;
        Customer_qrcode = customer_qrcode;
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

    public String getOrder_id() {
        return Order_id;
    }

    public void setOrder_id(String order_id) {
        Order_id = order_id;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getBotttle_qty() {
        return Botttle_qty;
    }

    public void setBotttle_qty(String botttle_qty) {
        Botttle_qty = botttle_qty;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    public String getCustomer_name() {
        return Customer_name;
    }

    public void setCustomer_name(String customer_name) {
        Customer_name = customer_name;
    }

    public String getCustomer_qrcode() {
        return Customer_qrcode;
    }

    public void setCustomer_qrcode(String customer_qrcode) {
        Customer_qrcode = customer_qrcode;
    }
}