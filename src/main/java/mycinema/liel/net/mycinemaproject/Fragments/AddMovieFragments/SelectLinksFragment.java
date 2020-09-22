package mycinema.liel.net.mycinemaproject.Fragments.AddMovieFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import mycinema.liel.net.mycinemaproject.R;

public class SelectLinksFragment extends Fragment {

    private static final String TAG = "SelectLinksFragment";

    public static EditText trailer = null;
    public static EditText cinemaCity = null;
    public static EditText yesPlanet = null;

    public SelectLinksFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_select_links, container, false);

        trailer = v.findViewById(R.id.addMovieTrailerEditText);
        cinemaCity = v.findViewById(R.id.addMovieCinemaCityEditText);
        yesPlanet = v.findViewById(R.id.addMovieYesPlanetEditText);

        return v;
    }

}
