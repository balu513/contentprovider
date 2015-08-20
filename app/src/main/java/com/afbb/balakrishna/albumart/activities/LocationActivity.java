package com.afbb.balakrishna.albumart.activities;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.afbb.balakrishna.albumart.R;

public class LocationActivity extends Activity implements LocationListener {

    private TextView tv_Curr_latlang;
    private TextView tv_Changes_latlang;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        tv_Curr_latlang = (TextView) findViewById(R.id.textView_currlatlang);
        tv_Changes_latlang = (TextView) findViewById(R.id.textView_latlangchanges);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    private void getCurrentLocation() {
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        double latitude = lastKnownLocation.getLatitude();
        double longitude = lastKnownLocation.getLongitude();
        tv_Curr_latlang.setText("Current Latitude: " + latitude + "\n\n" + "Current Longitude: " + longitude);
    }


    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        tv_Changes_latlang.setText("Latitude: " + latitude + "\n\n" + "Longitude: " + longitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null)
            locationManager.removeUpdates(this);
    }
}
