package mycinema.liel.net.mycinemaproject.HttpManagers.Users;

import android.content.Intent;
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

import mycinema.liel.net.mycinemaproject.AddAdminActivity;
import mycinema.liel.net.mycinemaproject.Analyze;
import mycinema.liel.net.mycinemaproject.Fragments.ProfileFragment;
import mycinema.liel.net.mycinemaproject.HttpManagers.Connector;
import mycinema.liel.net.mycinemaproject.MainActivity;
import mycinema.liel.net.mycinemaproject.Objects.User;
import mycinema.liel.net.mycinemaproject.R;
import mycinema.liel.net.mycinemaproject.Utils.GlobalVars;
import mycinema.liel.net.mycinemaproject.Utils.Methods;

public class AddAdmin extends AsyncTask<Void, Void, String> {

    private static final String TAG = "LoginUser";

    private View v;
    private String address;
    private String email, password, otherEmail;

    public AddAdmin(String address, String email, String password, String otherEmail) {
        this.address = address;
        this.email = email;
        this.password = password;
        this.otherEmail = otherEmail;

        v = Analyze.instance.findViewById(android.R.id.content);
        if(v == null)
            v = MainActivity.instance.findViewById(android.R.id.content);
        if(v == null)
            v = AddAdminActivity.instance.findViewById(android.R.id.content);
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
                Log.d(TAG, "Failed - Your credentials are wrong! " + Analyze.instance.sp.getString("email", "x"));
                Snackbar.make(v,  " Your credentials are wrong!", Snackbar.LENGTH_SHORT).show();
            } else {
                if (response.toLowerCase().contains("perm")) {
                    Log.d(TAG, "Failed - No permissions!");
                    Snackbar.make(v, " NO PERMISSIONS!", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (response.toLowerCase().contains("not found")) {
                        Log.d(TAG, "Failed - User wasn't found");
                        Snackbar.make(v, email + " wasn't found", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "Done");
                        Snackbar.make(v, email + " is now an Admin!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            Log.d(TAG, "Login failed, response: " + response);
            Snackbar.make(v,  "couldn't connect to the server!", Snackbar.LENGTH_SHORT).show();
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
            bw.write(Methods.packAddAdminData(email, password, otherEmail));

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
