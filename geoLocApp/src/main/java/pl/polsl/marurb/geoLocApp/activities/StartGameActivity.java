package pl.polsl.marurb.geoLocApp.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import pl.polsl.marurb.geoLocApp.R;
import pl.polsl.marurb.geoLocApp.helpers.BaseActivity;
import pl.polsl.marurb.geoLocApp.helpers.GlobalVariables;
import pl.polsl.marurb.geoLocApp.items.Task;

public class StartGameActivity extends BaseActivity {



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

    public void startGame(View view){
        getTask();


    }

    public void getTask(){
        Task task = new Task();
        task.setName("Test task");
        task.setDescription("asdasdasdasdasdasdasd" +
                "asdasdadasda sdasdasdasd asdasdasd asd as da sd asdd\n sdfsdf");
        task.setLongitude("19.0248");
        task.setLatitude("50.2736");

        GlobalVariables.setTask(task);
        Intent intent = new Intent(StartGameActivity.this, GameActivity.class);
        startActivity(intent);

        ///////

        //new HttpAsyncTask().execute(GlobalVariables.getTaskInfo());
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            InputStream inputStream = null;
            String result = "";
            try {

                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(urls[0]));

                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // convert inputstream to string
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = getText(R.string.server_problem).toString();

            } catch (Exception e) {
            }

            return result;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Task task = new Task();
            JSONArray taskJ;
            ////
            ////
            ////
            ///

            try {
                JSONObject json = new JSONObject(result);
                taskJ = json.getJSONArray("Task"); // get articles array
                task.setName(taskJ.getJSONObject(0).getString("name"));
                task.setDescription(taskJ.getJSONObject(0).getString("description"));
                task.setId(Long.parseLong(taskJ.getJSONObject(0).getString("id")));
                task.setLatitude(taskJ.getJSONObject(0).getString("latitude"));
                task.setLongitude(taskJ.getJSONObject(0).getString("longitude"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            Time now = new Time();
            now.setToNow();
            task.setStartDate(now.toString());

            Toast.makeText(getBaseContext(), getText(R.string.received), Toast.LENGTH_LONG).show();

            GlobalVariables.setTask(task);
            Intent intent = new Intent(StartGameActivity.this, GameActivity.class);
            startActivity(intent);
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
