package mycinema.liel.net.mycinemaproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import mycinema.liel.net.mycinemaproject.HttpManagers.Movies.DeleteMovie;
import mycinema.liel.net.mycinemaproject.HttpManagers.Movies.GetMovies;
import mycinema.liel.net.mycinemaproject.HttpManagers.Users.ChangeUsersEmail;
import mycinema.liel.net.mycinemaproject.HttpManagers.Users.ChangeUsersPassword;
import mycinema.liel.net.mycinemaproject.Objects.Movie;
import mycinema.liel.net.mycinemaproject.Utils.GlobalVars;
import mycinema.liel.net.mycinemaproject.Utils.Methods;
import mycinema.liel.net.mycinemaproject.Utils.YoutubeManager;

public class ChangeEmailActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText currentEmail;
    private EditText newEmail;
    private EditText confirmNewEmail;

    private Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        if(GlobalVars.user == null) {
            finish();
            Snackbar.make(getCurrentFocus(), "You must be logged in to view this page!", Snackbar.LENGTH_SHORT).show();
        }

        currentEmail = (EditText) findViewById(R.id.changeEmailCurrentEmailEt);
        newEmail = (EditText) findViewById(R.id.changeEmailNewEmailEt);
        confirmNewEmail = (EditText) findViewById(R.id.changeEmailConfirmNewEmailEt);

        change = (Button) findViewById(R.id.changeEmailSubmitButton);
        change.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == change) {
            if(currentEmail.getText().length() == 0
                    || newEmail.getText().length() == 0
                    || confirmNewEmail.getText().length() == 0) {
                Snackbar.make(getCurrentFocus(), "Please fill all the fields!", Snackbar.LENGTH_SHORT).show();
            } else {
                if(!currentEmail.getText().toString().equalsIgnoreCase(GlobalVars.user.getEmail())) {
                    Snackbar.make(getCurrentFocus(), "Wrong Email!", Snackbar.LENGTH_SHORT).show();
                } else {
                    if(!Methods.isValidEmailAddress(newEmail.getText().toString())) {
                        Snackbar.make(getCurrentFocus(), "New email is not valid!", Snackbar.LENGTH_SHORT).show();
                    } else {
                        if(!newEmail.getText().toString().equalsIgnoreCase(confirmNewEmail.getText().toString())) {
                            Snackbar.make(getCurrentFocus(), "Emails does not match!!", Snackbar.LENGTH_SHORT).show();
                        } else {
                            ChangeUsersEmail changeUsersEmail = new ChangeUsersEmail(getString(R.string.changeUsersEmailWebPhp), GlobalVars.user.getEmail(), GlobalVars.user.getPassword(), newEmail.getText().toString());
                            changeUsersEmail.execute();
                        }
                    }
                }
            }

        }
    }
}
