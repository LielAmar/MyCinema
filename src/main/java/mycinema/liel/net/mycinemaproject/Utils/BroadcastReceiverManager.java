package mycinema.liel.net.mycinemaproject.Utils;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import mycinema.liel.net.mycinemaproject.MainActivity;
import mycinema.liel.net.mycinemaproject.R;

public class BroadcastReceiverManager extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
            boolean isAirplaneModeOn = intent.getBooleanExtra("state", false);
            if (isAirplaneModeOn) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View v = inflater.inflate(R.layout.alert_layout, null);
                Button b = v.findViewById(R.id.closeDialog);
                builder.setView(v);
                final AlertDialog dialog = builder.create();

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                // AP mode is on
            } else {
                // AP mode is off
            }

    }
}
