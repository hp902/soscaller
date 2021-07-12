package com.example.soscaller.devicecontact;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.soscaller.SelectUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DatabaseAdapter {

    ArrayList<SelectUser> getSelected = new ArrayList<>();

    Cursor getPhoneNumber;

    Context context;

    public DatabaseAdapter(Context context) {
        this.context = context;
    }

    public void setGetSelected(ArrayList<SelectUser> getSelected) {
        this.getSelected = getSelected;
    }

    public List<SelectUser> getData() {
        List<SelectUser> data = new ArrayList<>();

        getPhoneNumber = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (getPhoneNumber != null) {
            Log.e("count", "" + getPhoneNumber.getCount());
            if (getPhoneNumber.getCount() == 0) {
                Toast.makeText(context, "No contacts in your contact list.", Toast.LENGTH_LONG).show();
            }

            HashSet<String> already = new HashSet<>();

            while (getPhoneNumber.moveToNext()) {

                String id = getPhoneNumber.getString(getPhoneNumber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                if (already.add(id)) {
                    String name = getPhoneNumber.getString(getPhoneNumber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = getPhoneNumber.getString(getPhoneNumber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    SelectUser selectUser = new SelectUser();
                    selectUser.setName(name);
                    selectUser.setPhone(phoneNumber);

                    for(SelectUser selectUser1 : getSelected){
                        if(selectUser1.getPhone().equals(selectUser.getPhone())){
                            selectUser.setCheckedBox(true);
                            break;
                        }

                        selectUser.setCheckedBox(false);
                    }

                    data.add(selectUser);
                }

            }
        } else {
            Log.e("Cursor close 1", "----");
        }

        getPhoneNumber.close();

        return data;
    }
}