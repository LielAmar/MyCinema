package mycinema.liel.net.mycinemaproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import mycinema.liel.net.mycinemaproject.Analyze;
import mycinema.liel.net.mycinemaproject.HttpManagers.Movies.GetMovies;
import mycinema.liel.net.mycinemaproject.MainActivity;
import mycinema.liel.net.mycinemaproject.MovieActivity;
import mycinema.liel.net.mycinemaproject.R;
import mycinema.liel.net.mycinemaproject.Utils.CustomAdapter;
import mycinema.liel.net.mycinemaproject.Utils.GlobalVars;

public class FavoritesFragment extends Fragment {

    private static final String TAG = "FavoritesFragment";

    private CustomAdapter moviesAdapter;
    private ListView moviesLV;

    public SwipeRefreshLayout srl;
    public FrameLayout frameLayoutFavorties;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_favorites, container, false);

        moviesLV = v.findViewById(R.id.favoriteMoviesListView);
        moviesLV.setDivider(null);
        moviesLV.setDividerHeight(0);
        moviesAdapter = new CustomAdapter(MainActivity.instance, GlobalVars.phone.getFavorites());
        moviesLV.setAdapter(moviesAdapter);
        moviesLV.setClickable(true);

        moviesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Starting Movie Activity");
                Intent intent = new Intent(getContext(), MovieActivity.class);
                intent.putExtra("movie", GlobalVars.phone.getFavorites().get(position).getName());
                startActivity(intent);
                Log.d(TAG, "Started Movie Activity");
            }
        });

        srl = v.findViewById(R.id.swipeLayoutFavorites);
        frameLayoutFavorties = v.findViewById(R.id.frameLayoutFavorties);

        srl.setColorSchemeResources(R.color.refresh, R.color.refresh, R.color.refresh);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                if(isConnected()) {
                    String result = "";
                    GetMovies getter = new GetMovies(getString(R.string.getMoviesWebPhp), result, true);
                    getter.execute();
                } else {
                    Snackbar.make(v.findViewById(R.id.swipeLayout), "No Connection.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        int color =  R.color.backgrounddark;
        if(Analyze.instance.sp.getBoolean("darktheme", false))
            frameLayoutFavorties.setBackgroundColor(getResources().getColor(color));

        return v;
    }

    public void reloadMovies() {
        moviesLV.setDivider(null);
        moviesLV.setDividerHeight(0);
        moviesAdapter = new CustomAdapter(MainActivity.instance, GlobalVars.phone.getFavorites());
        moviesLV.setAdapter(moviesAdapter);
        moviesLV.setClickable(true);
    }

    public void reload(Boolean isDark) {
        if(isDark) {
            int color =  R.color.backgrounddark;
            frameLayoutFavorties.setBackgroundColor(getResources().getColor(color));
        } else {
            int color =  R.color.background;
            frameLayoutFavorties.setBackgroundColor(getResources().getColor(color));
        }
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)MainActivity.instance.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }

}
