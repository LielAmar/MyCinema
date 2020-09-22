package mycinema.liel.net.mycinemaproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import mycinema.liel.net.mycinemaproject.HttpManagers.Movies.GetMovies;
import mycinema.liel.net.mycinemaproject.HttpManagers.Users.LoginUser;
import mycinema.liel.net.mycinemaproject.Objects.Phone;
import mycinema.liel.net.mycinemaproject.Utils.GlobalVars;

public class Analyze extends AppCompatActivity {

    private static final String TAG = "LoadData";

    public static Analyze instance;

    public SharedPreferences sp;
    public Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        GlobalVars.setUpSize(this);
        instance = this;

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        } else {
            startApp();
        }
    }

    public void startApp() {
        if(!isConnected()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
//            Snackbar.make(MainActivity.instance.getCurrentFocus(), "You are not connected to the internet!", Snackbar.LENGTH_LONG).show();
            return;
        }

        if(GlobalVars.movies.isEmpty()) {
            String email = sp.getString("email", "x");
            String password = sp.getString("password", "x");

            Log.d(TAG, "Checking email & password");
            if (!email.equals("x") && !password.equals("x")) {
                Log.d(TAG, "Trying to log in");
                LoginUser lu = new LoginUser(getString(R.string.loginWebPhp), email, password, false);
                lu.execute();
            }

            Log.d(TAG, "Connecting to the server");
            String result = "";
            GetMovies getter = new GetMovies(getString(R.string.getMoviesWebPhp), result);
            getter.execute();
        } else {
            Intent intent = new Intent(Analyze.instance, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    final int MY_PERMISSIONS_REQUEST = 111;

    public void requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.VIBRATE)) {

        } else {
            boolean vibrate = ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED;
            boolean internet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
            boolean access_network_state = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED;
//            boolean read_external_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

            String[] perms = new String[3];
            for(int i = 0; i < perms.length; i++)
                perms[i] = "default";

            if(!vibrate)
                perms[0] = Manifest.permission.VIBRATE;
            if(!internet)
                perms[1] = Manifest.permission.INTERNET;
            if(!access_network_state)
                perms[2] = Manifest.permission.ACCESS_NETWORK_STATE;
//            if(!read_external_storage)
//                perms[3] = Manifest.permission.READ_EXTERNAL_STORAGE;

            ActivityCompat.requestPermissions(this, perms, MY_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                startApp();
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }
}