package mycinema.liel.net.mycinemaproject.Objects;

import android.content.Context;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

import mycinema.liel.net.mycinemaproject.Analyze;
import mycinema.liel.net.mycinemaproject.MainActivity;
import mycinema.liel.net.mycinemaproject.MovieActivity;
import mycinema.liel.net.mycinemaproject.Utils.GlobalVars;

public class Phone {

    private ArrayList<Movie> favorites;
    private String data;

    public Phone() {
        loadFavorites();
        data = "";
    }

    public void loadFavorites(){
        this.favorites = new ArrayList<Movie>();

        String[] names = null;
        String analyze;

        FileInputStream inputStream;

        try {
            inputStream = Analyze.instance.openFileInput(GlobalVars.dataName);

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            analyze = new String(buffer);
            names = analyze.split("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(names == null) return;

        for(Movie m : GlobalVars.movies) {
            for(int i = 0; i < names.length; i++) {
                if(m.getName().equalsIgnoreCase(names[i]))
                    favorites.add(m);
            }
        }

    }

    public void setData(String data){
        this.data = data;
    }

    public String getData() {
        return this.data;
    }

    public boolean isFavorite(Movie movie) {
        return favorites.contains(movie);
    }

    public void addFavorite(Movie movie) {
        if(!favorites.contains(movie))
            this.favorites.add(movie);
    }

    public void removeFavorite(Movie movie) {
        if(favorites.contains(movie))
            this.favorites.remove(movie);
        if(MainActivity.instance.favoritesFragment == null) return;
        MainActivity.instance.favoritesFragment.reloadMovies();
    }

    public void saveFavorites() {



    }

    public ArrayList<Movie> getFavorites() {
        return this.favorites;
    }
}
