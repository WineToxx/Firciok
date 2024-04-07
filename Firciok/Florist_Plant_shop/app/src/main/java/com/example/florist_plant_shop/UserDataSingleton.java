package com.example.florist_plant_shop;

public class UserDataSingleton {
    private static UserDataSingleton instance;
    String username;
    String email;

    String number;
    String name;
    String password;

    String CardName;
    String CardNumber;
    String ExpiryDate;
    String cvv;
    String address;
    String emirate;

    public UserDataSingleton() {
    }

    public UserDataSingleton(String username, String email, String number, String name, String password, String cardName, String cardNumber, String expiryDate, String cvv, String address, String emirate) {
        this.username = username;
        this.email = email;
        this.number = number;
        this.name = name;
        this.password = password;
        this.CardName = cardName;
        this.CardNumber = cardNumber;
        this.ExpiryDate = expiryDate;
        this.cvv = cvv;
        this.address=address;
        this.emirate= emirate;
    }

    public String getEmirate() {
        return emirate;
    }

    public void setEmirate(String emirate) {
        this.emirate = emirate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }

    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static UserDataSingleton getInstance() {
        if (instance == null) {
            instance = new UserDataSingleton();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
