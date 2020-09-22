package mycinema.liel.net.mycinemaproject.Fragments.AddMovieFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import mycinema.liel.net.mycinemaproject.R;

public class SelectDescriptionFragment extends Fragment {

    private static final String TAG = "SelectDescriptionFragment";

    public static EditText description = null;

    public SelectDescriptionFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_select_description, container, false);

        description = v.findViewById(R.id.addMovieDescriptionEditText);

        return v;
    }

}
