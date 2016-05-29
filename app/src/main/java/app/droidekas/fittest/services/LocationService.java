package app.droidekas.fittest.services;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import app.droidekas.fittest.models.LocationHistory;

import static app.droidekas.fittest.log.Logger.logger;

/**
 * Created by Satyarth on 29/05/16.
 */
public class LocationService extends IntentService {
    public static final String INTENT_NAME = "GetLocation";
    public static final String SOURCE = "locationSource";


    Criteria criteria;
    public static final String TAG = LocationService.class.getSimpleName();

    public LocationService() {
        super(INTENT_NAME);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Location loc = getCurrentLocation();
        if (loc != null) {
            LocationHistory lh = new LocationHistory();
            Bundle extras = intent.getExtras();
            if (extras.containsKey(BatteryReceiver.BATTERY_STATE)) {
                lh.setBattery(true);
                lh.setBatteryChargeType(extras.getString(BatteryReceiver.BATTERY_STATE));

            } else if (extras.containsKey(WifiStateChangeReceiver.WIFI_NAME)) {
                lh.setWifi(true);
                lh.setWifiName(extras.getString(WifiStateChangeReceiver.WIFI_NAME));
            }
            lh.setTimestamp(System.currentTimeMillis() / 1000);
            lh.setLocationLat(loc.getLatitude());
            lh.setLocationLong(loc.getLongitude());
            lh.save();
        }
    }

    public Location getCurrentLocation() {
        LocationManager mLocationManager;
        Location mLocation = null;
        try {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            // setup bestProvider
            criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String bestprovider = mLocationManager.getBestProvider(criteria, true);
            // get an initial current location
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
            }
            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (mLocation == null)
                mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (mLocation == null)
                mLocation = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        } catch (Exception e) {
            logger(e);
        }
        logger(TAG, "current location = " + mLocation.getLatitude() + "," + mLocation.getLongitude());
        return mLocation;
    }
}
