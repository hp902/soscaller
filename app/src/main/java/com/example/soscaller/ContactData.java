package com.example.soscaller;

import android.widget.ImageView;

import java.io.Serializable;

public class ContactData implements Serializable {

    private String name;
    private String number;
    boolean isChecked = false;
    private int imageView;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
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

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

}
