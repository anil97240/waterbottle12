package com.example.waterbottle.admin_agent_side.Model;

public class order {
    private String Bottle_type, Order_date, bottle_price;

    public order(String bottle_type, String order_date, String bottle_price) {
        Bottle_type = bottle_type;
        Order_date = order_date;
        this.bottle_price = bottle_price;
    }

    public order() {
    }

    public String getBottle_type() {
        return Bottle_type;
    }

    public void setBottle_type(String bottle_type) {
        Bottle_type = bottle_type;
    }

    public String getOrder_date() {
        return Order_date;
    }

    public void setOrder_date(String order_date) {
        Order_date = order_date;
    }

    public String getBottle_price() {
        return bottle_price;
    }

    public void setBottle_price(String bottle_price) {
        this.bottle_price = bottle_price;
    }
}
