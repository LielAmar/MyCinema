package mycinema.liel.net.mycinemaproject.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import mycinema.liel.net.mycinemaproject.Fragments.MoviesFragment;
import mycinema.liel.net.mycinemaproject.MainActivity;
import mycinema.liel.net.mycinemaproject.R;

public class Methods {

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isValidEmailAddress(String email) {
        EmailValidator ev = new EmailValidator();
        return (ev.validateEmail(email));
    }

    public static boolean isValidUsername(String username) {
        return username.matches("[a-zA-Z0-9]*") && username.length() > 3;
    }

    public static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        } else {
            char c;
            int count = 0;
            for (int i = 0; i < password.length(); i++) {
                c = password.charAt(i);
                if (!(Character.isLetterOrDigit(c) || c == '!' || c == '@' || c == '#' || c == '$' || c == '%' || c == '^' || c == '&'
                        || c == '*' || c == '(' || c == ')' || c == '+' || c == '-' || c == '=')) {
                    return false;
                } else if (Character.isDigit(c)) {
                    count++;
                }
            }
            if (count < 1)   {
                return false;
            }
        }
        return true;
    }

    public static String hash(String passwordToHash)
    {
        String generatedPassword = null;

        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static void loadEverything(){
        if(MainActivity.instance != null) {
            MoviesFragment mf = new MoviesFragment();
            FragmentTransaction fragmentTransaction = MainActivity.instance.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainActivityFrameLayout, mf);
            fragmentTransaction.commit();
        }
    }

    public static void hideKeyboardForMain(){
        if(!(MainActivity.instance == null)) {
            InputMethodManager input = (InputMethodManager) MainActivity.instance.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (MainActivity.instance.getCurrentFocus() != null)
                input.hideSoftInputFromWindow(MainActivity.instance.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }



    public static String packChangePasswordData(String email, String password, String newpassword){
        JSONObject data = new JSONObject();
        StringBuffer packedData = new StringBuffer();

        try {
            data.put("email", email);
            data.put("password", password);
            data.put("newpassword", newpassword);

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

    public static String packAddAdminData(String email, String password, String otheremail) {
        JSONObject data = new JSONObject();
        StringBuffer packedData = new StringBuffer();

        try {
            data.put("email", email);
            data.put("password", password);
            data.put("otheremail", otheremail);

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

    public static String packChangeEmailData(String email, String password, String newEmail){
        JSONObject data = new JSONObject();
        StringBuffer packedData = new StringBuffer();

        try {
            data.put("email", email);
            data.put("password", password);
            data.put("newemail", newEmail);

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
