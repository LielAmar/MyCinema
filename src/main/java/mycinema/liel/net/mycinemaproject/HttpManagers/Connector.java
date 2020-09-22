package mycinema.liel.net.mycinemaproject.HttpManagers;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connector {

    private static final String TAG = "Connector";

    public static HttpURLConnection connect(String address) {
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            return connection;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "MalformedURLException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "IOException");
        }
        return null;
    }
}
