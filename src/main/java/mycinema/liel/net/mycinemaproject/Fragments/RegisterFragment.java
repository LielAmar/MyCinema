package mycinema.liel.net.mycinemaproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import mycinema.liel.net.mycinemaproject.Analyze;
import mycinema.liel.net.mycinemaproject.HttpManagers.Users.LoginUser;
import mycinema.liel.net.mycinemaproject.HttpManagers.Users.RegisterUser;
import mycinema.liel.net.mycinemaproject.MainActivity;
import mycinema.liel.net.mycinemaproject.R;
import mycinema.liel.net.mycinemaproject.TermsActivity;
import mycinema.liel.net.mycinemaproject.Utils.Methods;

public class RegisterFragment extends Fragment implements View.OnClickListener{

    public static RegisterFragment instance;

    private ConstraintLayout layout;

    private EditText email, username, password, confirmPassword;
    private Button register;
    private TextView terms, registerToLogin;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        this.v = v;

        instance = this;

        email = v.findViewById(R.id.registerEmailEt);
        username = v.findViewById(R.id.registerUsernameEt);
        password = v.findViewById(R.id.registerPasswordEt);
        confirmPassword = v.findViewById(R.id.registerConfirmPasswordEt);

        register = v.findViewById(R.id.registerRegisterButton);
        register.setOnClickListener(this);

        terms = v.findViewById(R.id.registerTermsTv);
        String txt = "By clicking 'register' you agree to our Terms of Service";
        SpannableString ss = new SpannableString(txt);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(MainActivity.instance, TermsActivity.class);
                startActivity(intent);
            }
        };
        ss.setSpan(clickableSpan, 40, 56, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        terms.setText(ss);
        terms.setMovementMethod(LinkMovementMethod.getInstance());

        registerToLogin = v.findViewById(R.id.registerLoginTv);
        registerToLogin.setOnClickListener(this);

        layout = v.findViewById(R.id.layoutRegister);

        int color =  R.color.backgrounddark;
        if(Analyze.instance.sp.getBoolean("darktheme", false))
            layout.setBackgroundColor(getResources().getColor(color));

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        email.clearFocus();
        username.clearFocus();
        password.clearFocus();
        confirmPassword.clearFocus();
    }

    @Override
    public void onClick(View view) {
        Methods.hideKeyboardForMain();

        if (isConnected()) {
            if (view == registerToLogin) {
                LoginFragment loginFragment = new LoginFragment();
                FragmentManager fm = MainActivity.instance.getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.mainActivityFrameLayout, loginFragment).commit();
            } else if (view == register) {
                if (!password.getText().toString().equalsIgnoreCase(confirmPassword.getText().toString())) {
                    Snackbar.make(MainActivity.instance.getCurrentFocus(), "The passwords does not match!", Snackbar.LENGTH_SHORT).show();

                } else if (!(Methods.isValidEmailAddress(email.getText().toString()) && Methods.isValidPassword(password.getText().toString()) && Methods.isValidUsername(username.getText().toString()) && password.getText().toString().equals(confirmPassword.getText().toString()))) {
                    if (!Methods.isValidEmailAddress(email.getText().toString()))
                        Snackbar.make(MainActivity.instance.getCurrentFocus(), "Invalid Email!", Snackbar.LENGTH_SHORT).show();
                    else if (!Methods.isValidUsername(username.getText().toString()))
                        Snackbar.make(MainActivity.instance.getCurrentFocus(), "Invalid Username! Your username must be at least 4 characters long and contain only letters and numbers!", Snackbar.LENGTH_SHORT).show();
                    else if (!Methods.isValidPassword(password.getText().toString()))
                        Snackbar.make(MainActivity.instance.getCurrentFocus(), "Invalid Password! Your password must be at least 8 characters long and contain at least 1 number", Snackbar.LENGTH_SHORT).show();

                    if (Analyze.instance.sp.getBoolean("vibrate", true))
                        Analyze.instance.vibrator.vibrate(400);
                } else {
                    String hashedPassword = Methods.hash(password.getText().toString());
                    RegisterUser ru = new RegisterUser(getString(R.string.registerWebPhp), email.getText().toString(), username.getText().toString(), hashedPassword);
                    ru.execute();
                }
            } else {
                Snackbar.make(this.v, "No Connection.", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)MainActivity.instance.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }
}
