package net.garbagepla.roskaproject.pebble;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import net.garbagepla.roskaproject.MainActivity;
import net.garbagepla.roskaproject.RoskaUtil;
import net.garbagepla.roskaproject.location.RoskaTracker;

/**
 * Created by anovil on 07/11/15.
 */
public class RoskaPebbleDataReceiver extends PebbleKit.PebbleDataReceiver {

    private final MainActivity activity;

    private RoskaTracker locationTracker;

    private static final String CLASS_NAME = "RoskaPebbleDataReceiver" ;

    public RoskaPebbleDataReceiver(MainActivity activity) {
        super(RoskaUtil.PEBBLE_APP_UUID);
        this.activity = activity ;
    }

    public void receiveData(final Context context, final int transactionId, final PebbleDictionary data) {
        if ( data == null ) {
            return ;
        }

        Log.i(CLASS_NAME, "Received value=" + RoskaUtil.byteArrayToInt(data.getBytes(0)) + " for key: 0 and size: " + data.size());

        final Handler handler = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run() {
                        /* Update your UI here. */
                Toast.makeText(context, "Received trash from pebble", Toast.LENGTH_SHORT);

                locationTracker = new RoskaTracker(activity);
                locationTracker.setUserData(data);
                locationTracker.startTracking();

            }

        });
        PebbleKit.sendAckToPebble(this.activity, transactionId);
    }

}
