package mycinema.liel.net.mycinemaproject.HttpManagers.Movies;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import mycinema.liel.net.mycinemaproject.HttpManagers.Connector;
import mycinema.liel.net.mycinemaproject.Utils.NameValuePair;

public class UploadImage extends AsyncTask<Void, Void, Void> {

    private String address;
    private Bitmap image;
    private String name;

    public UploadImage(String address, Bitmap image, String name){
        this.address = address;
        this.image = image;
        this.name = name;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        ArrayList<NameValuePair> dataToSend = new ArrayList<NameValuePair>();
        dataToSend.add(new NameValuePair("image", encodedImage));
        dataToSend.add(new NameValuePair("name", name.replaceAll(" ", "_")));

        send(dataToSend);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    private String send(ArrayList<NameValuePair> dataToSend){
        HttpURLConnection connection = Connector.connect(address);

        if(connection == null) {
            return null;
        }

        try {
            OutputStream os = connection.getOutputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bw.write(packData(dataToSend));

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
            Log.e("UploadImage", "UnsupportedEncodingException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("UploadImage", "IOException");
        }

        return null;
    }

    public String packData(ArrayList<NameValuePair> dataToSend){
        JSONObject data = new JSONObject();
        StringBuffer packedData = new StringBuffer();

        try {
            for(NameValuePair nvp : dataToSend) {
                data.put(nvp.getName(), nvp.getValue());
            }

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
            Log.e("UploadImage", "JSONException");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("UploadImage", "UnsupportedEncodingException");
        }
        return null;
    }
}
