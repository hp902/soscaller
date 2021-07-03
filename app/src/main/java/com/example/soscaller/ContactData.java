package com.example.soscaller;

public class ContactData {

    private String name;
    private String number;

    public ContactData(String name, String number) {
        this.name = name;
        this.number = number;

    }

    public ContactData() {

    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
