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
import android.widget.TextView;

import pl.polsl.marurb.geoLocApp.R;
import pl.polsl.marurb.geoLocApp.helpers.BaseActivity;
import pl.polsl.marurb.geoLocApp.helpers.GlobalVariables;

public class UserInfoActivity extends BaseActivity {

    TextView mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public void decorationDesigner() {
        super.decorationDesigner();
        headerTextView.setText(getString(R.string.title_activity_user_info));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mail = (TextView) findViewById(R.id.userMailTextView);
        mail.setText(GlobalVariables.getUser().getMail());
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
            View rootView = inflater.inflate(R.layout.fragment_user_info, container, false);
            return rootView;
        }
    }

}