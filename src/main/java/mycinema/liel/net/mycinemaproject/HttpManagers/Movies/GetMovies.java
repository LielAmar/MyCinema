package mycinema.liel.net.mycinemaproject.HttpManagers.Movies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import mycinema.liel.net.mycinemaproject.Analyze;
import mycinema.liel.net.mycinemaproject.HttpManagers.Connector;
import mycinema.liel.net.mycinemaproject.MainActivity;
import mycinema.liel.net.mycinemaproject.Objects.Movie;
import mycinema.liel.net.mycinemaproject.Objects.Phone;
import mycinema.liel.net.mycinemaproject.R;
import mycinema.liel.net.mycinemaproject.Utils.GlobalVars;
import mycinema.liel.net.mycinemaproject.Utils.Methods;

public class GetMovies extends AsyncTask<Void, Void, String> {

    private static final String TAG = "GetMovies";

    private String address;
    private String text;
    private boolean isRefresh;

    private String result;

    public GetMovies(String address, String text) {
        this.address = address;
        this.text = text;
        this.isRefresh = false;
    }

    public GetMovies(String address, String text, boolean isRefresh) {
        this.address = address;
        this.text = text;
        this.isRefresh = isRefresh;
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.getData();
    }

    @Override
    protected void onPostExecute (String response) {
        super.onPostExecute(response);

        if(response != null) {
            Log.d(TAG, "Connection success. " + response);
            this.text = response;

            // Start the main activity after receiving the data needed.
            if(!isRefresh) {
                Intent intent = new Intent(Analyze.instance, MainActivity.class);
                intent.putExtra("data", response);
                GlobalVars.phone.setData(response);
                this.result = response;
                Analyze.instance.startActivity(intent);
                Analyze.instance.finish();
            } else {
                MainActivity.instance.moviesFragment.srl.setRefreshing(false);
                MainActivity.instance.reloadFragment();
            }
        } else {
            // If the server is not working.
            Log.d(TAG, "Connection failed. " + response);
            Toast.makeText(Analyze.instance, "Connection Error", Toast.LENGTH_LONG).show();
        }
    }

    private String getData(){
        // Connect to the server
        HttpURLConnection connection = Connector.connect(address);

        // Check if connection worked
        if(connection == null) {
            return null;
        }

        // send data
        try {
            OutputStream os = connection.getOutputStream();

            // write to the server
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bw.write("get=get");

            bw.flush();
            bw.close();
            os.close();

            // get the response
            int responseCode = connection.getResponseCode();

            // if response
            if(responseCode == connection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer response = new StringBuffer();

                // Build the response
                String line;
                while((line=br.readLine()) != null) {
                    response.append(line);
                }

                br.close();

                // Response -> String
                this.text = response.toString();

                // Set up the movies arraylist in "LoadData" class in order to load all the movies.
                setUpMovies();

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

    public void setUpMovies(){
        GlobalVars.movies = new ArrayList<Movie>();

        String data = this.text;
        Log.d(TAG, "data: " + data);

        // Receive an arraylist with data of all movies.
        ArrayList<String[]> list = analyzeData(data);

        String str = "Connection success {\"response\":\"\"}";
        if(data.length() == str.length()) return;

        // create X movies (x = list.size()). Each movie with it's own data
        for(String[] s : list) {
            Movie m = new Movie();
            m.setName(s[1].replaceAll("~", ","));
            m.setDescription(s[2].replaceAll("~", ","));
            m.setPremiere(s[3].replaceAll("~", ","));
            m.setCategory(s[4].replaceAll("~", ","));
            m.setLength(Integer.parseInt(s[5]));
            m.setAgelimit(Integer.parseInt(s[6]));

            //?v=
            String movieUrl = s[7];
            if(movieUrl.contains("&") && movieUrl.contains("?v="))
                movieUrl = movieUrl.substring(movieUrl.indexOf("?v=")+3, movieUrl.indexOf('&'));
            else if(movieUrl.contains("?v="))
                movieUrl = movieUrl.substring(movieUrl.indexOf("?v=")+3);
            else if(movieUrl.contains(".be/"))
                movieUrl = movieUrl.substring(movieUrl.indexOf(".be/")+4);
            else
                movieUrl = "error";
            Log.d(TAG, "movieUrl: " + movieUrl);
            m.setTrailer(movieUrl);

            m.setCinemaCity(s[8]);
            m.setYesPlanet(s[9]);
            Bitmap bitmap = Methods.getBitmapFromURL(Analyze.instance.getString(R.string.server_address) + "/pictures/" + s[10].replaceAll(" ", "_") + ".JPG");
            Bitmap posterBitmap = Bitmap.createScaledBitmap(bitmap, GlobalVars.posterWidth, GlobalVars.posterHeight, false);
            m.setPoster(posterBitmap);

            // Add the movie to the arraylist.
            GlobalVars.movies.add(m);
        }
        // SET UP FAVORITES
        GlobalVars.phone = new Phone();
        setUpNewestMovies();
        Log.d("= GLOBAL VARS MOVIES =", GlobalVars.movies.toString());
    }

    public void setUpNewestMovies(){
        final int size = GlobalVars.movies.size();
        final int last = size - 1;

        final ArrayList<Movie> result = new ArrayList<Movie>();

        // iterate through the list in reverse order and append to the result
        for (int i = last; i >= 0; --i) {
            final Movie movie = GlobalVars.movies.get(i);
            result.add(movie);
        }

        GlobalVars.moviesNewest = result;
    }

    public static ArrayList<String[]> analyzeData(String dataString) {
        // Analyze the data received from the server.

        String str = "Connection success {\"response\":\"\"}";
        if(dataString.length() == str.length()) return null;

        ArrayList<String[]> dataArray = new ArrayList<String[]>();
        dataString.replaceAll("~", ",");

        String newData = "";
        char[] chars = dataString.toCharArray();
        for(char c : chars) {
            if(c != '\\') {
                newData += c;
            }
        }

        Log.d("newData: ", newData);

        String[] lines = newData.split(";"); // name: one, description: x; name: two, description xx   ->   lines[0] = name: one, description: x    &   lines[1] = name: two, description: xx
        if (lines.length == 0) return dataArray; // no data
        String[] singleLine = new String[11]; // amount of data received.

        for (int i = 0; i < lines.length; i++) {
            singleLine = lines[i].split(", "); // lines[0] = name: one, description: x  ->  signleLine[0] = name: one   &   singleLine[1] = description: x
            for (int j = 0; j < singleLine.length; j++) {
                if(!singleLine[j].contains(":")) continue;;

                String[] splittedLine = singleLine[j].split(": ", 2);  // singleLine[0] = name: one   ->   splittedLine[0] = name   &   splittedLine[1] = one
                for(int x = 0; x < splittedLine.length; x++)
                    Log.d("SPLITTED LINE " + x, splittedLine[x]);
                if(splittedLine[1] != null)
                    singleLine[j] = splittedLine[1]; // add only the splittedLine[1]
            }
            dataArray.add(i, singleLine);
        }

        // Log to see everything works.
        for(String[] s : dataArray) {
            for(int i = 0; i < s.length; i++) {
                Log.d("ANALYZE", "i = " + i + ", value = " + s[i]);
            }
        }

        dataArray.remove(dataArray.size()-1);
        // removing the last data received -> "}

        return dataArray;
    }

    public String getResult() {
        return this.result;
    }
}
