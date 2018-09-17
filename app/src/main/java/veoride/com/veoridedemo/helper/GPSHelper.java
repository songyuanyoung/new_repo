package veoride.com.veoridedemo.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngineListener;

import java.util.Iterator;

public class GPSHelper {
    public static LocationManager lm;
    private Context mcontext;
    private static final String TAG = "GPS_REPORT";

    public GPSHelper(Context context) {
        mcontext = context;
        init();
    }

    private void init() {
        lm = (LocationManager) mcontext.getSystemService(Context.LOCATION_SERVICE);


        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(mcontext, "Please turn on GPS...", Toast.LENGTH_SHORT).show();
            return;
        }

        String bestProvider = lm.getBestProvider(getCriteria(), true);

        if (bestProvider == null) {
            Log.e(TAG, "No location provider found!");
            return;
        }
        if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        Location location = lm.getLastKnownLocation(bestProvider);
        updateView(location);

        if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
    }

    private LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            updateView(location);
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
        public void onProviderEnabled(String provider) {
            if (ActivityCompat.checkSelfPermission(mcontext,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mcontext,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = lm.getLastKnownLocation(provider);
            updateView(location);
        }

        public void onProviderDisabled(String provider) {
            updateView(null);
        }

    };


    private void updateView(Location location) {
        if (location != null) {
            Toast.makeText(mcontext,"Accuracy："+String.valueOf(location.getAccuracy()),Toast.LENGTH_SHORT).show();
        }
    }

    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        criteria.setBearingRequired(false);
        criteria.setAltitudeRequired(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    public void removeListener() {
        lm.removeUpdates(locationListener);
    }
}