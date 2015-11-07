package net.garbagepla.roskaproject.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by anovil on 07/11/15.
 */
public class RoskaLocationListener implements LocationListener {

    private RoskaTracker tracker = null;

    public RoskaLocationListener(RoskaTracker tracker) {
        this.tracker = tracker;
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("RoskaLocation: ", location.getAccuracy() + " " + location.getLatitude() + " " +
            location.getLongitude());

        // update UI

        // stop tracking
        tracker.stopTracking(location);

    }
}
