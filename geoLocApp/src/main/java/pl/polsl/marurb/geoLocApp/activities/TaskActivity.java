package pl.polsl.marurb.geoLocApp.activities;

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
import android.view.Menu;
import android.view.MenuItem;
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
import pl.polsl.marurb.geoLocApp.helpers.BaseActivity;
import pl.polsl.marurb.geoLocApp.helpers.GlobalVariables;

public class TaskActivity extends BaseActivity {

    TextView taskName;
    TextView taskDesc;
    EditText taskAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

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
        headerTextView.setText(getText(R.string.title_activity_task));
    }

    @Override
    protected void onStart() {
        super.onStart();
        taskAnswer = (EditText) findViewById(R.id.taskAnswerEditText);
        taskDesc = (TextView) findViewById(R.id.taskDescriptionTextView);
        taskName = (TextView) findViewById(R.id.taskNameTextView);

        taskName.setText(GlobalVariables.getTask().getName());
        taskDesc.setText(GlobalVariables.getTask().getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task, menu);
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

    @Override
    public void onBackPressed() {
    }

    private static List<NameValuePair> post = new ArrayList<NameValuePair>(2);
    public void send(View view){

        post.clear();
        post.add(new BasicNameValuePair("userId", "" + GlobalVariables.getUser().getId()));
        post.add(new BasicNameValuePair("nodeId", ""+GlobalVariables.getTask().getId()));
        post.add(new BasicNameValuePair("password", taskAnswer.getText().toString()));

        new postTask().execute(GlobalVariables.getTaskAnswear());

    }


    public static CookieStore cookieStore = new BasicCookieStore();
    public static HttpContext localContext = new BasicHttpContext();

    class postTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;
        private String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(TaskActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            if(status.equalsIgnoreCase("WRONG_PASSWORD")){
                final AlertDialog alertDialog3 = new AlertDialog.Builder(TaskActivity.this)
                        .create();
                alertDialog3.setMessage(getString(R.string.wrong_answer).toString());
                alertDialog3.setButton(getText(R.string.ok).toString(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog3.hide();
                    }
                });
                alertDialog3.show();

            }else {
                Intent intent = new Intent(TaskActivity.this, StartGameActivity.class);
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
                status = "" + out.getString("status");
                //System.out.println("!!! ID: " + userJ.getString("longitude"));


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return  out.toString();
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
            View rootView = inflater.inflate(R.layout.fragment_task, container, false);
            return rootView;
        }
    }

}
