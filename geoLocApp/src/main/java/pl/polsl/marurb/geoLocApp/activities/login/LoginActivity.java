package pl.polsl.marurb.geoLocApp.activities.login;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import pl.polsl.marurb.geoLocApp.R;
import pl.polsl.marurb.geoLocApp.activities.StartGameActivity;
import pl.polsl.marurb.geoLocApp.helpers.BaseActivity;
import pl.polsl.marurb.geoLocApp.helpers.GlobalVariables;
import pl.polsl.marurb.geoLocApp.items.User;

public class LoginActivity extends BaseActivity{



    private EditText mail;
    private EditText pass;
    User user = new User();


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



    private static   List<NameValuePair> post = new ArrayList<NameValuePair>(2);
    private String status;
    public void login(View view) throws IOException, JSONException {

        post.clear();

        mail = (EditText) findViewById(R.id.mailTextView);
        pass = (EditText) findViewById(R.id.passTeaxtView);

        post.add(new BasicNameValuePair("email", mail.getText().toString()));
        post.add(new BasicNameValuePair("pass", pass.getText().toString()));

        new postTask().execute(GlobalVariables.getUserLogin());


    }

    public static CookieStore cookieStore = new BasicCookieStore();
    public static HttpContext localContext = new BasicHttpContext();

    class postTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;
        private long id;
        private String maill;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(status.equalsIgnoreCase("WRONG_DATA")){
                final AlertDialog alertDialog3 = new AlertDialog.Builder(LoginActivity.this)
                        .create();
                alertDialog3.setMessage(getString(R.string.login_problem).toString());
                alertDialog3.setButton(getText(R.string.ok).toString(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog3.show();
            } else if(status.equalsIgnoreCase("NO_GAME")){
                final AlertDialog alertDialog3 = new AlertDialog.Builder(LoginActivity.this)
                        .create();
                alertDialog3.setMessage(getString(R.string.no_game).toString());
                alertDialog3.setButton(getText(R.string.ok).toString(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog3.show();
            } else {



                user.setMail(maill);
                user.setPass(pass.getText().toString());
                user.setId(id);
                user.setLogin(mail.getText().toString());

                GlobalVariables.setUser(user);

                System.out.println("MAIL: "+GlobalVariables.getUser().getMail()+" ID: " +GlobalVariables.getUser().getId()+" PASS: "+GlobalVariables.getUser().getPass()+" LOGIN: "+GlobalVariables.getUser().getLogin());

                Intent intent = new Intent(LoginActivity.this, StartGameActivity.class);
                startActivity(intent);
            }


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
                //HttpResponse response = client.execute(httpPost);
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

            JSONObject out = new JSONObject();

            try {
                out = new JSONObject(response);
                Log.d("User-model", out.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONObject userJ = out.getJSONObject("user");
                System.out.println("!!! ID: " + userJ.getString("id"));
                id = Long.parseLong(userJ.getString("id"));
                maill = userJ.getString("email").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }




            try {
                status = out.getString("status").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  out.toString();
        }
    }

}


