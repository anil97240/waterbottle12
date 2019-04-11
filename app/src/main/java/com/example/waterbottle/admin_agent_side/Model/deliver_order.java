package com.example.waterbottle.admin_agent_side.Model;

import android.content.Context;

import java.util.List;

public class deliver_order {
    private String Amount_collected, Bottle_type, Botttles, Collected_Date, Padding_amount, QR_code, Amount_total, Agent_email, Payment_Method, Customer_name;


    public deliver_order() {
    }

    public deliver_order(String amount_collected, String bottle_type, String botttles, String collected_Date, String padding_amount, String QR_code, String amount_total, String agent_email, String payment_Method, String customer_name) {
        Amount_collected = amount_collected;
        Bottle_type = bottle_type;
        Botttles = botttles;
        Collected_Date = collected_Date;
        Padding_amount = padding_amount;
        this.QR_code = QR_code;
        Amount_total = amount_total;
        Agent_email = agent_email;
        Payment_Method = payment_Method;
        Customer_name = customer_name;
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

    public String getBotttles() {
        return Botttles;
    }

    public void setBotttles(String botttles) {
        Botttles = botttles;
    }

    public String getCollected_Date() {
        return Collected_Date;
    }

    public void setCollected_Date(String collected_Date) {
        Collected_Date = collected_Date;
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

    public String getAmount_total() {
        return Amount_total;
    }

    public void setAmount_total(String amount_total) {
        Amount_total = amount_total;
    }

    public String getAgent_email() {
        return Agent_email;
    }

    public void setAgent_email(String agent_email) {
        Agent_email = agent_email;
    }

    public String getPayment_Method() {
        return Payment_Method;
    }

    public void setPayment_Method(String payment_Method) {
        Payment_Method = payment_Method;
    }

    public String getCustomer_name() {
        return Customer_name;
    }

    public void setCustomer_name(String customer_name) {
        Customer_name = customer_name;
    }
}