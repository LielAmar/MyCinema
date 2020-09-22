package mycinema.liel.net.mycinemaproject.HttpManagers.Movies;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import mycinema.liel.net.mycinemaproject.HttpManagers.Connector;
import mycinema.liel.net.mycinemaproject.MainActivity;
import mycinema.liel.net.mycinemaproject.Objects.Movie;

public class SendMovies extends AsyncTask<Void, Void, String> {

    private static final String TAG = "SendMovies";

    private String address;

    private String name, description, premiere, category, trailer, cinemacity, yesplanet;
    private int length, agelimit;
    private Bitmap poster;

    public SendMovies(String address, String nameET, String descriptionET, String premiereET, String categoryET,
                      int lengthET, int agelimitET, String trailerET, String cinemacityET, String yesplanetET, Bitmap poster) {
        this.address = address;

        this.name = nameET;
        this.description = descriptionET;
        this.premiere = premiereET;
        this.category = categoryET;
        this.length = lengthET;
        this.agelimit = agelimitET;
        this.trailer = trailerET;
        this.cinemacity = cinemacityET;
        this.yesplanet = yesplanetET;
        this.poster = poster;

    }

    @Override
    protected String doInBackground(Void... params) {
        return this.sendMovie();
    }

    @Override
    protected void onPostExecute (String response) {
        super.onPostExecute(response);

        if(response != null) {
            if(response.toLowerCase().contains("exist"))
                Snackbar.make(MainActivity.instance.findViewById(android.R.id.content), "Movie Exists", Snackbar.LENGTH_SHORT).show();
            else
                Snackbar.make(MainActivity.instance.findViewById(android.R.id.content), "Movie Created", Snackbar.LENGTH_SHORT).show();
            Log.d(TAG, "*success* " + response);
        } else {
            Log.d(TAG, "*no success* " + response);
        }
    }

    private String sendMovie(){
        HttpURLConnection connection = Connector.connect(address);

        if(connection == null) {
            return null;
        }

        try {
            OutputStream os = connection.getOutputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bw.write(new Movie( name, description, premiere, category, length, agelimit, trailer, cinemacity, yesplanet, poster).packData());

            bw.flush();
            bw.close();
            os.close();

            int responseCode = connection.getResponseCode();

            if(responseCode == connection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer response = new StringBuffer();

                String line;
                while((line=br.readLine()) != null) {
                    response.append(line);
                }

                br.close();
                return response.toString();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d(TAG, "UnsupportedEncodingException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "IOException");
        }

        return null;
    }
}
