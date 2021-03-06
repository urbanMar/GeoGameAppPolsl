package pl.polsl.marurb.geoLocApp.helpers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import pl.polsl.marurb.geoLocApp.R;
import pl.polsl.marurb.geoLocApp.activities.MessengerActivity;
import pl.polsl.marurb.geoLocApp.activities.NoInternetActivity;
import pl.polsl.marurb.geoLocApp.activities.TaskInfoActivity;
import pl.polsl.marurb.geoLocApp.activities.login.UserInfoActivity;
import pl.polsl.marurb.geoLocApp.interfaces.BaseInterface;
import pl.polsl.marurb.geoLocApp.tools.gps.CheckGpsStatus;

public class BaseActivity extends Activity implements BaseInterface{
    public CheckGpsStatus checkGpsStatus;

    protected ImageButton headerMessengerButton;
    protected ImageButton headerInfoButton;
    protected ImageButton headerUserButton;
    protected TextView headerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.top_header);

        decorationDesigner();
    }

    @Override
    protected void onStart() {
        super.onStart();
        headerMessengerButton =(ImageButton) findViewById(R.id.messengerButton);
        headerInfoButton = (ImageButton) findViewById(R.id.infoButton);
        headerTextView = (TextView) findViewById(R.id.headerNameTextView);
        headerUserButton = (ImageButton) findViewById(R.id.loginButton);
    }

    @Override
    public void decorationDesigner(){

        headerMessengerButton =(ImageButton) findViewById(R.id.messengerButton);
        headerInfoButton = (ImageButton) findViewById(R.id.infoButton);
        headerTextView = (TextView) findViewById(R.id.headerNameTextView);
        headerUserButton = (ImageButton) findViewById(R.id.loginButton);

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
                Intent intent = new Intent(BaseActivity.this, TaskInfoActivity.class);
                startActivity(intent);
            }
        });
        headerMessengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseActivity.this, MessengerActivity.class);
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
