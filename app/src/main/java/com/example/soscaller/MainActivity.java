package com.example.soscaller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.soscaller.contact.ContactActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ImageButton sosButton, contactButton;
    private GpsTracker gpsTracker;

    private ArrayList<SelectUser> selectedContacts;
    private final ArrayList<String> numbers = new ArrayList<>();

    private double latitude;
    private double longitude;
    private String message;

    public static final int RequestPermissionCode = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        if (!CheckingPermissionIsEnabledOrNot()) {
            RequestMultiplePermission();
        }

        sosButton = findViewById(R.id.sos_button);
        contactButton = findViewById(R.id.contact_button);

        loadData();

        for (SelectUser selectUser : selectedContacts) {
            numbers.add(selectUser.getPhone());
        }


        getLocation();

        message = "This is Test SOS message with last known location"
                + System.getProperty("line.separator") + "-SOS"
                + System.getProperty("line.separator")
                + System.getProperty("line.separator")
                + "Click on this link to get the location: "
                + "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;

        contactButton.setFocusableInTouchMode(false);
        contactButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
            startActivity(intent);
        });


        sosButton.setFocusableInTouchMode(false);
        sosButton.setOnClickListener(v -> {

            if (numbers.size() == 0) {
                Toast.makeText(this, "Please Select The Contacts", Toast.LENGTH_SHORT).show();
            } else {
                /*Toast.makeText(this, numbers.get(0), Toast.LENGTH_SHORT).show();*/
                sendSMS();
            }
        });

        gpsTracker.setOnLocationChangeListener((longitude, latitude) -> {
            this.longitude=longitude;
            this.latitude=latitude;
            updateMessage();
        });

    }

    private void updateMessage() {
        message = "This is Test SOS message with last known location"
                + System.getProperty("line.separator") + "-SOS"
                + System.getProperty("line.separator")
                + System.getProperty("line.separator")
                + "Click on this link to get the location: "
                + "https://www.google.com/maps/search/?api=1&query=" +
                this.latitude + "," + this.longitude;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
        numbers.clear();
        for (SelectUser selectUser : selectedContacts) {
            numbers.add(selectUser.getPhone());
        }
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Main2", null);
        Type type = new TypeToken<ArrayList<SelectUser>>() {
        }.getType();
        selectedContacts = gson.fromJson(json, type);
        if (selectedContacts == null) {
            selectedContacts = new ArrayList<>();
        }

        Log.i(TAG, "DATA LOADED");
    }

    private void sendSMS() {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> messageArray = smsManager.divideMessage(message);  // To manage long length messages

            for (String number : numbers) {
                smsManager.sendMultipartTextMessage(number, null, messageArray, null, null);
            }

            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void getLocation() {
        gpsTracker = new GpsTracker(MainActivity.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        ACCESS_FINE_LOCATION,
                        READ_CONTACTS,
                        SEND_SMS,
                        ACCESS_COARSE_LOCATION,
                        INTERNET

                }, RequestPermissionCode);

    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);

        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);
        int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FourthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestPermissionCode) {
            if (grantResults.length > 0) {

                boolean ReadContactsPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean LocationPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean SendSMSPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean CoarseLocationPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                boolean InternetPermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                if (!(ReadContactsPermission && LocationPermission && SendSMSPermission && CoarseLocationPermission
                        && InternetPermission)) {
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}