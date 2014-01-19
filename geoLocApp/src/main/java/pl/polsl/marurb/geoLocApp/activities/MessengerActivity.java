package pl.polsl.marurb.geoLocApp.activities;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pl.polsl.marurb.geoLocApp.R;
import pl.polsl.marurb.geoLocApp.helpers.BaseActivity;
import pl.polsl.marurb.geoLocApp.helpers.GlobalVariables;

public class MessengerActivity extends BaseActivity {

    TextView messenger;
    EditText message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        decorationDesigner();


        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            postGetTask task = new postGetTask();
                            task.execute(GlobalVariables.getGetMsg(""+GlobalVariables.getUser().getId()));
                        } catch (Exception e) {

                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 50000); //execute in every 50000 ms




    }

    @Override
    public void decorationDesigner() {
        super.decorationDesigner();
        headerTextView.setText(getText(R.string.title_activity_messenger));
    }

    @Override
    protected void onStart() {
        super.onStart();

        messenger = (TextView) findViewById(R.id.allMessegesTextView);
        message = (EditText) findViewById(R.id.userMessageEditText);
        messenger.setMovementMethod(new ScrollingMovementMethod());


    }

    private static List<NameValuePair> post = new ArrayList<NameValuePair>(2);

    public void send(View view){

        post.clear();

        post.add(new BasicNameValuePair("userId", "" + GlobalVariables.getUser().getId()));
        post.add(new BasicNameValuePair("msg", message.getText().toString()));

        new postSendTask().execute(GlobalVariables.getSendMsg());

    }


    public static CookieStore cookieStore = new BasicCookieStore();
    public static HttpContext localContext = new BasicHttpContext();

    class postSendTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MessengerActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            message.setText("");
            progressDialog.dismiss();
            new postGetTask().execute(GlobalVariables.getGetMsg(""+GlobalVariables.getUser().getId()));


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



    class postGetTask extends AsyncTask<String, String, String> {

        JSONArray array = new JSONArray();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String htmlString = "";

            for(int i =0 ; i < array.length(); i++){
                try {
                    JSONObject object = array.getJSONObject(i);
                   // System.out.println(object.getString("login"));


                    //System.out.println(object.getString("text"));

                    htmlString = htmlString + "<b>" + object.getString("login").toString()  + "</b>"
                            + ":<br>" + object.getString("text").toString()+ "<br><br>" ;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            messenger.setText(Html.fromHtml(htmlString));


        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(params[0]);
            //Log.d("USER-URL", url);

            try {
                localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
                HttpResponse response = client.execute(httpGet, localContext);
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
                    //Log.e(JsonReader.class.toString(), "Failed to download file");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String response = builder.toString();


            try {
                array = new JSONArray(response);
                //Log.d("User-model", array.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return "";
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
            View rootView = inflater.inflate(R.layout.fragment_messenger, container, false);
            return rootView;
        }
    }

}
