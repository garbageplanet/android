package net.garbagepla.roskaproject;

import android.content.Entity;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by anovil on 07/11/15.
 */
public final class RoskaUtil {

    // private final static UUID PEBBLE_APP_UUID = UUID.fromString("763c46ac-73c9-4229-aca7-97be723a59c7");

    // public final static UUID PEBBLE_APP_UUID = UUID.fromString("59c46eff-156f-430b-90cf-e365cbd4322a"); // demo app

    // public final static UUID PEBBLE_APP_UUID = UUID.fromString("5b91c692-1ae5-4995-80b7-d92965768b03"); // rspd

    public final static UUID PEBBLE_APP_UUID = UUID.fromString("2acab78c-7d47-46fa-beff-d42da21fce6b"); // rspd accel

    private static final String CLASS_NAME = "RoskaUtil";

    public static final int KEY_BUTTON_UP = 0 ;
    public static final int KEY_BUTTON_DOWN = 1 ;


    public static int byteArrayToInt(byte[] b) {
        int MASK = 0xFF;
        int result = 0;
        result = b[0] & MASK;
        result = result + ((b[1] & MASK) << 8);
        result = result + ((b[2] & MASK) << 16);
        result = result + ((b[3] & MASK) << 24);
        return result;
    }

    public static final byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }

    public static final byte[] toByteArray(int value) {
        return  ByteBuffer.allocate(4).putInt(value).array();
    }

    public static final int fromByteArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    // courtesy: http://stackoverflow.com/questions/2938502/sending-post-data-in-android
    public static final String postData(String url, HashMap<String, String> params) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        String outputString = null;

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(params.size());
            Iterator it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                nameValuePairs.add(new BasicNameValuePair((String) pair.getKey(), (String) pair.getValue()));
                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
                byte [] result = EntityUtils.toByteArray(response.getEntity());
                outputString = new String(result, "UTF-8");
            }

        } catch (ClientProtocolException e) {
            Log.i(CLASS_NAME, "Client protocolException happened: " + e.getMessage());
        } catch (IOException e) {
            Log.i(CLASS_NAME, "Client IOException happened: " + e.getMessage());
        } catch (NetworkOnMainThreadException e) {
            Log.i(CLASS_NAME, "Client NetworkOnMainThreadException happened: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.i(CLASS_NAME, "Unknown exeception: " + e.getMessage());
        }
        return outputString;
    }
}
