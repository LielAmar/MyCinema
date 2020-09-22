package mycinema.liel.net.mycinemaproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import mycinema.liel.net.mycinemaproject.HttpManagers.Movies.DeleteMovie;
import mycinema.liel.net.mycinemaproject.Objects.Movie;
import mycinema.liel.net.mycinemaproject.Utils.GlobalVars;
import mycinema.liel.net.mycinemaproject.Utils.Methods;
import mycinema.liel.net.mycinemaproject.Utils.YoutubeManager;

public class MovieActivity extends YouTubeBaseActivity implements View.OnClickListener{

    public static MovieActivity instance;

    private Movie movie;
    private ImageView poster;

    private TextView name, description, premiere, category, length, ageLimit, yesPlanet, cinemaCity;
    private TextView categoryHeader, premiereHeader, lengthHeader, ageLimitHeader;
    private ImageButton addMovieToFavorite, removeMovieFromFavorite;
    private YouTubePlayerView trailer;
    private YouTubePlayer.OnInitializedListener mOnInitializedListener;

    private Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        instance = this;

        Intent intent = getIntent();
        delete = findViewById(R.id.movieDelete);
        delete.setVisibility(View.GONE);
        if(GlobalVars.user != null) {
            if(GlobalVars.user.getType().equalsIgnoreCase("admin")) {
                delete.setVisibility(View.VISIBLE);
            }
        }

        for(Movie m : GlobalVars.movies) {
            if(intent.getStringExtra("movie").equals(m.getName()))
                this.movie = m;
        }

        if(movie == null)
            finish();

        delete.setOnClickListener(this);

        Bitmap bitmap = movie.getPoster();
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, GlobalVars.posterWidth, GlobalVars.posterHeight, false);

        poster = findViewById(R.id.moviePoster);
        poster.setImageBitmap(newBitmap);

        name = findViewById(R.id.movieName);
        name.setText(movie.getName());

        description = findViewById(R.id.movieDescription);
        if(!movie.getDescription().startsWith("\"") && !movie.getDescription().endsWith("\""))
            description.setText("\"" + movie.getDescription() + "\"");
        else
            description.setText(movie.getDescription());

        premiereHeader = findViewById(R.id.moviePremiereHeader);
        premiereHeader.setText("Premiere date: ");
        premiere = findViewById(R.id.moviePremiere);
        premiere.setText(movie.getPremiere());

        categoryHeader = findViewById(R.id.movieCategoryHeader);
        categoryHeader.setText("Category: ");
        category = findViewById(R.id.movieCategory);
        category.setText(movie.getCategory());

        lengthHeader = findViewById(R.id.movieLengthHeader);
        lengthHeader.setText("Length: ");
        length = findViewById(R.id.movieLength);
        length.setText(movie.getLengthWithMinutes());

        ageLimitHeader = findViewById(R.id.movieAgeLimitHeader);
        ageLimitHeader.setText("Age Limit: ");
        ageLimit = findViewById(R.id.movieAgeLimit);
        ageLimit.setText(movie.getAgelimit() + "+");

        yesPlanet = findViewById(R.id.movieYesPlanet);
        yesPlanet.setOnClickListener(this);

        cinemaCity = findViewById(R.id.movieCinemaCity);
        cinemaCity.setOnClickListener(this);

        addMovieToFavorite = findViewById(R.id.addMovieToFavorite);
        removeMovieFromFavorite = findViewById(R.id.removeMovieFromFavorite);

        if(GlobalVars.phone.isFavorite(movie)) {
            removeMovieFromFavorite.setVisibility(View.VISIBLE);
            addMovieToFavorite.setVisibility(View.GONE);
        } else {
            removeMovieFromFavorite.setVisibility(View.GONE);
            addMovieToFavorite.setVisibility(View.VISIBLE);
        }

        addMovieToFavorite.setOnClickListener(this);
        removeMovieFromFavorite.setOnClickListener(this);

        trailer = findViewById(R.id.movieActivityTrailerVideo);
        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(movie.getTrailer());
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        trailer.initialize(YoutubeManager.getApiKey(), mOnInitializedListener);
    }

    @Override
    public void onClick(View view) {
        if(view == delete) {
            areYouSureDialog();
            return;
        }
        if(view == addMovieToFavorite) {
            addMovieToFavorite.setVisibility(View.GONE);
            removeMovieFromFavorite.setVisibility(View.VISIBLE);
            GlobalVars.phone.addFavorite(movie);
            return;
        } else if(view == removeMovieFromFavorite) {
            addMovieToFavorite.setVisibility(View.VISIBLE);
            removeMovieFromFavorite.setVisibility(View.GONE);
            GlobalVars.phone.removeFavorite(movie);
            return;
        } else if(view == cinemaCity) {
            String url = movie.getCinemaCity();
            if(!url.startsWith("http://"))
                url = "http://" + url;

            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        if(view == yesPlanet) {
            String url = movie.getYesPlanet();
            if(!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;

            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    public void areYouSureDialog(){
        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle("Are you sure?").setMessage("Are you sure you want to delete this movie?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMovie();
                    }
                });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                });
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    public void deleteMovie(){
        DeleteMovie dm = new DeleteMovie(this, getString(R.string.deleteMovieWebPhp), movie.getName(), GlobalVars.user.getEmail(), GlobalVars.user.getPassword());
        dm.execute();
    }
}
