package pl.polsl.marurb.geoLocApp.activities.login;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import pl.polsl.marurb.geoLocApp.R;
import pl.polsl.marurb.geoLocApp.helpers.BaseActivity;


public class MainLoginActivity extends BaseActivity{

    public ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);



        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        decorationDesigner();

    }

    public void decorationDesigner(){
        super.decorationDesigner();
        headerTextView.setText(getText(R.string.title_activity_main_login));
        headerMessengerButton.setVisibility(View.GONE);
        headerInfoButton.setVisibility(View.GONE);
        headerUserButton.setVisibility(View.GONE);
    }


    public void loginUser(View view){
        Intent intent = new Intent(MainLoginActivity.this, LoginActivity.class);
        startActivity(intent);

    }

    public void registerUser(View view){
        Intent intent = new Intent(MainLoginActivity.this, RegistryActivity.class);
        startActivity(intent);

    }

    public void remindUser(View view){
        Intent intent = new Intent(MainLoginActivity.this, RemindActivity.class);
        startActivity(intent);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_login, container, false);
            return rootView;
        }
    }

}
