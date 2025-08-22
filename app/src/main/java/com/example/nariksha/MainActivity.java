package com.example.nariksha;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

// libraries that require accelerometer for shaking feature
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;


public class MainActivity extends AppCompatActivity {

    private static final int REQ_PERMS = 1001;

    private ContactsDBHelper dbHelper;

    private SensorManager sensorManager;
    private float accelCurrent;
    private float accelLast;
    private float shake;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper = new ContactsDBHelper(this);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sensorManager.registerListener(sensorListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        accelCurrent = SensorManager.GRAVITY_EARTH;
        accelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sos = findViewById(R.id.sosButton);
        Button manage = findViewById(R.id.manageContactsButton);

        sos.setOnClickListener(v -> trySendSOS());
        manage.setOnClickListener(v -> startActivity(new Intent(this, ManageContactsActivity.class)));

        Button aiChat = findViewById(R.id.aiChatButton);
        aiChat.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChatbotActivity.class);
            startActivity(intent);
        });
    }


    private void trySendSOS() {
        List<String> contacts = loadContacts();
        if (contacts.isEmpty()) {
            Toast.makeText(this, "No emergency contacts added", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!hasAllPermissions()) {
            requestAllPermissions();
            return;
        }
        sendWithLocation(contacts);
    }

    private boolean hasAllPermissions() {
        boolean sms = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        boolean fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        return sms && fine && coarse;
    }

    private void requestAllPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                REQ_PERMS);
    }

    @SuppressWarnings("MissingPermission")
    private void sendWithLocation(List<String> contacts) {
        FusedLocationProviderClient fused = LocationServices.getFusedLocationProviderClient(this);
        Task<Location> t = fused.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null);
        t.addOnSuccessListener(loc -> {
            String msg;
            if (loc != null) {
                String url = "https://maps.google.com/?q=" + loc.getLatitude() + "," + loc.getLongitude();
                msg = "SOS! I need help. My location: " + url;
            } else {
                msg = "SOS! I need help. Location unavailable.";
            }
            sendSmsToAll(contacts, msg);
        }).addOnFailureListener(e -> {
            String msg = "SOS! I need help. Location error.";
            sendSmsToAll(contacts, msg);
        });
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            accelLast = accelCurrent;
            accelCurrent = (float) Math.sqrt(x*x + y*y + z*z);
            float delta = accelCurrent - accelLast;
            shake = shake * 0.9f + delta; // low-pass filter

            if (shake > 12) { // adjust sensitivity
                call112();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorListener);
        }
    }

    private void call112() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 123);
            return;
        }
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:112"));
        startActivity(callIntent);
    }

    private void sendSmsToAll(List<String> contacts, String msg) {
        try {
            SmsManager sm = SmsManager.getDefault();
            for (String c : contacts) {
                String n = c.trim();
                if (!n.isEmpty()) sm.sendTextMessage(n, null, msg, null, null);
            }
            Toast.makeText(this, "SOS sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SOS", Toast.LENGTH_SHORT).show();
        }
    }

    private List<String> loadContacts() {
        ArrayList<Contact> contactList = dbHelper.getAllContacts();
        ArrayList<String> phoneNumbers = new ArrayList<>();
        for (Contact c : contactList) {
            phoneNumbers.add(c.getPhone());
        }
        return phoneNumbers;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMS) trySendSOS();
    }
}
