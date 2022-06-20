package com.example.myapplication.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class LocationUtils implements LocationListener {

    private Context mContext;

    private LocationManager mLocationManager;
    private boolean isGPSEnabled, isNetworkEnabled;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 meter

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 20; // 20 sec

    private int maxAttempts = 3, currentAttempt = 1;

    private LocationChangeListener locationChangeListener;

    public LocationUtils(Context context) {
        mContext = context;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void setLocationChangeListener(LocationChangeListener locationChangeListener) {
        this.locationChangeListener = locationChangeListener;
    }

    public void stopUsingGPS(){
        try {
            boolean hasPermission = (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    && (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
            if (hasPermission) {
                if (mLocationManager != null) {
                    mLocationManager.removeUpdates(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isGpsEnabled() {
        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return isGPSEnabled || isNetworkEnabled;
    }

    public Location getLocation() {
        boolean hasPermission = (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        if (hasPermission) {
            //Criteria criteria = new Criteria();
            //String bestProvider = mLocationManager.getBestProvider(criteria, false);

            /*List<String> providers = mLocationManager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = l;
                }
            }*/


            currentAttempt = 1;

            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (isNetworkEnabled) {
                if (location == null) {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.e("Network", "Network");
                    if (mLocationManager != null) {
                        location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
            }

            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {

                if (location == null) {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.e("GPS Enabled", "GPS Enabled");
                    if (mLocationManager != null) {
                        location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }

                if (location == null) {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.PASSIVE_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (mLocationManager != null) {
                        location = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    }
                }

                // dummy location
                /*if(location == null) {
                    location = new Location("gps");
                    location.setLatitude(23.770530);
                    location.setLongitude(90.366309);
                }*/
            }

            return location;

        }
        return null;
    }



    @Override
    public void onLocationChanged(Location location) {
        if(locationChangeListener != null) {
            locationChangeListener.onLocationUpdate(location);
        }

        if(currentAttempt >= maxAttempts) {
            stopUsingGPS();
        } else {
            currentAttempt++;
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public interface LocationChangeListener {
        public void onLocationUpdate(Location location);
    }


}
