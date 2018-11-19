package com.example.peterboncheff.durhackapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import static android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    //, LocationListener,
//        OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener
    public final static byte SENDING = 1, CONNECTING = 2, ERROR = 3, SENT = 4, SHUTDOWN = 5;
    private final static short PAUSE_IN_MILISECONDS = 1000;
    public static final String TAG = "hallo";
    private final byte REQUEST_CODE = 34;
    private boolean mLocationPermissionGranted = false;
    public static final String URI_NAME = "uri";

    private Button cameraButton;
    private TextView textView;
    private Location currentLocation;
    protected static ImageView image;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Vector<String> list;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!checkPermissions()) requestPermissions();
        else getLastLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    private void init() {
        this.cameraButton = findViewById(R.id.cameraButton);
        this.cameraButton.setOnClickListener(this);
        this.textView = findViewById(R.id.aText);
        this.listView = findViewById(R.id.listOfPreviousItems);
        this.list = new Vector<>();
        this.list.add("London,UK\t\t\tBig Ben");
        this.list.add("Leeds,UK\t\t\tRoyal Armouries Museum");
        this.list.add("Bristol,UK\t\t\tBristol Harbour");
        this.list.add("Frankfurt,GR\t\t\tPalmengarden");
        this.list.add("Paris,FR\t\t\tLouvre Museum");
        this.list.add("Barcelona,SP\t\t\tSegrada Familia");
        this.list.add("Sydney,AU\t\t\tSydney Opera House");

        this.list.add("London,UK\t\t\tBig Ben");
        this.list.add("Leeds,UK\t\t\tRoyal Armouries Museum");
        this.list.add("Bristol,UK\t\t\tBristol Harbour");
        this.list.add("Frankfurt,GR\t\t\tPalmengarden");
        this.list.add("Paris,FR\t\t\tLouvre Museum");
        this.list.add("Barcelona,SP\t\t\tSegrada Familia");
        this.list.add("Sydney,AU\t\t\tSydney Opera House");

        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        this.listView.setAdapter(this.adapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == this.cameraButton.getId() ){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            final int DEFAULT_REQUEST_CODE = 0;
            startActivityForResult(intent, DEFAULT_REQUEST_CODE);
            final Intent goToFoundLocation = new Intent(getApplicationContext(), FoundLocation.class);
            intent.putExtra(URI_NAME, searchForLocation());
            startActivity(goToFoundLocation);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        final String DATA_NAME = "data";
        Bitmap bm = (Bitmap) Objects.requireNonNull(data.getExtras()).get(DATA_NAME);
        image.setImageBitmap(bm);
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }


    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
        } else {
            Log.i(TAG, "Requesting permission");
            startLocationPermissionRequest();
        }
    }

    private void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    currentLocation = task.getResult();
                    //textView.setText(String.format("%s %s", currentLocation.getLatitude(), currentLocation.getLongitude()));
                } else {
                    Log.w(TAG, "getLastLocation:exception", task.getException());
                }
            }
        });
    }

    private Uri searchForLocation(){
        String query = currentLocation.getLatitude() + " " + currentLocation.getLongitude();
        String escapedQuery;
        try {
            final String UTF = "UTF-8", GOOGLE_MAPS_LINK = "https://www.google.com/maps/@";
            escapedQuery = URLEncoder.encode(query, UTF);
            Uri uri = Uri.parse(GOOGLE_MAPS_LINK + escapedQuery);
            Log.d(TAG, uri.toString());
            return uri;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String EXCEPTION_THROWN = "Search location method, exception";
        Log.d(TAG, EXCEPTION_THROWN);
        return null;
    }
}
