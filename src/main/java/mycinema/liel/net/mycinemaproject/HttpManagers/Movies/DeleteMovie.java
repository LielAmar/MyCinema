package mycinema.liel.net.mycinemaproject.HttpManagers.Movies;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Iterator;

import mycinema.liel.net.mycinemaproject.HttpManagers.Connector;
import mycinema.liel.net.mycinemaproject.MainActivity;
import mycinema.liel.net.mycinemaproject.MovieActivity;

public class DeleteMovie extends AsyncTask<Void, Void, String> {

    private static final String TAG = "DeleteMovie";

    private String address;

    private String name, email, password;

    private MovieActivity ma;

    public DeleteMovie(MovieActivity ma, String address, String name, String email, String password) {
        this.address = address;

        this.ma = ma;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.deleteMovie();
    }

    @Override
    protected void onPostExecute (String response) {
        super.onPostExecute(response);

        if(response != null) {
            if(!response.toLowerCase().contains("deleted"))
                Snackbar.make(MainActivity.instance.findViewById(android.R.id.content), "Delete failed", Snackbar.LENGTH_SHORT).show();
            else
                Snackbar.make(MainActivity.instance.findViewById(android.R.id.content), "Movie Deleted", Snackbar.LENGTH_SHORT).show();
            ma.finish();
            Log.d(TAG, "*connect success* " + response);
        } else {
            Log.d(TAG, "*connect no success* " + response);
        }
    }

    private String deleteMovie(){
        HttpURLConnection connection = Connector.connect(address);

        if(connection == null) {
            return null;
        }

        try {
            OutputStream os = connection.getOutputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bw.write(packDeleteData(name, email, password));

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

    public String packDeleteData(String name, String email, String password){
        JSONObject data = new JSONObject();
        StringBuffer packedData = new StringBuffer();

        try {
            data.put("name", name);
            data.put("email", email);
            data.put("password", password);

            Iterator it = data.keys();

            boolean fv = true;

            do {
                String key = it.next().toString();
                String value = data.get(key).toString();

                if (fv)
                    fv = false;
                else
                    packedData.append("&");

                packedData.append(URLEncoder.encode(key, "UTF-8"));
                packedData.append("=");
                packedData.append(URLEncoder.encode(value, "UTF-8"));

            }while (it.hasNext());

            return packedData.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("UserData", "JSONException");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("UserData", "UnsupportedEncodingException");
        }
        return null;
    }
}
