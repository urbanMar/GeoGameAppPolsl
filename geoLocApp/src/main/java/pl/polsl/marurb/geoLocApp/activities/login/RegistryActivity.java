package pl.polsl.marurb.geoLocApp.activities.login;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import pl.polsl.marurb.geoLocApp.R;
import pl.polsl.marurb.geoLocApp.helpers.BaseActivity;
import pl.polsl.marurb.geoLocApp.items.User;

public class RegistryActivity extends BaseActivity {

    private EditText email1;
    private EditText email2;
    private EditText pass1;
    private EditText pass2;
    private EditText login;

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        decorationDesigner();
    }

    @Override
    protected void onStart() {
        super.onStart();
        login = (EditText) findViewById(R.id.loginEditText);
        email1 = (EditText) findViewById(R.id.email1EditText);
        email2 = (EditText) findViewById(R.id.email2EditText);
        pass1 = (EditText) findViewById(R.id.pass1EditText);
        pass2 = (EditText) findViewById(R.id.pass2EditText);

    }

    public void regiter(View view) throws IOException {

        if(email1.getText().toString().equals(email2.getText().toString()) &&
                pass1.getText().toString().equals(pass2.getText().toString()) &&
                !email1.getText().toString().isEmpty() &&
                !pass1.getText().toString().isEmpty() &&
                !login.getText().toString().isEmpty()){
            user.register(login.getText().toString(), email1.getText().toString(), pass1.getText().toString());
            super.onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), getText(R.string.registration_error),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void decorationDesigner() {
        super.decorationDesigner();
        headerTextView.setText(getString(R.string.title_activity_registry));
        headerMessengerButton.setVisibility(View.GONE);
        headerInfoButton.setVisibility(View.GONE);
        headerUserButton.setVisibility(View.GONE);
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
            View rootView = inflater.inflate(R.layout.fragment_registry, container, false);
            return rootView;
        }
    }

}
