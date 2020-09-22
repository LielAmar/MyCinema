package mycinema.liel.net.mycinemaproject.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import java.util.ArrayList;

import mycinema.liel.net.mycinemaproject.Objects.Movie;
import mycinema.liel.net.mycinemaproject.Objects.Phone;
import mycinema.liel.net.mycinemaproject.Objects.User;

public class GlobalVars {

    private static final double ratio = 1.4509576320371445153801508995937;
    public static String dataName = "data";

    public static int screenWidth, screenHeight;
    public static int posterHeight, posterWidth;
    public static ArrayList<Movie> movies = new ArrayList<Movie>();
    public static ArrayList<Movie> moviesNewest = new ArrayList<Movie>();
    public static User user;
    public static Phone phone;

    public static void setUpSize(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        posterHeight = screenHeight/3;
        posterWidth = (int) (posterHeight/ratio);
    }
}
