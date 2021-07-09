package com.example.soscaller.devicecontact;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soscaller.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainActivity2 extends AppCompatActivity {


    private static final String TAG = "MainActivity2";

    ArrayList<SelectUser> selectedUsers;

    DatabaseAdapter mydb;
    SelectUserAdapter suAdapter;
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

        /* selectedUsers = (ArrayList<SelectUser>) getIntent().getSerializableExtra("key");*/


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
            public boolean onQueryTextChange(String stext) {
                suAdapter.filter(stext);
                return false;
            }
        });


    }


    private void setRecyclerview() {

        /*new ContactLoader().execute();*/

        selectUsersList = mydb.getData();

        suAdapter = new SelectUserAdapter(MainActivity2.this, selectUsersList, new SelectUserAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(SelectUser selectUser) {
                selectedUsers.add(selectUser);
                selectedUsers = (ArrayList<SelectUser>) selectedUsers.stream().distinct().collect(Collectors.toList());
                saveData();
            }

            @Override
            public void onItemUncheck(SelectUser selectUser) {
                selectedUsers.remove(selectUser);
                selectedUsers = (ArrayList<SelectUser>) selectedUsers.stream().distinct().collect(Collectors.toList());
                saveData();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
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

        Log.i(TAG, "DATA LOADED --> " + selectedUsers.size());
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(selectedUsers);
        editor.putString("Main2", json);
        editor.apply();

        Log.i(TAG, "DATA SAVED --> " + selectedUsers.size());
    }

    /*public class ContactLoader extends AsyncTask<Void, Void, List<SelectUser>> {

        @Override
        protected List<SelectUser> doInBackground(Void... voids) {
            return mydb.getData();
        }

        @Override
        protected void onPostExecute(@NonNull List<SelectUser> selectUsers) {
            if (!selectUsers.isEmpty()) {

                selectUsersList = selectUsers;

                suAdapter = new SelectUserAdapter(MainActivity2.this, selectUsersList, new SelectUserAdapter.OnItemCheckListener() {
                    @Override
                    public void onItemCheck(SelectUser selectUser) {
                        selectedUsers.add(selectUser);
                    }

                    @Override
                    public void onItemUncheck(SelectUser selectUser) {
                        selectedUsers.remove(selectUser);
                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
                recyclerView.setAdapter(suAdapter);

                saveData();
            }

        }
    }*/
}