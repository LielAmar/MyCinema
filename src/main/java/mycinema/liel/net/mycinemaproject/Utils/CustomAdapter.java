package mycinema.liel.net.mycinemaproject.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mycinema.liel.net.mycinemaproject.Objects.Movie;
import mycinema.liel.net.mycinemaproject.R;

public class CustomAdapter extends ArrayAdapter<Movie> {

    private Context mContext;
    private ArrayList<Movie> arrayList;

    public CustomAdapter(@NonNull Context context, ArrayList<Movie> list) {
        super(context, 0, list);
        mContext = context;
        arrayList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null)
            v = LayoutInflater.from(mContext).inflate(R.layout.movie_view, parent, false);

        Movie movie = arrayList.get(position);

        ImageView moviePoster = v.findViewById(R.id.posterIV);
        moviePoster.setImageBitmap(movie.getPoster());

        TextView movieName = v.findViewById(R.id.movieNameTv);
        movieName.setText(movie.getName());

        TextView movieLength = v.findViewById(R.id.movieLengthTv);
        movieLength.setText(movie.getLengthWithMinutes());

        TextView movieCategory = v.findViewById(R.id.movieGenreTv);
        movieCategory.setText(movie.getCategory());

        return v;
    }
}