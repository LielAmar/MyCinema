package mycinema.liel.net.mycinemaproject;

import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import mycinema.liel.net.mycinemaproject.Fragments.AddMovieFragments.SelectDescriptionFragment;
import mycinema.liel.net.mycinemaproject.Fragments.AddMovieFragments.SelectLinksFragment;
import mycinema.liel.net.mycinemaproject.Fragments.AddMovieFragments.SelectNameFragment;
import mycinema.liel.net.mycinemaproject.Fragments.AddMovieFragments.SelectOtherInfoFragment;
import mycinema.liel.net.mycinemaproject.Fragments.AddMovieFragments.SelectPosterFragment;
import mycinema.liel.net.mycinemaproject.Fragments.AddMovieFragments.SelectPremiereFragment;
import mycinema.liel.net.mycinemaproject.HttpManagers.Movies.SendMovies;
import mycinema.liel.net.mycinemaproject.HttpManagers.Movies.UploadImage;

public class AddMovieActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "AddMovieActivity";

    public static AddMovieActivity instance;

    public Button next;

    public static int currentFragment;
    public static Bitmap poster;
    public static String name;
    public static String description;
    public static String premiere;
    public static String genre;
    public static int length;
    public static int agelimit;
    public static String trailer;
    public static String cinemaCity;
    public static String yesPlanet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        setUpToolbar();

        instance = this;

        next = findViewById(R.id.nextButton);
        next.setOnClickListener(this);
        next.setText("Next");

        currentFragment = 0;

        SelectPosterFragment fragment = new SelectPosterFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.addMovieFrameLayout, fragment).commit();
        Log.d(TAG, "PosterFragment activated");
    }

    @Override
    public void onClick(View view) {
        if(view == next) {

            if(currentFragment == 0) {
                if(!SelectPosterFragment.changedImage) {
                    Snackbar.make(findViewById(android.R.id.content), "Please select a poster first!", Snackbar.LENGTH_SHORT).show();
                } else {
                    this.poster = SelectPosterFragment.originalBitmap;
                    currentFragment++;
                    nextFragment();
                }
            } else if(currentFragment == 1) {
                if(SelectNameFragment.name == null) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter the movie's name!", Snackbar.LENGTH_SHORT).show();
                } else {
                    if(SelectNameFragment.name.getText().length() < 1) {
                        Snackbar.make(findViewById(android.R.id.content), "Please enter the movie's name!", Snackbar.LENGTH_SHORT).show();
                    } else {
                        this.name = SelectNameFragment.name.getText().toString();
                        currentFragment++;
                        nextFragment();
                    }
                }
            } else if(currentFragment == 2) {
                if(SelectDescriptionFragment.description == null) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter the movie's description!", Snackbar.LENGTH_SHORT).show();
                } else {
                    if(SelectDescriptionFragment.description .getText().length() < 1) {
                        Snackbar.make(findViewById(android.R.id.content), "Please enter the movie's description!", Snackbar.LENGTH_SHORT).show();
                    } else {
                        this.description = SelectDescriptionFragment.description .getText().toString();
                        currentFragment++;
                        nextFragment();
                    }
                }
            } else if(currentFragment == 3) {
                if(SelectPremiereFragment.premiere == null) {
                    Snackbar.make(findViewById(android.R.id.content), "Please select the movie's premiere!", Snackbar.LENGTH_SHORT).show();
                } else {
                    if(SelectPremiereFragment.premiere.length() < 1) {
                        Snackbar.make(findViewById(android.R.id.content), "Please select the movie's premiere!", Snackbar.LENGTH_SHORT).show();
                    } else {
                        this.premiere = SelectPremiereFragment.premiere.toString();
                        currentFragment++;
                        nextFragment();
                    }
                }
            } else if(currentFragment == 4) {
                if(SelectOtherInfoFragment.genre == null || SelectOtherInfoFragment.length == null || SelectOtherInfoFragment.ageLimit == null) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter genre, length & age limit!", Snackbar.LENGTH_SHORT).show();
                } else {
                    if(SelectOtherInfoFragment.genre.length() < 1 || SelectOtherInfoFragment.length.length() < 1 || SelectOtherInfoFragment.ageLimit.length() < 1) {
                        Snackbar.make(findViewById(android.R.id.content), "Please enter genre, length & age limit!", Snackbar.LENGTH_SHORT).show();
                    } else {
                        this.genre = SelectOtherInfoFragment.genre.getText().toString();
                        this.length = Integer.parseInt(SelectOtherInfoFragment.length.getText().toString());
                        this.agelimit = Integer.parseInt(SelectOtherInfoFragment.ageLimit.getText().toString());
                        currentFragment++;
                        nextFragment();
                    }
                }
            } else if(currentFragment == 5){
                if(SelectLinksFragment.trailer == null || SelectLinksFragment.cinemaCity == null || SelectLinksFragment.yesPlanet == null) {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter trailer, cinema City & yes Planet!", Snackbar.LENGTH_SHORT).show();
                } else {
                    if(SelectLinksFragment.trailer.length() < 1 || SelectLinksFragment.cinemaCity.length() < 1 || SelectLinksFragment.yesPlanet.length() < 1) {
                        Snackbar.make(findViewById(android.R.id.content), "Please enter trailer, cinema City & yes Planet!", Snackbar.LENGTH_SHORT).show();
                    } else {
                        this.trailer = SelectLinksFragment.trailer.getText().toString();
                        this.cinemaCity = SelectLinksFragment.cinemaCity.getText().toString();
                        this.yesPlanet = SelectLinksFragment.yesPlanet.getText().toString();

                        uploadMovie();
                    }
                }
            }
        }
    }

    public void nextFragment(){
        if(currentFragment == 1) {
            SelectNameFragment fragment = new SelectNameFragment();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.addMovieFrameLayout, fragment).commit();
            Log.d(TAG, "NameFragment activated");
        } else if(currentFragment == 2) {
            SelectDescriptionFragment fragment = new SelectDescriptionFragment();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.addMovieFrameLayout, fragment).commit();
            Log.d(TAG, "SelectDescriptionFragment activated");
        } else if(currentFragment == 3) {
            SelectPremiereFragment fragment = new SelectPremiereFragment();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.addMovieFrameLayout, fragment).commit();
            next.setVisibility(View.GONE);
            Log.d(TAG, "SelectDescriptionFragment activated");
        } else if(currentFragment == 4) {
            SelectOtherInfoFragment fragment = new SelectOtherInfoFragment();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.addMovieFrameLayout, fragment).commit();
            Log.d(TAG, "SelectDescriptionFragment activated");
        } else if(currentFragment == 5) {
            SelectLinksFragment fragment = new SelectLinksFragment();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.addMovieFrameLayout, fragment).commit();
            next.setText("Finish");
            Log.d(TAG, "SelectLinksFragment activated");
        }
    }

    public void uploadMovie(){
        SendMovies sender = new SendMovies(getString(R.string.uploadMovieWebPhp), name, description, premiere, genre, length, agelimit, trailer, cinemaCity, yesPlanet, poster);
        sender.execute();
        Log.d(TAG, "Sent movie");
        UploadImage uploadImage = new UploadImage(getString(R.string.uploadImageWebPhp), poster, name.toString());
        uploadImage.execute();
        Log.d(TAG, "Sent image");

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUpToolbar(){
        Toolbar toolbar = findViewById(R.id.addMovieActivityActionBar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
