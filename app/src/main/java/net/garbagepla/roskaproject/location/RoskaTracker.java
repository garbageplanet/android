package net.garbagepla.roskaproject.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.getpebble.android.kit.util.PebbleDictionary;

import net.garbagepla.roskaproject.MainActivity;
import net.garbagepla.roskaproject.RoskaUtil;

/**
 * Created by anovil on 07/11/15.
 */
public class RoskaTracker {

    private MainActivity context = null;

    private LocationManager locationManager = null;

    private RoskaLocationListener locationListener = null;

    private PebbleDictionary userData = null;

    public RoskaTracker(MainActivity context) {
        this.context = context;
        this.locationListener = new RoskaLocationListener(this);
    }

    public void startTracking() {
        // get the location manager
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        // start getting updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    public void stopTracking(Location location) {
        Log.i("RoskaTracker", "Stopping tracking");

        locationManager.removeUpdates(this.locationListener);

        context.getListItems().add("Trash plot lat:" + location.getLatitude() +
                        " long: " + location.getLongitude() + " scale: " + RoskaUtil.byteArrayToInt(getUserData().getBytes(0))
        );

        context.getAdapter().notifyDataSetChanged();
    }

    private PebbleDictionary getUserData() {
        return userData;
    }

    public void setUserData(PebbleDictionary userData) {
        this.userData = userData;
    }



}
