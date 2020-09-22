package mycinema.liel.net.mycinemaproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import mycinema.liel.net.mycinemaproject.AddAdminActivity;
import mycinema.liel.net.mycinemaproject.AddMovieActivity;
import mycinema.liel.net.mycinemaproject.Analyze;
import mycinema.liel.net.mycinemaproject.ChangeEmailActivity;
import mycinema.liel.net.mycinemaproject.ChangePasswordActivity;
import mycinema.liel.net.mycinemaproject.MainActivity;
import mycinema.liel.net.mycinemaproject.R;
import mycinema.liel.net.mycinemaproject.Utils.GlobalVars;

public class ProfileFragment extends Fragment implements View.OnClickListener, Switch.OnCheckedChangeListener{

    private static final String TAG = "ProfileActivity";

    public ConstraintLayout layout;

    private TextView helloUsernameTv, emailTv, changeEmailTv, changePasswordTv;
    private LinearLayout profileAdminLayout;
    private TextView profileAddMovieTv, profileAddAdminTv;
    private Switch vibrations, notifications /*darktheme*/;
    private Button signOut;
    private View fragment_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        this.fragment_view = v;

        if(GlobalVars.user == null) {
            LoginFragment loginFragment = new LoginFragment();
            FragmentManager fm = MainActivity.instance.getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.mainActivityFrameLayout, loginFragment).commit();
            return null;
        }

        helloUsernameTv = v.findViewById(R.id.profileHelloUsernameTv);
        helloUsernameTv.setText("Hello, " + GlobalVars.user.getUsername());

        emailTv = v.findViewById(R.id.profileEmailTv);
        emailTv.setText("Your email address is: " + GlobalVars.user.getEmail());

        changeEmailTv = v.findViewById(R.id.profileChangeEmailTv);
        changePasswordTv = v.findViewById(R.id.profileChangePasswordTv);
        changeEmailTv.setOnClickListener(this);
        changePasswordTv.setOnClickListener(this);
        //TODO add email & password click event

        profileAdminLayout = v.findViewById(R.id.profileAdminLayout);
        if(!GlobalVars.user.getType().equalsIgnoreCase("admin"))
            profileAdminLayout.setVisibility(View.GONE);

        profileAddMovieTv = v.findViewById(R.id.profileAddMovieTv);
        profileAddAdminTv = v.findViewById(R.id.profileAddAdminTv);
        profileAddMovieTv.setOnClickListener(this);
        profileAddAdminTv.setOnClickListener(this);
        //TODO add admin & movie click event

        vibrations = v.findViewById(R.id.profileVibrationsSwitch);
        notifications = v.findViewById(R.id.profileNotificationsSwitch);
        //darktheme = v.findViewById(R.id.profileDarkthemeSwitch);

        vibrations.setChecked(false);
        notifications.setChecked(false);
//        darktheme.setChecked(false);

        if(Analyze.instance.sp.getBoolean("vibrations", true))
            vibrations.setChecked(true);
        if(Analyze.instance.sp.getBoolean("notifications", true))
            notifications.setChecked(true);
//        if(Analyze.instance.sp.getBoolean("darktheme", false))
//            darktheme.setChecked(true);

        vibrations.setOnCheckedChangeListener(this);
        notifications.setOnCheckedChangeListener(this);
//        darktheme.setOnCheckedChangeListener(this);

        signOut = v.findViewById(R.id.profileSignOutButton);
        signOut.setOnClickListener(this);

        layout = v.findViewById(R.id.layoutProfile);

        int color =  R.color.backgrounddark;
//        if(Analyze.instance.sp.getBoolean("darktheme", false))
//            layout.setBackgroundColor(getResources().getColor(color));

        return v;
    }

    @Override
    public void onClick(View view) {
        if(view == signOut) {
            GlobalVars.user = null;

            SharedPreferences.Editor editor = Analyze.instance.sp.edit();
            editor.putString("email", "x");
            editor.putString("password", "x");
            editor.commit();
            editor.apply();

            LoginFragment loginFragment = new LoginFragment();
            FragmentManager fm = MainActivity.instance.getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.mainActivityFrameLayout, loginFragment).commit();
            return;
        } else if(view == profileAddMovieTv) {
            Intent intent = new Intent(MainActivity.instance, AddMovieActivity.class);
            MainActivity.instance.startActivity(intent);
        } else if(view == changePasswordTv) {
            Intent intent = new Intent(MainActivity.instance, ChangePasswordActivity.class);
            MainActivity.instance.startActivity(intent);
        } else if(view == changeEmailTv) {
            Intent intent = new Intent(MainActivity.instance, ChangeEmailActivity.class);
            MainActivity.instance.startActivity(intent);
        } else if(view == profileAddAdminTv) {
            Intent intent = new Intent(MainActivity.instance, AddAdminActivity.class);
            MainActivity.instance.startActivity(intent);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == vibrations.getId()) {
            if (b)
                Snackbar.make(compoundButton.getRootView().findViewById(android.R.id.content), "Vibrations are now enabled", Snackbar.LENGTH_SHORT).show();
            else
                Snackbar.make(compoundButton.getRootView().findViewById(android.R.id.content), "Vibrations are now disabled", Snackbar.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = Analyze.instance.sp.edit();
            editor.putBoolean("vibrations", b);
            editor.commit();
            editor.apply();
            return;
        }
        if (compoundButton.getId() == notifications.getId()) {
            if (b)
                Snackbar.make(compoundButton.getRootView().findViewById(android.R.id.content), "Notifications are now enabled", Snackbar.LENGTH_SHORT).show();
            else
                Snackbar.make(compoundButton.getRootView().findViewById(android.R.id.content), "Notifications are now disabled", Snackbar.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = Analyze.instance.sp.edit();
            editor.putBoolean("notifications", b);
            editor.commit();
            editor.apply();
            return;
        }
//        if (compoundButton.getId() == darktheme.getId()) {
//            if (b)
//                Snackbar.make(compoundButton.getRootView().findViewById(android.R.id.content), "Dark Theme is now enabled", Snackbar.LENGTH_SHORT).show();
//            else
//                Snackbar.make(compoundButton.getRootView().findViewById(android.R.id.content), "Dark Theme is now disabled", Snackbar.LENGTH_SHORT).show();
//            SharedPreferences.Editor editor = Analyze.instance.sp.edit();
//            editor.putBoolean("darktheme", b);
//            MainActivity.instance.reload(b);
//            editor.commit();
//            editor.apply();
//            return;
//        }
    }

    public void reload(Boolean isDark) {
        if(isDark) {
            int color =  R.color.backgrounddark;
            layout.setBackgroundColor(getResources().getColor(color));
        } else {
            int color =  R.color.background;
            layout.setBackgroundColor(getResources().getColor(color));
        }
    }

}
