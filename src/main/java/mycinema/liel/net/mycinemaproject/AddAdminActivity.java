package mycinema.liel.net.mycinemaproject;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mycinema.liel.net.mycinemaproject.HttpManagers.Users.AddAdmin;
import mycinema.liel.net.mycinemaproject.Utils.GlobalVars;
import mycinema.liel.net.mycinemaproject.Utils.Methods;

public class AddAdminActivity extends AppCompatActivity implements View.OnClickListener{

    public static AddAdminActivity instance;

    private EditText email;

    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);

        instance = this;

        if(GlobalVars.user == null) {
            finish();
            Snackbar.make(getCurrentFocus(), "You must be logged in to view this page!", Snackbar.LENGTH_SHORT).show();
        }

        email = (EditText) findViewById(R.id.addAdminEmailEt);

        add = (Button) findViewById(R.id.addAdminSubmitButton);
        add.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == add) {
            if(email.getText().length() == 0) {
                Snackbar.make(getCurrentFocus(), "Please fill all the fields!", Snackbar.LENGTH_SHORT).show();
            } else {
                if(!Methods.isValidEmailAddress(email.getText().toString())) {
                    Snackbar.make(getCurrentFocus(), "New email is not valid!", Snackbar.LENGTH_SHORT).show();
                } else {
                    AddAdmin addAdmin = new AddAdmin(getString(R.string.addAdminWebPhp), GlobalVars.user.getEmail(), GlobalVars.user.getPassword(), email.getText().toString());
                    addAdmin.execute();
                }
            }

        }
    }
}
