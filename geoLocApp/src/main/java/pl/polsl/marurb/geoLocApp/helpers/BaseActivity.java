package pl.polsl.marurb.geoLocApp.helpers;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

import pl.polsl.marurb.geoLocApp.R;
import pl.polsl.marurb.geoLocApp.activities.NoGpsActivity;
import pl.polsl.marurb.geoLocApp.activities.NoInternetActivity;
import pl.polsl.marurb.geoLocApp.activities.login.UserInfoActivity;
import pl.polsl.marurb.geoLocApp.interfaces.BaseInterface;
import pl.polsl.marurb.geoLocApp.tools.gps.CheckGpsStatus;

public class BaseActivity extends Activity implements BaseInterface{
    public CheckGpsStatus checkGpsStatus;

    protected Button headerMessengerButton;
    protected Button headerInfoButton;
    protected Button headerUserButton;
    protected TextView headerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.top_header);

        decorationDesigner();
    }

    @Override
    public void decorationDesigner(){

        headerMessengerButton =(Button) findViewById(R.id.messengerButton);
        headerInfoButton = (Button) findViewById(R.id.infoButton);
        headerTextView = (TextView) findViewById(R.id.headerNameTextView);
        headerUserButton = (Button) findViewById(R.id.loginButton);

        headerTextView.setTextColor(Color.WHITE);
        headerTextView.setText(((Object) this).getClass().getSimpleName());

        headerUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });
        headerInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });
        headerMessengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });
    }


    public void loginInfo(View view){
        Intent intent = new Intent(this, UserInfoActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);

    }

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getExtras() != null) { // we're connected
                NetworkInfo ni=(NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                if(ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                    if(GlobalVariables.isInternet == false){
                        GlobalVariables.isInternet = true;
                        onBackPressed();
                    }


                } else { // we're not connected
                    GlobalVariables.isInternet = false;
                    Intent intentNoInternet = new Intent(context, NoInternetActivity.class);
                    intentNoInternet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intentNoInternet);

                }
            } else { // we're not connected
                GlobalVariables.isInternet = false;
                Intent intentNoInternet = new Intent(context, NoInternetActivity.class);
                intentNoInternet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentNoInternet);

            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }







}
