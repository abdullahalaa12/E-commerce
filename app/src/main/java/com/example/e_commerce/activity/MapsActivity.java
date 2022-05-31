package com.example.e_commerce.activity;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.e_commerce.R;
import com.example.e_commerce.order.myLocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.e_commerce.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    LocationManager locManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        locManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener=new myLocationListener(getApplicationContext());

        try{
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,6000,0,locationListener);
        }catch (SecurityException ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Geocoder geocoder=new Geocoder(getApplicationContext());
        Location loc=null;

        try{
            loc=locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }catch (SecurityException ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (loc != null) {
            LatLng myPosition = new LatLng(loc.getLatitude(), loc.getLongitude());
            mMap.addMarker(new MarkerOptions().position(myPosition).title("My location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 15));
        }else {
            Toast.makeText(getApplicationContext(), "Please wait untill your position is determined ",
                    Toast.LENGTH_LONG).show();
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                try {
                    List<Address> addressList;
                    addressList = geocoder.getFromLocation(latLng.latitude,
                            latLng.longitude, 1);
                    if (!addressList.isEmpty()) {
                        String address = "";
                        for (int i = 0; i <= addressList.get(0).getMaxAddressLineIndex(); i++)
                            address += addressList.get(0).getAddressLine(i) + ", ";
                        mMap.addMarker(new MarkerOptions().position(latLng)
                                .title("Chosen location").snippet(address)).setDraggable(true);
                        Intent i = new Intent();
                        i.putExtra("PickedPoint",address);
                        setResult(RESULT_OK,i);
                        finish();
                }
                } catch (IOException e) { // if network not available
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}