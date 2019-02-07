package com.example.waterbottle.admin_agent_side;

public class sliding_image {
    String total;
    int IMAGE;

    public sliding_image() {
    }

    public sliding_image(String total) {
        this.total = total;
    }

    public sliding_image(int IMAGE) {
        this.IMAGE = IMAGE;
    }

    public sliding_image(String total, int IMAGE) {
        this.total = total;
        this.IMAGE = IMAGE;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(int IMAGE) {
        this.IMAGE = IMAGE;
    }
}
