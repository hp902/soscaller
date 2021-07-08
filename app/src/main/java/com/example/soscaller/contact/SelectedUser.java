package com.example.soscaller.contact;

import android.os.Parcel;
import android.os.Parcelable;

public class SelectedUser implements Parcelable {
    String name;
    String phone;


    public SelectedUser(Parcel in) {
        name = in.readString();
        phone = in.readString();
    }

    public static final Creator<SelectedUser> CREATOR = new Creator<SelectedUser>() {
        @Override
        public SelectedUser createFromParcel(Parcel in) {
            return new SelectedUser(in);
        }

        @Override
        public SelectedUser[] newArray(int size) {
            return new SelectedUser[size];
        }
    };

    public SelectedUser() {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
    }
}