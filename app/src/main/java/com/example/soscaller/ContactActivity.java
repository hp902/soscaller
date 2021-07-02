package com.example.soscaller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    private MyAdapter listAdapter;
    private ArrayList<Contact> contactsList = new ArrayList<>();
    private RecyclerView recycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffcdd0")));
        bar.setTitle("Contacts");
        bar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_contact);

        recycler = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        listAdapter = new MyAdapter(contactsList, this);
        recycler.setAdapter(listAdapter);


        contactsList.add(new Contact("Daniel Shiffman", "778899009"));
        contactsList.add(new Contact("Jhon Doe", "778899009"));
        contactsList.add(new Contact("Jane Doe", "778899009"));

        listAdapter.notifyDataSetChanged();


    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}