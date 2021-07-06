package com.example.soscaller.contact;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soscaller.ContactData;
import com.example.soscaller.R;
import com.example.soscaller.devicecontact.DeviceContactActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    private ContactAdapter contactAdapter;
    private ArrayList<ContactData> selectedContacts;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private static final int PERMISSION_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffcdd0")));
        bar.setTitle("Contacts");
        bar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_contact);

        floatingActionButton = findViewById(R.id.fab_button);
        recyclerView = findViewById(R.id.recycler_view);

        floatingActionButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_READ_CONTACTS);
            } else {

                Intent intent = new Intent(this, DeviceContactActivity.class);
                startActivity(intent);

            }
        });


        loadData();

        buildRecyclerView();


    }


    private void loadData() {
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("Contact List", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<ContactData>>() {
        }.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        selectedContacts = gson.fromJson(json, type);

        // checking below if the array list is empty or not
        if (selectedContacts == null) {
            // if the array list is empty
            // creating a new array list.
            selectedContacts = new ArrayList<>();
        }


    }


    private void buildRecyclerView() {
        // initializing our adapter class.
        contactAdapter = new ContactAdapter(selectedContacts, this);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);

        // setting layout manager to our recycler view.
        recyclerView.setLayoutManager(manager);

        // setting adapter to our recycler view.
        recyclerView.setAdapter(contactAdapter);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, DeviceContactActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, " Until You Grant the permission ,we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }


}