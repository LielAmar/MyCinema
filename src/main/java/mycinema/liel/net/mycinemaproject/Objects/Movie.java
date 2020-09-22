package mycinema.liel.net.mycinemaproject.Objects;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

public class Movie {

    private String name;
    private String description;
    private String premiere;
    private String category;
    private String trailer;
    private String cinemaCity;
    private String yesPlanet;

    private int id;
    private int length;
    private int agelimit;

    private Bitmap poster;

    public Movie(){ }

    public Movie(String name, String description, String premiere, String category, int length, int agelimit, String trailer, String cinemaCity, String yesPlanet, Bitmap poster) {

        this.name = name;
        this.description = description;
        this.premiere = premiere;
        this.category = category;
        this.length = length;
        this.agelimit = agelimit;
        this.trailer = trailer;
        this.cinemaCity = cinemaCity;
        this.yesPlanet = yesPlanet;
        this.poster = poster;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPremiere() {
        return premiere;
    }

    public void setPremiere(String premiere) {
        this.premiere = premiere;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getCinemaCity() {
        return cinemaCity;
    }

    public void setCinemaCity(String cinemaCity) {
        this.cinemaCity = cinemaCity;
    }

    public String getYesPlanet() {
        return yesPlanet;
    }

    public void setYesPlanet(String yesPlanet) {
        this.yesPlanet = yesPlanet;
    }

    public int getLength() {
        return length;
    }

    public String getLengthWithMinutes(){
        return length + " minutes";
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getAgelimit() {
        return agelimit;
    }

    public void setAgelimit(int agelimit) {
        this.agelimit = agelimit;
    }

    public Bitmap getPoster() {
        return poster;
    }

    public void setPoster(Bitmap poster) {
        this.poster = poster;
    }

    @Override
    public String toString(){
        return name + ", " + description + ", " + premiere + ", " + category + ", " + trailer + ", " + cinemaCity + ", " + yesPlanet + ", " + length + ", " + agelimit + ", " + poster.toString();
    }

    public String packData() {
        JSONObject data = new JSONObject();
        StringBuffer packedData = new StringBuffer();

        try {
            data.put("name", name.replaceAll(",", "~"));
            data.put("description", description.replaceAll(",", "~"));
            data.put("premiere", premiere);
            data.put("category", category);
            data.put("length", length);
            data.put("agelimit", agelimit);
            data.put("trailer", trailer);
            data.put("cinemaCity", cinemaCity);
            data.put("yesPlanet", yesPlanet);

            // saving the image name
            data.put("poster", name.replaceAll(" ", "_").replaceAll(",", "~"));

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

            } while (it.hasNext());

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
