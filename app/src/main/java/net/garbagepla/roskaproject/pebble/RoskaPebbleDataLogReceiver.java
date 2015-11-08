package net.garbagepla.roskaproject.pebble;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;

import net.garbagepla.roskaproject.MainActivity;
import net.garbagepla.roskaproject.RoskaUtil;
import net.garbagepla.roskaproject.location.RoskaTracker;

import java.util.UUID;

/**
 * Created by anovil on 07/11/15.
 */
public class RoskaPebbleDataLogReceiver extends PebbleKit.PebbleDataLogReceiver {

    private final MainActivity mActivity;

    private RoskaTracker locationTracker;

    protected RoskaPebbleDataLogReceiver(UUID subscribedUuid, MainActivity activity) {
        super(RoskaUtil.PEBBLE_APP_UUID);
        this.mActivity = activity ;
    }

    @Override
    public void receiveData(final Context context, UUID logUuid, Long timestamp, final Long tag, final Long data) {
        final Handler handler = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run() {
                        /* Update your UI here. */
                Toast.makeText(context, "Received trash from pebble", Toast.LENGTH_SHORT);

                locationTracker = new RoskaTracker(mActivity);
                // locationTracker.setUserData(data.toString());
                locationTracker.startTracking();

            }

        });
    }
}
