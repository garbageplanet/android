package net.garbagepla.roskaproject;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.getpebble.android.kit.PebbleKit;

import net.garbagepla.roskaproject.location.RoskaTracker;
import net.garbagepla.roskaproject.pebble.RoskaPebbleDataReceiver;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView logListView = (ListView) findViewById(R.id.logListView);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, logListItems) ;
        logListView.setAdapter(adapter);

        final boolean connected = PebbleKit.areAppMessagesSupported(getApplicationContext());
        if ( connected ) {
            setupPebbleConnections();
        }

    }

    private void setupPebbleConnections() {

        PebbleKit.registerReceivedAckHandler(getApplicationContext(), new PebbleKit.PebbleAckReceiver(RoskaUtil.PEBBLE_APP_UUID) {

            @Override
            public void receiveAck(Context context, int transactionId) {
                Log.i(getLocalClassName(), "Received ack for transaction " + transactionId);
            }

        });

        PebbleKit.registerReceivedNackHandler(getApplicationContext(), new PebbleKit.PebbleNackReceiver(RoskaUtil.PEBBLE_APP_UUID) {

            @Override
            public void receiveNack(Context context, int transactionId) {
                Log.i(getLocalClassName(), "Received nack for transaction " + transactionId);
            }

        });

        RoskaPebbleDataReceiver dataReceiver = new RoskaPebbleDataReceiver(this);
        PebbleKit.registerReceivedDataHandler(this, dataReceiver);

    }

    public ArrayAdapter<String> getAdapter() {
        return adapter;
    }

    public ArrayList<String> getLogListItems() {
        return logListItems;
    }

    private MainActivity getMainActivity() {
        return this;
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

    ArrayAdapter <String> adapter;

    ArrayList<String> logListItems =new ArrayList<String>();

    RoskaTracker locationTracker = null;


}
