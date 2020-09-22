package mycinema.liel.net.mycinemaproject.Fragments.AddMovieFragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

import mycinema.liel.net.mycinemaproject.AddMovieActivity;
import mycinema.liel.net.mycinemaproject.R;

public class SelectPremiereFragment extends Fragment {

    private static final String TAG = "SelectPremiereFragment";

    public static String premiere;

    private Button selectDate;
    private DatePickerDialog.OnDateSetListener dpdListener;

    public SelectPremiereFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_select_premiere, container, false);

        selectDate = v.findViewById(R.id.addMovieDateButton);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == selectDate) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            AddMovieActivity.instance,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            dpdListener,
                            year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            }
        });

        dpdListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date = day + "/" + month + "/" + year;
                SelectPremiereFragment.premiere = date;
                AddMovieActivity.instance.next.setVisibility(View.VISIBLE);
            }
        };

        return v;
    }

}
