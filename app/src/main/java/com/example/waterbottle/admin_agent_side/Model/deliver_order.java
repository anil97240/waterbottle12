package com.example.waterbottle.admin_agent_side.Model;

import android.content.Context;

import java.util.List;

public class deliver_order {
  private   String Amount_collected,Bottle_type,Botttle_total,Delivry_date,Padding_amount,QR_code,Total_amount;

    public deliver_order() {
    }

    public deliver_order(String amount_collected, String bottle_type, String botttle_total, String delivry_date, String padding_amount, String QR_code, String total_amount) {
        Amount_collected = amount_collected;
        Bottle_type = bottle_type;
        Botttle_total = botttle_total;
        Delivry_date = delivry_date;
        Padding_amount = padding_amount;
        this.QR_code = QR_code;
        Total_amount = total_amount;
    }

    public String getAmount_collected() {
        return Amount_collected;
    }

    public void setAmount_collected(String amount_collected) {
        Amount_collected = amount_collected;
    }

    public String getBottle_type() {
        return Bottle_type;
    }

    public void setBottle_type(String bottle_type) {
        Bottle_type = bottle_type;
    }

    public String getBotttle_total() {
        return Botttle_total;
    }

    public void setBotttle_total(String botttle_total) {
        Botttle_total = botttle_total;
    }

    public String getDelivry_date() {
        return Delivry_date;
    }

    public void setDelivry_date(String delivry_date) {
        Delivry_date = delivry_date;
    }

    public String getPadding_amount() {
        return Padding_amount;
    }

    public void setPadding_amount(String padding_amount) {
        Padding_amount = padding_amount;
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
