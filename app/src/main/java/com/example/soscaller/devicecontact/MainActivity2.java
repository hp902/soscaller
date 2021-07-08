package com.example.soscaller.devicecontact;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soscaller.R;
import com.example.soscaller.contact.SelectedUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {


    private static final String TAG = "MAINActivity2";

    ArrayList<SelectedUser> selectedUsers;

    DatabaseAdapter mydb;
    SelectUserAdapter suAdapter;
    List<SelectUser> selectUsersList = new ArrayList<>();

    RecyclerView recyclerView;
    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main2);
        selectedUsers = getIntent().getParcelableArrayListExtra("Data");

        selectedUsers.addAll(suAdapter.getSelectedUsers());



        mydb = new DatabaseAdapter(getApplicationContext());
        recyclerView = findViewById(R.id.contacts_list);
        setRecyclerview();

        //saveData();

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

        //saveData();

    }

    private void setRecyclerview() {

        /*new ContactLoader().execute();*/

        if (selectUsersList.size() == 0) {
            Log.i(TAG, "RECYCLER VIEW: Loading Contacts From device");

            new ContactLoader().execute();
        } else {
            Log.i(TAG, "RECYCLER VIEW: Loading Contacts From sharedPreference");

            suAdapter = new SelectUserAdapter(MainActivity2.this, selectUsersList);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
            recyclerView.setAdapter(suAdapter);
        }
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Main2", null);
        Type type = new TypeToken<ArrayList<SelectUser>>() {
        }.getType();

        selectUsersList = gson.fromJson(json, type);
        if (selectUsersList == null) {
            selectUsersList = new ArrayList<>();
        }

        Log.i(TAG, "DATA LOADED -->" + selectUsersList.size());
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(selectUsersList);
        editor.putString("Main2", json);
        editor.apply();

        Log.i(TAG, "DATA SAVED --> " + selectUsersList.size());
    }

    public class ContactLoader extends AsyncTask<Void, Void, List<SelectUser>> {

        @Override
        protected List<SelectUser> doInBackground(Void... voids) {
            return mydb.getData();
        }

        @Override
        protected void onPostExecute(@NonNull List<SelectUser> selectUsers) {
            if (!selectUsers.isEmpty()) {

                selectUsersList = selectUsers;

                suAdapter = new SelectUserAdapter(MainActivity2.this, selectUsersList);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
                recyclerView.setAdapter(suAdapter);

            }
        }
    }
}