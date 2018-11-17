package com.example.peterboncheff.durhackapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Button cameraButton;
    private TextView textView;
    private ImageView mainImage;
    private GoogleApiClient googleApiClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        apiSetup();
    }

    private void apiSetup() {
        this.googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void init() {
        this.cameraButton = findViewById(R.id.cameraButton);
        this.cameraButton.setOnClickListener(this);

        this.textView = findViewById(R.id.aText);
        this.mainImage = findViewById(R.id.imageView);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final int DEFAULT_REQUEST_CODE = 0;
        startActivityForResult(intent, DEFAULT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        final String DATA_NAME = "data";
        Bitmap bm = (Bitmap) Objects.requireNonNull(data.getExtras()).get(DATA_NAME);
        this.mainImage.setImageBitmap(bm);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
