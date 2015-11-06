package net.garbagepla.roskaproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.UUID;


public class MainActivity extends ActionBarActivity {


    // private final static UUID PEBBLE_APP_UUID = UUID.fromString("763c46ac-73c9-4229-aca7-97be723a59c7");

    private final static UUID PEBBLE_APP_UUID = UUID.fromString("59c46eff-156f-430b-90cf-e365cbd4322a"); // demo app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.text_view01);
        // boolean connected = PebbleKit.isWatchConnected(getApplicationContext());
        final boolean connected = PebbleKit.areAppMessagesSupported(getApplicationContext());
        textView.setText("The pebble kit app messages supported: " + connected);

/*
        Button sendButton = (Button) findViewById(R.id.button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PebbleDictionary data = new PebbleDictionary();
                data.addUint8(0, (byte) 42);
                data.addString(1, "A string");
                PebbleKit.sendDataToPebble(getApplicationContext(), PEBBLE_APP_UUID, data);
                Log.i("MainActivity", "sent Data");
            }
        });
*/

     /*   PebbleKit.registerReceivedAckHandler(getApplicationContext(), new PebbleKit.PebbleAckReceiver(PEBBLE_APP_UUID) {

            @Override
            public void receiveAck(Context context, int transactionId) {
                Log.i(getLocalClassName(), "Received ack for transaction " + transactionId);
            }

        });

        PebbleKit.registerReceivedNackHandler(getApplicationContext(), new PebbleKit.PebbleNackReceiver(PEBBLE_APP_UUID) {

            @Override
            public void receiveNack(Context context, int transactionId) {
                Log.i(getLocalClassName(), "Received nack for transaction " + transactionId);
            }

        });
*/
        final Handler handler = new Handler();
        PebbleKit.registerReceivedDataHandler(this, new PebbleKit.PebbleDataReceiver(PEBBLE_APP_UUID) {

            @Override
            public void receiveData(final Context context, final int transactionId, final PebbleDictionary data) {
                if ( data == null ) {
                    return ;
                }
                Log.i(getLocalClassName(), "Received value=" + data + " for key: 0");

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        /* Update your UI here. */
                        Toast.makeText(context, "Received trash from pebble", Toast.LENGTH_SHORT);

                        // Acquire a reference to the system Location Manager
                        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                        String locationProvider = LocationManager.NETWORK_PROVIDER;
                        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

                        TextView textView = (TextView) findViewById(R.id.text_view01);
                        textView.setText("Received plot at location lat:" + lastKnownLocation.getLatitude() +
                                        " long: " + lastKnownLocation.getLongitude() + " at: " + lastKnownLocation.getTime()
                        );
                    }

                });
                PebbleKit.sendAckToPebble(getApplicationContext(), transactionId);
            }

        });

        PebbleKit.registerPebbleConnectedReceiver(getApplicationContext(), new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "Pebble connected!",Toast.LENGTH_SHORT);
            }

        });

        PebbleKit.registerPebbleDisconnectedReceiver(getApplicationContext(), new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "Pebble disconnected!", Toast.LENGTH_SHORT);
            }

        });

        // Launching my app
        // PebbleKit.startAppOnPebble(getApplicationContext(), PEBBLE_APP_UUID);

// Closing my app
        PebbleKit.closeAppOnPebble(getApplicationContext(), PEBBLE_APP_UUID);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
