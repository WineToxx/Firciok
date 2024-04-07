package com.example.florist_plant_shop;

import java.io.Serializable;

public class MyCartModel implements Serializable {
    String ProductImg;
    String ProductName;
    String ProductPrice;
    String currentDate;
    int totalPrice;
    String totalQuantity;
    String documentId;

    public MyCartModel() {
    }

    public MyCartModel(String productImg, String productName, String productPrice, String currentDate, int totalPrice, String totalQuantity, String documentId) {
        this.ProductImg = productImg;
        this.ProductName = productName;
        this.ProductPrice = productPrice;
        this.currentDate = currentDate;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.documentId=documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getProductImg() {
        return ProductImg;
    }

    public void setProductImg(String productImg) {
        ProductImg = productImg;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
