package mycinema.liel.net.mycinemaproject.Fragments.AddMovieFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import mycinema.liel.net.mycinemaproject.R;

public class SelectOtherInfoFragment extends Fragment {

    private static final String TAG = "SelectOtherInfoFragment";

    public static EditText genre;
    public static EditText length;
    public static EditText ageLimit;

    public SelectOtherInfoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_select_other_info, container, false);

        genre = v.findViewById(R.id.addMovieGenreEditText);
        length = v.findViewById(R.id.addMovieLengthEditText);
        ageLimit = v.findViewById(R.id.addMovieAgeLimitEditText);

        return v;
    }

}
