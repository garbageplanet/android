package net.garbagepla.roskaproject.location;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

import com.getpebble.android.kit.util.PebbleDictionary;

import net.garbagepla.roskaproject.MainActivity;
import net.garbagepla.roskaproject.RoskaUtil;

import java.net.URL;
import java.util.HashMap;

/**
 * Created by anovil on 07/11/15.
 */
public class RoskaTracker {

    private MainActivity activity = null;

    private LocationManager locationManager = null;

    private RoskaLocationListener locationListener = null;

    private PebbleDictionary userData = null;



    public RoskaTracker(MainActivity activity) {
        this.activity = activity;
        this.locationListener = new RoskaLocationListener(this);
    }

    public void startTracking() {
        // get the location manager
        locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);

        // start getting updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    public void stopTracking(Location location) {
        Log.i("RoskaTracker", "Stopping tracking");

        locationManager.removeUpdates(this.locationListener);

        activity.getLogListItems().add("Trash plot lat:" + location.getLatitude() +
                        " long: " + location.getLongitude() + " scale: " + getUserDataValue()
        );

        activity.getAdapter().notifyDataSetChanged();

        HashMap<String, String> params = new HashMap<String, String>(3);
        params.put("lat", Double.toString(location.getLatitude()));
        params.put("lng", Double.toString(location.getLongitude()));
        params.put("amount", Long.toString(getUserDataValue()));
        new ApiLauncher(params).execute("http://api.garbagepla.net/api/userlesstrash");
    }

    private PebbleDictionary getUserData() {
        return userData;
    }

    public void setUserData(PebbleDictionary userData) {
        this.userData = userData;
    }

    private Long getUserDataValue() {
        Long l = 0L;
        if (this.getUserData().getInteger(RoskaUtil.KEY_BUTTON_DOWN) != null) {
            return this.getUserData().getInteger(RoskaUtil.KEY_BUTTON_DOWN);
        } else if (this.getUserData().getInteger(RoskaUtil.KEY_BUTTON_UP) != null) {
            return this.getUserData().getInteger(RoskaUtil.KEY_BUTTON_UP);
        }
        return l;
    }

    private class ApiLauncher extends AsyncTask<String, String, String> {

        private final HashMap<String, String> mData;

        public ApiLauncher(HashMap<String, String> data) {
            this.mData = data;
        }
        protected String doInBackground(String... params) {
            return RoskaUtil.postData(params[0], mData);
        }

        protected void onProgressUpdate(Integer... progress) {
            // setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            // showDialog("Downloaded " + result + " bytes");
        }
    }


}
