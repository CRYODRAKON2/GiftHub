package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GMaps extends AppCompatActivity implements OnMapReadyCallback {

    Button currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        currentLocation = findViewById(R.id.currentLocation);
        currentLocation.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("location", "13.352585, 74.793579");
            setResult(RESULT_OK, resultIntent);
            super.finish();
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // Add a marker in India and move the camera
        LatLng india = new LatLng(13.352585, 74.793579);
        googleMap.addMarker(new MarkerOptions().position(india).title("Marker in India"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(india));

        // Customizing marker
        MarkerOptions options = new MarkerOptions().position(india).title("MIT MANIPAL");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        googleMap.addMarker(options);
    }
}
