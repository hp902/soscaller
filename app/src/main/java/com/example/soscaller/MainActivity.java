package com.example.soscaller;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.soscaller.contact.ContactActivity;

import java.util.Objects;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;

public class MainActivity extends AppCompatActivity {

    private ImageButton sosButton, contactButton;
    private GpsTracker gpsTracker;
    private String numbers[];

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

        numbers = new String[]{"6386506033"};

        getLocation();

        message ="This is Test SOS message with last known location"
                +System.getProperty("line.separator")+ "-SOS"
                +System.getProperty("line.separator")
                +System.getProperty("line.separator")
                +"https://www.google.com/maps/search/?api=1&query=" +latitude+ ","+longitude;

        contactButton.setFocusableInTouchMode(false);

        contactButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
            startActivity(intent);
        });

        sosButton.setOnClickListener(v -> sendSMS());

    }

    private void sendSMS() {
        try {
            SmsManager smsManager = SmsManager.getDefault();

            for(String number: numbers) {
                smsManager.sendTextMessage(number, null, message, null, null);
            }
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void getLocation(){
        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        }else{
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