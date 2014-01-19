package pl.polsl.marurb.geoLocApp.activities;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.Calendar;
import java.util.List;

import pl.polsl.marurb.geoLocApp.R;
import pl.polsl.marurb.geoLocApp.helpers.BaseActivity;
import pl.polsl.marurb.geoLocApp.helpers.GlobalVariables;
import pl.polsl.marurb.geoLocApp.items.Task;
import pl.polsl.marurb.geoLocApp.items.User;

public class StartGameActivity extends BaseActivity {

    User user = GlobalVariables.getUser();
    Task task = new Task();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        decorationDesigner();

    }

    private static List<NameValuePair> post = new ArrayList<NameValuePair>(2);

    public void startGame(View view){
        //getTask();

        post.clear();
        post.add(new BasicNameValuePair("email", user.getLogin()));
        post.add(new BasicNameValuePair("pass", user.getPass()));

        new postTask().execute(GlobalVariables.getUserLogin());

    }

    public void getTask(){
        Task task = new Task();
        task.setName("Test");
        task.setDescription("2 + 2 *2 is equal");
        task.setLongitude("19.0248");
        task.setLatitude("50.2736");

        GlobalVariables.setTask(task);
        Intent intent = new Intent(StartGameActivity.this, GameActivity.class);
        startActivity(intent);

        ///////

        //new HttpAsyncTask().execute(GlobalVariables.getTaskInfo());
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
            progressDialog = new ProgressDialog(StartGameActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Calendar c = Calendar.getInstance();
            task.setStartDate("" + c.getTime());
            GlobalVariables.setTask(task);
            progressDialog.dismiss();
            Intent intent = new Intent(StartGameActivity.this, GameActivity.class);
            startActivity(intent);



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
                JSONObject userJ = out.getJSONObject("node");
                System.out.println("!!! ID: " + userJ);
                //System.out.println("!!! ID: " + userJ.getString("longitude"));
                task.setLongitude(userJ.getString("longitude"));
                task.setLatitude(userJ.getString("latitude"));
                task.setName(userJ.getString("description"));
                task.setDescription(userJ.getString("task"));
                task.setId(userJ.getLong("id"));
                System.out.println("!!! ID: " + userJ.getLong("id"));

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return  out.toString();
        }
    }







    @Override
    public void decorationDesigner() {
        super.decorationDesigner();
        headerTextView.setText(getText(R.string.title_activity_start_game));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_game, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_start_game, container, false);
            return rootView;
        }
    }

}
