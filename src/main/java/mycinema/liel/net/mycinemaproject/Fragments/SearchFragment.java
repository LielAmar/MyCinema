package mycinema.liel.net.mycinemaproject.Fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import mycinema.liel.net.mycinemaproject.Analyze;
import mycinema.liel.net.mycinemaproject.HttpManagers.Movies.GetMovies;
import mycinema.liel.net.mycinemaproject.MainActivity;
import mycinema.liel.net.mycinemaproject.Objects.Movie;
import mycinema.liel.net.mycinemaproject.R;
import mycinema.liel.net.mycinemaproject.Utils.CustomAdapter;
import mycinema.liel.net.mycinemaproject.Utils.GlobalVars;
import mycinema.liel.net.mycinemaproject.Utils.Methods;

public class SearchFragment extends Fragment implements EditText.OnEditorActionListener{

    private static final String TAG = "SearchFragment";
    private ArrayList<Movie> searchMovies;

    public SwipeRefreshLayout srl;
    public FrameLayout frameLayoutSearch;

    private CustomAdapter moviesAdapter;
    private EditText search;
    private ListView moviesLV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_search, container, false);

        search = v.findViewById(R.id.searchText);
        moviesLV = v.findViewById(R.id.searchMoviesListView);
        searchMovies = new ArrayList<Movie>();

        search.setOnEditorActionListener(this);

        srl = v.findViewById(R.id.swipeLayoutSearch);
        frameLayoutSearch = v.findViewById(R.id.frameLayoutSearch);

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
            frameLayoutSearch.setBackgroundColor(getResources().getColor(color));

        return v;
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if(actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || keyEvent.getAction() == keyEvent.ACTION_DOWN
                || keyEvent.getAction() == keyEvent.KEYCODE_ENTER) {
            searchMovies();
            Methods.hideKeyboardForMain();
        }
        return false;
    }

    public void searchMovies(){
        searchMovies.clear();
        for(Movie m : GlobalVars.movies) {
            if(m.getName().toLowerCase().contains(search.getText().toString().toLowerCase())) {
                searchMovies.add(m);
                Log.d(TAG, "added movie: " + m.toString());
            }
        }
        moviesLV.setDivider(null);
        moviesLV.setDividerHeight(0);
        moviesAdapter = new CustomAdapter(MainActivity.instance, searchMovies);
        moviesLV.setAdapter(moviesAdapter);
    }

    public void reload(Boolean isDark) {
        if(isDark) {
            int color =  R.color.backgrounddark;
            frameLayoutSearch.setBackgroundColor(getResources().getColor(color));
        } else {
            int color =  R.color.background;
            frameLayoutSearch.setBackgroundColor(getResources().getColor(color));
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
