package mycinema.liel.net.mycinemaproject.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import mycinema.liel.net.mycinemaproject.Analyze;
import mycinema.liel.net.mycinemaproject.HttpManagers.Users.LoginUser;
import mycinema.liel.net.mycinemaproject.MainActivity;
import mycinema.liel.net.mycinemaproject.R;
import mycinema.liel.net.mycinemaproject.Utils.Methods;

public class LoginFragment extends Fragment implements View.OnClickListener{

    public static LoginFragment instance;

    private ConstraintLayout layout;

    private EditText email, password;
    private Button logIn;
    private TextView loginToRegister;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        this.v = v;

        instance = this;

        email = v.findViewById(R.id.loginEmailEt);
        password = v.findViewById(R.id.loginPasswordEt);

        logIn = v.findViewById(R.id.loginLoginButton);
        logIn.setOnClickListener(this);

        loginToRegister = v.findViewById(R.id.loginRegisterTv);
        loginToRegister.setOnClickListener(this);

        layout = v.findViewById(R.id.layoutLogin);

        int color =  R.color.backgrounddark;
        if(Analyze.instance.sp.getBoolean("darktheme", false))
            layout.setBackgroundColor(getResources().getColor(color));

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        email.clearFocus();
        password.clearFocus();
    }

    @Override
    public void onClick(View view) {
        Methods.hideKeyboardForMain();

        if(isConnected()) {
            if (view == loginToRegister) {
                RegisterFragment registerFragment = new RegisterFragment();
                FragmentManager fm = MainActivity.instance.getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.mainActivityFrameLayout, registerFragment).commit();
            } else if (view == logIn) {
                if (email.getText().length() > 0 && password.getText().length() > 0) {
                    String hashedPassword = Methods.hash(password.getText().toString());
                    LoginUser lu = new LoginUser(getString(R.string.loginWebPhp), email.getText().toString(), hashedPassword, true);
                    lu.execute();
                } else {
                    Snackbar.make(view.findViewById(android.R.id.content), "Please enter email And password.", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        } else {
            Snackbar.make(this.v, "No Connection.", Snackbar.LENGTH_LONG).show();
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
