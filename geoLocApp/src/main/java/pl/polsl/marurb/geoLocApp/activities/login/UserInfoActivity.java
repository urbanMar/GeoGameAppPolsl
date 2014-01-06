package pl.polsl.marurb.geoLocApp.activities.login;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;

import pl.polsl.marurb.geoLocApp.R;
import pl.polsl.marurb.geoLocApp.helpers.BaseActivity;
import pl.polsl.marurb.geoLocApp.helpers.GlobalVariables;
import pl.polsl.marurb.geoLocApp.items.User;

public class UserInfoActivity extends BaseActivity {


    User user = new User();
    TextView mail;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        decorationDesigner();
    }

    @Override
    public void decorationDesigner() {
        super.decorationDesigner();
        headerTextView.setText(getString(R.string.title_activity_user_info));
    }

    @Override
    protected void onStart() {
        super.onStart();
        login = (TextView) findViewById(R.id.userLoginNameTextView);
        login.setText(GlobalVariables.getUser().getLogin());
        mail = (TextView) findViewById(R.id.userLoginMailTextView);
        mail.setText(GlobalVariables.getUser().getMail());
    }

    public void logout(View view) throws IOException {
        user.logout();
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
