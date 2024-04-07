package com.example.florist_plant_shop;

import java.io.Serializable;

public class ViewAllModel implements Serializable {

    String name;
    String description;
    String rating;
    int price;
    String img_url;
    String maintenance;
    String rate_img;
    String type;
    String color;

    public ViewAllModel() {
    }

    public ViewAllModel(String name, String description, String rating, int price, String img_url, String maintenance, String rate_img, String type, String color) {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.img_url = img_url;
        this.maintenance = maintenance;
        this.rate_img = rate_img;
        this.type = type;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(String maintenance) {
        this.maintenance = maintenance;
    }

    public String getRate_img() {
        return rate_img;
    }

    public void setRate_img(String rate_img) {
        this.rate_img = rate_img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
