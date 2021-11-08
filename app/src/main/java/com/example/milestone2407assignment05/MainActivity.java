package com.example.milestone2407assignment05;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 13;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;
    private final LatLng mDestinationLatLng = new LatLng(43.0757339,-89.4061951);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);

        mapFragment.getMapAsync(googleMap->{
            mMap = googleMap;
            googleMap.addMarker(new MarkerOptions().position(mDestinationLatLng).title("Destination"));
        });
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        displayMyLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            displayMyLocation();
        }
    }

    private void displayMyLocation(){
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, task -> {
                Location mLastKnownLocation = task.getResult();
                Log.i("Need Help", "About to check Task");
                    if (task.isSuccessful() && mLastKnownLocation != null){
                        Log.i("Need help", "Task was successful");
                        mMap.addPolyline(new PolylineOptions().add(
                                new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()),
                                    mDestinationLatLng));
                        mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()) ).title("Current Location"));
                    }
            });
        }
    }
}