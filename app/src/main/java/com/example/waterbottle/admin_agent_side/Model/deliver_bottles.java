package com.example.waterbottle.admin_agent_side.Model;

public class deliver_bottles {
    String Agent_email,Bottle_type,Botttle_price,Botttles,Delivry_date,QR_code,Total_amount;

    public deliver_bottles() {

    }

    public deliver_bottles(String agent_email, String bottle_type, String botttle_price, String botttles, String delivry_date, String QR_code, String total_amount) {
        Agent_email = agent_email;
        Bottle_type = bottle_type;
        Botttle_price = botttle_price;
        Botttles = botttles;
        Delivry_date = delivry_date;
        this.QR_code = QR_code;
        Total_amount = total_amount;
    }

    public String getAgent_email() {
        return Agent_email;
    }

    public void setAgent_email(String agent_email) {
        Agent_email = agent_email;
    }

    public String getBottle_type() {
        return Bottle_type;
    }

    public void setBottle_type(String bottle_type) {
        Bottle_type = bottle_type;
    }

    public String getBotttle_price() {
        return Botttle_price;
    }

    public void setBotttle_price(String botttle_price) {
        Botttle_price = botttle_price;
    }

    public String getBotttles() {
        return Botttles;
    }

    public void setBotttles(String botttles) {
        Botttles = botttles;
    }

    public String getDelivry_date() {
        return Delivry_date;
    }

    public void setDelivry_date(String delivry_date) {
        Delivry_date = delivry_date;
    }

    public String getQR_code() {
        return QR_code;
    }

    public void setQR_code(String QR_code) {
        this.QR_code = QR_code;
    }

    public String getTotal_amount() {
        return Total_amount;
    }

    public void setTotal_amount(String total_amount) {
        Total_amount = total_amount;
    }
}
