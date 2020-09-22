package mycinema.liel.net.mycinemaproject.Objects;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

public class User {

    private String email;
    private String username;
    private String password;
    private String type;

    public User(){}

    public User(String email, String username, String password, String type) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String packLoginData(){
        JSONObject data = new JSONObject();
        StringBuffer packedData = new StringBuffer();

        try {
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

    public String packData(){
        JSONObject data = new JSONObject();
        StringBuffer packedData = new StringBuffer();

        try {
            data.put("email", email);
            data.put("username", username);
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
