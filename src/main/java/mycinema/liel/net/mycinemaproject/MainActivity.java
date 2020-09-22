package mycinema.liel.net.mycinemaproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import android.widget.LinearLayout;

import java.io.FileOutputStream;

import mycinema.liel.net.mycinemaproject.Fragments.FavoritesFragment;
import mycinema.liel.net.mycinemaproject.Fragments.LoginFragment;
import mycinema.liel.net.mycinemaproject.Fragments.MoviesFragment;
import mycinema.liel.net.mycinemaproject.Fragments.NewestFragment;
import mycinema.liel.net.mycinemaproject.Fragments.ProfileFragment;
import mycinema.liel.net.mycinemaproject.Fragments.SearchFragment;
import mycinema.liel.net.mycinemaproject.Objects.Movie;
import mycinema.liel.net.mycinemaproject.Utils.BroadcastReceiverManager;
import mycinema.liel.net.mycinemaproject.Utils.GlobalVars;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";

    public static MainActivity instance;
    public LinearLayout activityMain;

    private String currentFragment;
    private BottomNavigationView navigation;

    public BroadcastReceiverManager brm;

    public MoviesFragment moviesFragment;
    public NewestFragment newestFragment;
    public FavoritesFragment favoritesFragment;
    public LoginFragment loginFragment;
    public ProfileFragment profileFragment;
    public SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        instance = this;
        activityMain = findViewById(R.id.activityMain);

        // Notifications
        Intent intent = new Intent(this, AppService.class);
        startService(intent);

        // Airplane mode
        brm = new BroadcastReceiverManager();

        // Navigation
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        int color = R.color.backgrounddark;
        if (Analyze.instance.sp.getBoolean("darktheme", false)) {
            activityMain.setBackgroundColor(getResources().getColor(color));
            navigation.setBackgroundColor(getResources().getColor(color));
            getWindow().setNavigationBarColor(getResources().getColor(color));
        }
        setUpToolbar(Analyze.instance.sp.getBoolean("darktheme", false));

        moviesFragment = new MoviesFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.mainActivityFrameLayout, moviesFragment).commit();
        currentFragment = "MoviesFragment";
        Log.d(TAG, "MoviesFragment activated");
    }

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(brm, filter);

        super.onResume();
    }

    @Override
    protected void onPause() {
        String list = "";
        for (Movie m : GlobalVars.phone.getFavorites()) {
            list += m.getName() + "\n";
        }
        // WORKING 'TILL HERE

        String filename = GlobalVars.dataName;
        FileOutputStream outputStream;
        try {
            outputStream = MainActivity.instance.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(list.getBytes());
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        GlobalVars.phone.saveFavorites();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.navigation_home) {
            if (!currentFragment.equals("MoviesFragment")) {
                moviesFragment = new MoviesFragment();
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.mainActivityFrameLayout, moviesFragment).commitAllowingStateLoss();
                currentFragment = "MoviesFragment";
                Log.d(TAG, "MoviesFragment activated");
                return true;
            }
            return true;
        } else if (item.getItemId() == R.id.navigation_profile) {
            if (!currentFragment.equals("ProfileFragment")) {
                if (GlobalVars.user == null) {
                    loginFragment = new LoginFragment();
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.mainActivityFrameLayout, loginFragment).commitAllowingStateLoss();
                    currentFragment = "ProfileFragment";
                    Log.d(TAG, "Login activated");
                    return true;
                }
                if (!currentFragment.equals("ProfileFragment")) {
                    profileFragment = new ProfileFragment();
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.mainActivityFrameLayout, profileFragment).commitAllowingStateLoss();
                    currentFragment = "ProfileFragment";
                    Log.d(TAG, "Profile activated");
                    return true;
                }
                return true;
            }
        } else if (item.getItemId() == R.id.navigation_search) {
            if (!currentFragment.equals("SearchFragment")) {
                searchFragment = new SearchFragment();
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.mainActivityFrameLayout, searchFragment).commitAllowingStateLoss();
                currentFragment = "SearchFragment";
                Log.d(TAG, "SearchFragment activated");
                return true;
            }
            return true;
        } else if (item.getItemId() == R.id.navigation_newest) {
            if (!currentFragment.equals("NewestFragment")) {
                newestFragment = new NewestFragment();
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.mainActivityFrameLayout, newestFragment).commitAllowingStateLoss();
                currentFragment = "NewestFragment";
                Log.d(TAG, "NewestFragment activated");
                return true;
            }
            return true;
        } else if (item.getItemId() == R.id.navigation_favorites) {
            if (!currentFragment.equals("FavoritesFragment")) {
                favoritesFragment = new FavoritesFragment();
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.mainActivityFrameLayout, favoritesFragment).commitAllowingStateLoss();
                currentFragment = "FavoritesFragment";
                Log.d(TAG, "FavoritesFragment activated");
                return true;
            }
            return true;
        }
        return true;
    }

    public void reloadFragment() {
        if (currentFragment.equals("MoviesFragment")) {
            moviesFragment = new MoviesFragment();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.mainActivityFrameLayout, moviesFragment).commitAllowingStateLoss();
            currentFragment = "MoviesFragment";
            Log.d(TAG, "MoviesFragment activated");
        } else if (currentFragment.equals("ProfileFragment")) {
            if (GlobalVars.user == null) {
                loginFragment = new LoginFragment();
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.mainActivityFrameLayout, loginFragment).commitAllowingStateLoss();
                currentFragment = "ProfileFragment";
                Log.d(TAG, "Login activated");
            } else {
                profileFragment = new ProfileFragment();
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.mainActivityFrameLayout, profileFragment).commitAllowingStateLoss();
                currentFragment = "ProfileFragment";
                Log.d(TAG, "Profile activated");
            }
        } else if (currentFragment.equals("SearchFragment")) {
            searchFragment = new SearchFragment();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.mainActivityFrameLayout, searchFragment).commitAllowingStateLoss();
            currentFragment = "SearchFragment";
            Log.d(TAG, "SearchFragment activated");
        } else if (currentFragment.equals("NewestFragment")) {
            newestFragment = new NewestFragment();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.mainActivityFrameLayout, newestFragment).commitAllowingStateLoss();
            currentFragment = "NewestFragment";
            Log.d(TAG, "NewestFragment activated");
        } else if (currentFragment.equals("FavoritesFragment")) {
            favoritesFragment = new FavoritesFragment();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.mainActivityFrameLayout, favoritesFragment).commitAllowingStateLoss();
            currentFragment = "FavoritesFragment";
            Log.d(TAG, "FavoritesFragment activated");
        }
    }

    public void setUpToolbar(boolean isDark) {
        Toolbar toolbar = findViewById(R.id.mainActivityActionBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        if (isDark) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.backgrounddark));
        } else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.background));
        }

    }

    public void reload(Boolean isDark) {
        if (isDark) {
            int color = R.color.backgrounddark;
            activityMain.setBackgroundColor(getResources().getColor(color));
            navigation.setBackgroundColor(getResources().getColor(color));


            if (currentFragment.equalsIgnoreCase("MoviesFragment"))
                moviesFragment.reload(isDark);
            else if (currentFragment.equalsIgnoreCase("ProfileFragment")) {
                profileFragment.reload(isDark);
            } else if (currentFragment.equalsIgnoreCase("SearchFragment"))
                searchFragment.reload(isDark);
            else if (currentFragment.equalsIgnoreCase("NewestFragment"))
                newestFragment.reload(isDark);
            else if (currentFragment.equalsIgnoreCase("FavoritesFragment"))
                favoritesFragment.reload(isDark);
        } else {
            int color = R.color.background;
            activityMain.setBackgroundColor(getResources().getColor(color));
            navigation.setBackgroundColor(getResources().getColor(color));


            if (currentFragment.equalsIgnoreCase("MoviesFragment"))
                moviesFragment.reload(isDark);
            else if (currentFragment.equalsIgnoreCase("ProfileFragment")) {
                profileFragment.reload(isDark);
            } else if (currentFragment.equalsIgnoreCase("SearchFragment"))
                searchFragment.reload(isDark);
            else if (currentFragment.equalsIgnoreCase("NewestFragment"))
                newestFragment.reload(isDark);
            else if (currentFragment.equalsIgnoreCase("FavoritesFragment"))
                favoritesFragment.reload(isDark);
        }
        setUpToolbar(isDark);
    }
}