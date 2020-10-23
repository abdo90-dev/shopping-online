package com.example.myfirtapp.model;

public class ShipmentOrder {
    private String date,time,Totale_price,adrass,name,phone_number,image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ShipmentOrder() {
    }

    public ShipmentOrder(String date, String time, String totale_price, String adrass, String name, String phone_number) {
        this.date = date;
        this.time = time;
        Totale_price = totale_price;
        this.adrass = adrass;
        this.name = name;
        this.phone_number = phone_number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotale_price() {
        return Totale_price;
    }

    public void setTotale_price(String totale_price) {
        Totale_price = totale_price;
    }

    public String getAdrass() {
        return adrass;
    }

    public void setAdrass(String adrass) {
        this.adrass = adrass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
