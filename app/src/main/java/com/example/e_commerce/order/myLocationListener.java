package com.example.e_commerce.order;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.widget.Toast;

public class myLocationListener implements LocationListener {
    private Context activityContext;

    public myLocationListener(Context cont) {
        activityContext = cont;
    }

    @Override
    public void onProviderDisabled(String argo) {
        //Toast.makeText(activityContext, "GPS Disabled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String argo) {
        //Toast.makeText(activityContext, "GPS Enabled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(activityContext, location.getLatitude() + ", " + location.getLongitude(),
                //Toast.LENGTH_LONG).show();
    }
}

