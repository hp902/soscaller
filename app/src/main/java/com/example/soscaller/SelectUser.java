package com.example.soscaller;

import java.io.Serializable;

public class SelectUser implements Serializable {
    String name;
    String phone;
    Boolean checkedBox = false;


    public SelectUser() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getCheckedBox() {
        return checkedBox;
    }

    public void setCheckedBox(Boolean checkedBox) {
        this.checkedBox = checkedBox;
    }


}