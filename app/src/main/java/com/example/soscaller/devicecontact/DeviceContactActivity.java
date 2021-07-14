package com.example.soscaller.devicecontact;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soscaller.R;
import com.example.soscaller.SelectUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DeviceContactActivity extends AppCompatActivity {


    ArrayList<SelectUser> selectedUsers;

    DatabaseAdapter mydb;
    DeviceContactAdapter suAdapter;
    List<SelectUser> selectUsersList = new ArrayList<>();

    RecyclerView recyclerView;
    SearchView search;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main2);

        loadData();


        mydb = new DatabaseAdapter(getApplicationContext());

        mydb.setGetSelected(selectedUsers);

        recyclerView = findViewById(R.id.contacts_list);
        setRecyclerview();

        selectedUsers = (ArrayList<SelectUser>) selectedUsers.stream().distinct().collect(Collectors.toList());

        search = findViewById(R.id.searchView);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                suAdapter.filter(searchText);
                return false;
            }
        });


    }


    private void setRecyclerview() {

        selectUsersList = mydb.getData();

        suAdapter = new DeviceContactAdapter(DeviceContactActivity.this, selectUsersList, new DeviceContactAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(SelectUser selectUser) {
                selectedUsers.add(selectUser);
                selectedUsers = (ArrayList<SelectUser>) selectedUsers.stream().distinct().collect(Collectors.toList());
                Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
                saveData();
            }

            @Override
            public void onItemUncheck(SelectUser selectUser) {
                selectedUsers.remove(selectUser);
                selectedUsers = (ArrayList<SelectUser>) selectedUsers.stream().distinct().collect(Collectors.toList());
                saveData();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(DeviceContactActivity.this));
        recyclerView.setAdapter(suAdapter);


    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Main2", null);
        Type type = new TypeToken<ArrayList<SelectUser>>() {
        }.getType();
        selectedUsers = gson.fromJson(json, type);
        if (selectedUsers == null) {
            selectedUsers = new ArrayList<>();
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(selectedUsers);
        editor.putString("Main2", json);
        editor.apply();
    }

}