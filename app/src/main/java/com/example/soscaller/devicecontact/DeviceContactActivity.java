package com.example.soscaller.devicecontact;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soscaller.ContactData;
import com.example.soscaller.R;
import com.example.soscaller.contact.ContactAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DeviceContactActivity extends AppCompatActivity {

    ArrayList<ContactData> selectUsers;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    DeviceContactAdapter adapter;
    Cursor phones;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffcdd0")));
        bar.setTitle("Select Contacts");
        bar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_device_contact);

        recyclerView = (RecyclerView) findViewById(R.id.device_recycler);
        recyclerView.setLayoutManager(layoutManager);

        //to load previous saved list
        loadData();

        //to check permission and to retrieve contacts from device is list == null form load data
        showContacts();


        buildRecyclerView();

        //Saving list changes
        saveData();
    }

    private void buildRecyclerView() {
        /*// initializing our adapter class.
        contactAdapter = new ContactAdapter(selectedContacts, this);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);

        // setting layout manager to our recycler view.
        recyclerView.setLayoutManager(manager);

        // setting adapter to our recycler view.
        recyclerView.setAdapter(contactAdapter);*/
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapter = new DeviceContactAdapter(inflater, selectUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Contact List", null);
        Type type = new TypeToken<ArrayList<ContactData>>() {}.getType();

        selectUsers = gson.fromJson(json, type);
        if (selectUsers == null) {
            selectUsers = new ArrayList<>();
        }
    }


    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(selectUsers);
        editor.putString("Contact List", json);
        editor.apply();
    }


    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overridden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            if(selectUsers.size() == 0){
                phones = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                LoadContact loadContact = new LoadContact();
                loadContact.execute();
            }else{
                buildRecyclerView();
            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we can't display the contacts", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone

            if (phones != null) {
                Log.e("count", "" + phones.getCount());
                if (phones.getCount() == 0) {

                }

                while (phones.moveToNext()) {
                    Bitmap bit_thumb = null;
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    ContactData selectUser = new ContactData();
                    selectUser.setName(name);
                    selectUser.setNumber(phoneNumber);
                    selectUser.setImageView(R.drawable.ic_baseline_check_circle_outline_24);
                    selectUser.setChecked(false);
                    selectUsers.add(selectUser);

                }
            } else {
                Log.e("Cursor close 1", "----------------");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //sortContacts();
            int count = selectUsers.size();
            ArrayList<ContactData> removed = new ArrayList<>();
            ArrayList<ContactData> contacts = new ArrayList<>();
            for (int i = 0; i < selectUsers.size(); i++) {
                ContactData inviteFriendsProjo = selectUsers.get(i);

                if (inviteFriendsProjo.getName().matches("\\d+(?:\\.\\d+)?") || inviteFriendsProjo.getName().trim().length() == 0) {
                    removed.add(inviteFriendsProjo);
                    Log.d("Removed Contact", new Gson().toJson(inviteFriendsProjo));
                } else {
                    contacts.add(inviteFriendsProjo);
                }
            }

            contacts.addAll(removed);
            selectUsers = contacts;
        }
    }

}