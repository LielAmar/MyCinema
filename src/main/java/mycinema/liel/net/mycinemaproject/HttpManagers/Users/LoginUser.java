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

import mycinema.liel.net.mycinemaproject.Analyze;
import mycinema.liel.net.mycinemaproject.Fragments.ProfileFragment;
import mycinema.liel.net.mycinemaproject.HttpManagers.Connector;
import mycinema.liel.net.mycinemaproject.MainActivity;
import mycinema.liel.net.mycinemaproject.Objects.User;
import mycinema.liel.net.mycinemaproject.R;
import mycinema.liel.net.mycinemaproject.Utils.GlobalVars;

public class LoginUser extends AsyncTask<Void, Void, String> {

    private static final String TAG = "LoginUser";

    private View v;
    private String address;
    private String email, password;
    private Boolean logedInManually;

    public LoginUser(String address, String email, String password, boolean logedInManually) {
        this.address = address;
        this.email = email;
        this.password = password;
        this.logedInManually = logedInManually;

        v = Analyze.instance.findViewById(android.R.id.content);
        if(v == null)
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
            if (response.toLowerCase().contains("failed")) {
                Log.d(TAG, "Log In Failed! Email: " + Analyze.instance.sp.getString("email", "x"));
                if(Analyze.instance.sp.getString("email", "x").equals("x"))
                    Snackbar.make(v, "The entered Email or password is wrong/The user does not exists!", Snackbar.LENGTH_SHORT).show();
                else {
                    Log.d(TAG, "Log In Failed doe to wrong credentials");
                    Snackbar.make(v, "Logged out dou to wrong credentials!", Snackbar.LENGTH_SHORT).show();
                    SharedPreferences.Editor edit = Analyze.instance.sp.edit();
                    edit.putString("email", "x");
                    edit.putString("password", "x");
                    edit.commit();
                    edit.apply();
                }
            } else {
                Log.d(TAG, "Log In success");
                if(Analyze.instance.sp.getString("email", "x").equals("x"))
                    Snackbar.make(v, "Logging in", Snackbar.LENGTH_SHORT).show();

                // save user information
                SharedPreferences.Editor edit = Analyze.instance.sp.edit();
                edit.putString("email", email);
                edit.putString("password", password);
                edit.commit();
                edit.apply();
                Log.d(TAG, "getUsername: " + getUsername(response) + ", getType: " + getType(response));
                GlobalVars.user = new User(email, getUsername(response), password, getType(response));
            }
            if(logedInManually) {
                ProfileFragment profileFragment = new ProfileFragment();
                FragmentManager fm = MainActivity.instance.getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.mainActivityFrameLayout, profileFragment).commit();
            }
            Log.d(TAG, "Login success, response: " + response);
        } else {
            Log.d(TAG, "Login failed, response: " + response);

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
            bw.write(new User(email, "x", password, "x").packLoginData());

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

    public String getUsername(String result) {
        String response = result;

        String[] values = response.split(",");
        //{"response":"ok","email":null,"username":"","type":"user"}  ->  {"response":"ok" & "email":null & "username":"" & "type":"user"}
        String username = "";

        for(int i = 0; i < values.length; i++) {
            String[] keyValue = values[i].split("\":\"");
            //"username":""  ->  "username & null"
            for(int j = 0; j < keyValue.length; j++) {
                if(keyValue[0].contains("username")) {
                    username = keyValue[1];
                    Log.d(TAG, "username is now equals to " + username);
                    username = username.substring(0, username.length() - 1); // remove the " in the end
                }
            }
        }
        return username;
    }

    public String getType(String result) {
        String response = result;

        String[] values = response.split(",");
        String type = "";

        for(int i = 0; i < values.length; i++) {
            String[] keyValue = values[i].split("\":\"");
            for(int j = 0; j < keyValue.length; j++) {
                type = keyValue[1];
            }
        }

        if(type.contains("admin"))
            return "admin";
        else
            return "user";
    }
}
