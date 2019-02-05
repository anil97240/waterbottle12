package com.example.waterbottle.client_side.client_model;

public class client {

    int image;
    String name, no,barcode,address;

    public client(int image, String name, String no, String barcode, String address) {
        this.image = image;
        this.name = name;
        this.no = no;
        this.barcode = barcode;
        this.address = address;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
