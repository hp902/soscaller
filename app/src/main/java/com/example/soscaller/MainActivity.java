package com.example.soscaller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soscaller.contact.ContactActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    ImageButton sosButton, contactButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);


        sosButton = findViewById(R.id.sos_button);
        contactButton = findViewById(R.id.contact_button);

        contactButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
            startActivity(intent);
        });

    }
}