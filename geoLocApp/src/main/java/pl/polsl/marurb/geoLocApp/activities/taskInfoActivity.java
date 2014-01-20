package pl.polsl.marurb.geoLocApp.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.polsl.marurb.geoLocApp.R;
import pl.polsl.marurb.geoLocApp.helpers.BaseActivity;
import pl.polsl.marurb.geoLocApp.helpers.GlobalVariables;
import pl.polsl.marurb.geoLocApp.items.Task;

public class TaskInfoActivity extends BaseActivity {

    Task task;
    TextView time;
    TextView currLat;
    TextView currLon;
    TextView taskLat;
    TextView taskLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

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
        task = GlobalVariables.getTask();
        time = (TextView)findViewById(R.id.taskInfoStartTimetextView);
        currLat = (TextView)findViewById(R.id.taskInfoCurrLattextView);
        currLon = (TextView)findViewById(R.id.taskInfoCurrLontextView);
        taskLat = (TextView)findViewById(R.id.taskInfoTaskLattextView);
        taskLon = (TextView)findViewById(R.id.taskInfoTaskLontextView);

        time.setText(task.getStartDate());
        currLat.setText(task.getCurrentLatitud());
        currLon.setText(task.getCurrentLongitude());
        taskLat.setText(task.getLatitude());
        taskLon.setText(task.getLongitude());

    }


    @Override
    public void decorationDesigner() {
        super.decorationDesigner();
        headerTextView.setText(getString(R.string.title_activity_task_info));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task_info, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_task_info, container, false);
            return rootView;
        }
    }

}
