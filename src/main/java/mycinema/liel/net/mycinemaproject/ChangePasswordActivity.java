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
import mycinema.liel.net.mycinemaproject.HttpManagers.Users.ChangeUsersPassword;
import mycinema.liel.net.mycinemaproject.Objects.Movie;
import mycinema.liel.net.mycinemaproject.Utils.GlobalVars;
import mycinema.liel.net.mycinemaproject.Utils.Methods;
import mycinema.liel.net.mycinemaproject.Utils.YoutubeManager;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText currentPassword;
    private EditText newPassword;
    private EditText confirmNewPassword;

    private Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        if(GlobalVars.user == null) {
            finish();
            Snackbar.make(getCurrentFocus(), "You must be logged in to view this page!", Snackbar.LENGTH_SHORT).show();
        }

        currentPassword = (EditText) findViewById(R.id.changePasswordCurrentPasswordEt);
        newPassword = (EditText) findViewById(R.id.changePasswordNewPasswordEt);
        confirmNewPassword = (EditText) findViewById(R.id.changePasswordConfirmNewPasswordEt);

        change = (Button) findViewById(R.id.changePasswordSubmitButton);
        change.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == change) {
            if(currentPassword.getText().length() == 0
                    || newPassword.getText().length() == 0
                    || confirmNewPassword.getText().length() == 0) {
                Snackbar.make(getCurrentFocus(), "Please fill all the fields!", Snackbar.LENGTH_SHORT).show();
            } else {
                if(!Methods.hash(currentPassword.getText().toString()).equalsIgnoreCase(GlobalVars.user.getPassword())) {
                    Snackbar.make(getCurrentFocus(), "Wrong Password!", Snackbar.LENGTH_SHORT).show();
                } else {
                    if(!Methods.isValidPassword(newPassword.getText().toString())) {
                        Snackbar.make(getCurrentFocus(), "New password is not valid!", Snackbar.LENGTH_SHORT).show();
                    } else {
                        if(!newPassword.getText().toString().equalsIgnoreCase(confirmNewPassword.getText().toString())) {
                            Snackbar.make(getCurrentFocus(), "Passwords does not match!!", Snackbar.LENGTH_SHORT).show();
                        } else {
                            ChangeUsersPassword changeUsersPassword = new ChangeUsersPassword(getString(R.string.changeUsersPasswordWebPhp), GlobalVars.user.getEmail(), Methods.hash(currentPassword.getText().toString()), Methods.hash(newPassword.getText().toString()));
                            changeUsersPassword.execute();
                        }
                    }
                }
            }

        }
    }
}
