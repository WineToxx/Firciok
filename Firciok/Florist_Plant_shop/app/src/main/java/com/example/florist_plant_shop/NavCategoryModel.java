package com.example.florist_plant_shop;

public class NavCategoryModel {
    String name;
    String type;

    public NavCategoryModel() {
    }

    public NavCategoryModel(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
