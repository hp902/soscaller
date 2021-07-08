package com.example.soscaller.contact;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soscaller.R;
import com.example.soscaller.devicecontact.MainActivity2;
import com.example.soscaller.devicecontact.SelectUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    private static final String TAG = "ContactActivity";

    private List<SelectUser> selectedContacts;
    private RecyclerView recyclerView;

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

        loadData();

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_button);
        recyclerView = findViewById(R.id.recycler_view);

        floatingActionButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_READ_CONTACTS);
            } else {

                Intent intent = new Intent(this, MainActivity2.class);
                startActivity(intent);

            }
        });

        buildRecyclerView();
    }


    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Main2", null);
        Type type = new TypeToken<ArrayList<SelectUser>>() {}.getType();
        selectedContacts = gson.fromJson(json, type);
        if (selectedContacts == null) {
            selectedContacts = new ArrayList<>();
        }

        Log.i(TAG, "DATA LOADED");
    }

    private void buildRecyclerView() {
        ContactAdapter contactAdapter = new ContactAdapter(selectedContacts, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(contactAdapter);

        Log.i(TAG, "RECYCLER VIEW BUILT");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, MainActivity2.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, " Until You Grant the permission ,we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }


}