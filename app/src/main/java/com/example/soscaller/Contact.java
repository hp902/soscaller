package com.example.soscaller;

public class Contact {

    private String name;
    private String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;

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
