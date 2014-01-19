package pl.polsl.marurb.geoLocApp.activities.login;

import android.app.Fragment;
import android.app.ProgressDialog;
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
import pl.polsl.marurb.geoLocApp.items.User;


public class RemindActivity extends BaseActivity {

    private EditText mail;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);

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
        headerTextView.setText(getString(R.string.title_activity_remind));
        headerMessengerButton.setVisibility(View.GONE);
        headerInfoButton.setVisibility(View.GONE);
        headerUserButton.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mail = (EditText) findViewById(R.id.emailRemindEditText);
    }

    private static List<NameValuePair> post = new ArrayList<NameValuePair>(1);

    public void remind(View view) throws IOException {

        post.clear();


        if(!mail.getText().toString().isEmpty()){
            post.add(new BasicNameValuePair("email", mail.getText().toString()));
            new postTask().execute(GlobalVariables.getUserForgot());
        } else {
            Toast.makeText(getApplicationContext(), getText(R.string.empty_mail),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static CookieStore cookieStore = new BasicCookieStore();
    public static HttpContext localContext = new BasicHttpContext();

    class postTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RemindActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

                finish();

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
            View rootView = inflater.inflate(R.layout.fragment_remind, container, false);
            return rootView;
        }
    }

}
