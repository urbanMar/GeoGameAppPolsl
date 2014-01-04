package pl.polsl.marurb.geoLocApp.activities.login;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import pl.polsl.marurb.geoLocApp.activities.StartGameActivity;
import pl.polsl.marurb.geoLocApp.helpers.BaseActivity;
import pl.polsl.marurb.geoLocApp.helpers.GlobalVariables;
import pl.polsl.marurb.geoLocApp.interfaces.BaseInterface;
import pl.polsl.marurb.geoLocApp.items.User;

public class LoginActivity extends BaseActivity{

    User user = new User();

    private EditText mail;
    private EditText pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


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
        headerTextView.setText(getString(R.string.title_activity_login));
        headerMessengerButton.setVisibility(View.GONE);
        headerInfoButton.setVisibility(View.GONE);
        headerUserButton.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mail = (EditText) findViewById(R.id.mailTextView);
        pass = (EditText) findViewById(R.id.passTeaxtView);
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
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            return rootView;
        }
    }

    public void login(View view){

        User user = new User();
        user.setMail(mail.getText().toString());
        user.setPass(pass.getText().toString());

        GlobalVariables.setUser(user);

        Intent intent = new Intent(LoginActivity.this, StartGameActivity.class);
        startActivity(intent);
    }

    public void login1(View view) throws IOException {
        user.login(mail.getText().toString(), pass.getText().toString());

        if (user.getLastStatus() != 1) {
            try {
                String errCode = user.getLastErrors();

                if (errCode.compareTo("API_ERROR_NIKOMPLETNE_DANE_WEJSCIOWE") == 0)
                    Toast.makeText(LoginActivity.this,
                            "Wypełnij poprawnie wszystkie pola",
                            Toast.LENGTH_SHORT).show();

                else if (errCode.compareTo("API_ERROR_UZYTKOWNIK_NIE_ISTNIEJE") == 0)
                    Toast.makeText(LoginActivity.this,
                            "Podany użytkownik nie istnieje",
                            Toast.LENGTH_SHORT).show();

                else if (errCode.compareTo("API_ERROR_BLEDNY_LOGIN_HASLO") == 0)
                    Toast.makeText(LoginActivity.this, "Błdne hasło",
                            Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(LoginActivity.this,
                            "Wystąpił nieznany błąd", Toast.LENGTH_SHORT)
                            .show();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //dialog.dismiss();
            }
        }

        else {

            final AlertDialog alertDialog3 = new AlertDialog.Builder(this)
                    .create();
            User user = new User();
            user.setMail(mail.getText().toString());
            user.setPass(pass.getText().toString());

            GlobalVariables.setUser(user);
            alertDialog3.setMessage("Zostałeś poprawnie zalogowany");
            alertDialog3.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            alertDialog3.show();

            //dialog.dismiss();
            User.isLogged = true;
            user.setUserData();
        }
    }

}


