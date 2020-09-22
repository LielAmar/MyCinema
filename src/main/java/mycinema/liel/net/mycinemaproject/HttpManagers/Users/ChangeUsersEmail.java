package mycinema.liel.net.mycinemaproject.HttpManagers.Users;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
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
import mycinema.liel.net.mycinemaproject.HttpManagers.Connector;
import mycinema.liel.net.mycinemaproject.Utils.GlobalVars;
import mycinema.liel.net.mycinemaproject.Utils.Methods;

public class ChangeUsersEmail extends AsyncTask<Void, Void, String> {

    private static final String TAG = "ChangeUsersEmail";

    private View v;
    private String address;
    private String email, newEmail, password;

    public ChangeUsersEmail(String address, String email, String password, String newEmail) {
        this.address = address;
        this.email = email;
        this.password =password;
        this.newEmail = newEmail;
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.sendUser();
    }

    @Override
    protected void onPostExecute (String response) {
        super.onPostExecute(response);

        if(response != null) {
            if (response.toLowerCase().contains("failed")) {
                Log.d(TAG, "Failed - Email not found! Email: " + Analyze.instance.sp.getString("email", "x"));
            } else {
                Log.d(TAG, "Email changed successfully");
                if(Analyze.instance.sp.getString("email", "x").equals("x"))
                    Snackbar.make(v, "Logging in", Snackbar.LENGTH_SHORT).show();

                // save user information
                SharedPreferences.Editor edit = Analyze.instance.sp.edit();
                edit.putString("email", newEmail);
                edit.commit();
                edit.apply();
                GlobalVars.user.setEmail(newEmail);
            }
        } else {
            Log.d(TAG, "Response is null: " + response);
        }
    }

    private String sendUser(){
        Log.d(TAG, "0");
        HttpURLConnection connection = Connector.connect(address);

        if(connection == null) {
            Log.d(TAG, "1");
            return null;
        }
        Log.d(TAG, "2");
        try {
            Log.d(TAG, "3");
            OutputStream os = connection.getOutputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bw.write(Methods.packChangeEmailData(email, password, newEmail));
            Log.d(TAG, "4");
            Log.d(TAG, "5: " + Methods.packChangePasswordData(email, password, newEmail));
            bw.flush();
            bw.close();
            os.close();

            int responseCode = connection.getResponseCode();

            if(responseCode == connection.HTTP_OK) {
                Log.d(TAG, "6");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer response = new StringBuffer();
                Log.d(TAG, "7");
                String line;
                while((line=br.readLine()) != null) {
                    response.append(line);
                }
                br.close();
                Log.d(TAG, "8");
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
