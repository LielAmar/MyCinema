package mycinema.liel.net.mycinemaproject.HttpManagers.Users;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import mycinema.liel.net.mycinemaproject.Analyze;
import mycinema.liel.net.mycinemaproject.Fragments.ProfileFragment;
import mycinema.liel.net.mycinemaproject.HttpManagers.Connector;
import mycinema.liel.net.mycinemaproject.MainActivity;
import mycinema.liel.net.mycinemaproject.Objects.User;
import mycinema.liel.net.mycinemaproject.R;
import mycinema.liel.net.mycinemaproject.Utils.GlobalVars;

public class RegisterUser extends AsyncTask<Void, Void, String> {

    private static final String TAG = "RegisterUser";

    private View v;
    private String address;
    private String email, username, password;

    public RegisterUser(String address, String email, String username, String password) {
        this.address = address;
        this.email = email;
        this.username = username;
        this.password = password;

        v = MainActivity.instance.findViewById(android.R.id.content);
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.sendUser();
    }

    @Override
    protected void onPostExecute (String response) {
        super.onPostExecute(response);

        if(response != null) {
            if(response.toLowerCase().contains("exist"))
                Snackbar.make(v, "Username/Email is already in use!", Snackbar.LENGTH_SHORT).show();
            else if(response.toLowerCase().contains("failed"))
                Snackbar.make(v, "Error! Please try again", Snackbar.LENGTH_SHORT).show();
            else {
                Snackbar.make(v, "Registration Completed", Snackbar.LENGTH_SHORT).show();
                // save user information
                SharedPreferences.Editor edit = Analyze.instance.sp.edit();
                edit.putString("email", email);
                edit.putString("password", password);
                edit.commit();
                edit.apply();
                GlobalVars.user = new User(email, username, password, "user");
                ProfileFragment profileFragment = new ProfileFragment();
                FragmentManager fm = MainActivity.instance.getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.mainActivityFrameLayout, profileFragment).commit();
            }
            Log.d(TAG, "*success* " + response);
        } else {
            Log.d(TAG, "*no success* " + response);
        }
    }

    private String sendUser(){
        HttpURLConnection connection = Connector.connect(address);

        if(connection == null) {
            return null;
        }

        try {
            OutputStream os = connection.getOutputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bw.write(new User(email, username, password, "user").packData());

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
