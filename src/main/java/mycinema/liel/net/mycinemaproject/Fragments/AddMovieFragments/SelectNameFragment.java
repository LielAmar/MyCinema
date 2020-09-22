package mycinema.liel.net.mycinemaproject.Fragments.AddMovieFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import mycinema.liel.net.mycinemaproject.R;

public class SelectNameFragment extends Fragment {

    private static final String TAG = "SelectNameFragment";


    public static EditText name = null;

    public SelectNameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_select_name, container, false);

        name = v.findViewById(R.id.addMovieNameEditText);

        return v;
    }

}
