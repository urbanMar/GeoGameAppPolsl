package pl.polsl.marurb.geoLocApp.activities.login;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import pl.polsl.marurb.geoLocApp.R;
import pl.polsl.marurb.geoLocApp.helpers.BaseActivity;
import pl.polsl.marurb.geoLocApp.helpers.GlobalVariables;

public class RegistryActivity extends BaseActivity {

    private EditText email1;
    private EditText email2;
    private EditText pass1;
    private EditText pass2;
    private EditText login;




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
    private static List<NameValuePair> post = new ArrayList<NameValuePair>(3);
    private String status;
    public void regiter(View view) throws IOException {

        if(email1.getText().toString().equals(email2.getText().toString()) &&
                pass1.getText().toString().equals(pass2.getText().toString()) &&
                !email1.getText().toString().isEmpty() &&
                !pass1.getText().toString().isEmpty() &&
                !login.getText().toString().isEmpty()){


            post.clear();



            post.add(new BasicNameValuePair("login", login.getText().toString()));
            post.add(new BasicNameValuePair("email", email1.getText().toString()));
            post.add(new BasicNameValuePair("pass", pass1.getText().toString()));

            new postTask().execute(GlobalVariables.getUserRegister());


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

    public static CookieStore cookieStore = new BasicCookieStore();
    public static HttpContext localContext = new BasicHttpContext();

    class postTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;
        boolean isReg = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegistryActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(!isReg){
                final AlertDialog alertDialog3 = new AlertDialog.Builder(RegistryActivity.this)
                        .create();
                alertDialog3.setMessage(getString(R.string.reg_error).toString());
                alertDialog3.setButton(getText(R.string.ok).toString(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog3.show();
            } else {
                finish();
            }

            //Intent intent = new Intent(RegistryActivity.this, LoginActivity.class);
           // startActivity(intent);


        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params[0]);


            try {
                localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
                httpPost.setEntity(new UrlEncodedFormEntity(post, HTTP.UTF_8));


                HttpResponse response = client.execute(httpPost, localContext);
                StatusLine statusLine = response.getStatusLine();

                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } else {
                    Log.e(JsonReader.class.toString(), "Failed to download file");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String response = builder.toString();

            //System.out.println(builder);
            isReg = Boolean.parseBoolean(builder.toString());



            return  "";
        }
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
