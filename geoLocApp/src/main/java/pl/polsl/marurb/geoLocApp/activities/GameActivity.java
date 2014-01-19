package pl.polsl.marurb.geoLocApp.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import java.util.Calendar;
import java.util.List;

import pl.polsl.marurb.geoLocApp.R;
import pl.polsl.marurb.geoLocApp.helpers.BaseActivity;
import pl.polsl.marurb.geoLocApp.helpers.GlobalVariables;
import pl.polsl.marurb.geoLocApp.items.Task;

import static android.widget.Toast.LENGTH_LONG;

public class GameActivity extends BaseActivity implements SensorEventListener, LocationListener{


    private LocationManager mLocationManager;
    private SensorManager mSensorManager;


    private float[] mRotationMatrix = new float[16];
    private float[] mValues = new float[3];

    private float currentBearing = 0;
    private Location currentLocation;
    private Location desiredLocation;

    private ImageView arrowIV;

    private Bitmap arrowImgN;
    private Bitmap arrowImgNE;
    private Bitmap arrowImgNW;
    private Bitmap arrowImgE;
    private Bitmap arrowImgW;
    private Bitmap arrowImgS;

    private Task task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


        arrowImgE = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_e);
        arrowImgN = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_n);
        arrowImgNE = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_ne);
        arrowImgNW = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_nw);
        arrowImgS = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_s);
        arrowImgW = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_w);


        desiredLocation = new Location(LocationManager.GPS_PROVIDER);

        task = GlobalVariables.getTask();

        desiredLocation.setLatitude(Double.parseDouble(task.getLatitude()));
        desiredLocation.setLongitude(Double.parseDouble(task.getLongitude()));

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this); //change to gps!
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorManager.registerListener(this, mSensor,SensorManager.SENSOR_DELAY_GAME);

        decorationDesigner();
    }

    @Override
    public void decorationDesigner() {
        super.decorationDesigner();
        headerTextView.setText(getString(R.string.title_activity_game));
    }



    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
      protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
    }

    public void update(){
        if (arrowIV == null){
            arrowIV = (ImageView) findViewById(R.id.compass_arrow);
            RelativeLayout f = (RelativeLayout) findViewById(R.id.fragmentGame);
            int size = f.getMeasuredWidth();
            arrowIV.getLayoutParams().width = size;
            arrowIV.getLayoutParams().height = size;
        }

        if (currentLocation instanceof Location) {
            if (desiredLocation instanceof Location) {
                float bearing = currentLocation.bearingTo(desiredLocation) ;
                float distance = currentLocation.distanceTo(desiredLocation);
                if (distance > 50) {

                    float bearingRelative = bearing - currentBearing;
                    while (bearingRelative < 0) bearingRelative =  bearingRelative + 360;
                    while (bearingRelative > 360) bearingRelative =  bearingRelative - 360;
                    if (bearingRelative <= 22) {
                        arrowIV.setImageBitmap(arrowImgN);
                        arrowIV.setVisibility(View.VISIBLE);
                    } else if (22 < bearingRelative  && bearingRelative <= 68) {
                        arrowIV.setImageBitmap(arrowImgNE);
                        arrowIV.setVisibility(View.VISIBLE);
                    } else if (68 < bearingRelative && bearingRelative <= 135) {
                        arrowIV.setImageBitmap(arrowImgE);
                        arrowIV.setVisibility(View.VISIBLE);
                    } else if (138 < bearingRelative && bearingRelative <= 225) {
                        arrowIV.setImageBitmap(arrowImgS);
                        arrowIV.setVisibility(View.VISIBLE);
                    } else if (225 < bearingRelative && bearingRelative <= 295) {
                        arrowIV.setImageBitmap(arrowImgW);
                        arrowIV.setVisibility(View.VISIBLE);
                    } else if (295 < bearingRelative && bearingRelative <= 338) {
                        arrowIV.setImageBitmap(arrowImgNW);
                        arrowIV.setVisibility(View.VISIBLE);
                    } else if (338 < bearingRelative) {
                        arrowIV.setImageBitmap(arrowImgN);
                        arrowIV.setVisibility(View.VISIBLE);
                    }
                } else {
                    arrowIV.setVisibility(View.INVISIBLE);   //ON PLACE
                    mLocationManager.removeUpdates(this);
                    mSensorManager.unregisterListener(this);
                    Intent intent = new Intent(GameActivity.this, TaskActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    private static List<NameValuePair> post = new ArrayList<NameValuePair>(3);
    Calendar c = Calendar.getInstance();
    int startTime = c.getTime().getMinutes() + c.getTime().getHours()*60;
    boolean isFistTime = true;
    @Override
    public void onLocationChanged(Location location) {

            this.currentLocation = location;

            task.setCurrentLatitud("" + location.getLatitude());
            task.setCurrentLongitude("" + location.getLongitude());

            c = Calendar.getInstance();
            int mseconds = c.getTime().getMinutes() + c.getTime().getHours()*60;
            System.out.println("SRATR: "+ startTime +" zmiana: " + mseconds);
            if(mseconds >= startTime+1 || isFistTime){
                isFistTime = false;
                System.out.println("SENDING!!!");
                startTime = c.getTime().getMinutes() + c.getTime().getHours()*60;
                post.clear();
                post.add(new BasicNameValuePair("id", "" + GlobalVariables.getUser().getId()));
                post.add(new BasicNameValuePair("lat", ""+ location.getLatitude()));
                post.add(new BasicNameValuePair("lng", ""+ location.getLongitude()));
                new postTask().execute(GlobalVariables.getUserSendCoordinates());

            }
            update();

    }
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(GameActivity.this,"Error onProviderDisabled", LENGTH_LONG).show();
    }
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(GameActivity.this,"onProviderEnabled", LENGTH_LONG).show();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(GameActivity.this,"onStatusChanged", LENGTH_LONG).show();
    }
      
    @Override
    public void onSensorChanged(SensorEvent event) {

        currentBearing = event.values[0];
        update();
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    public static CookieStore cookieStore = new BasicCookieStore();
    public static HttpContext localContext = new BasicHttpContext();

    class postTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("!!!!!!!!!!!!!POSZŁO");
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
            View rootView = inflater.inflate(R.layout.fragment_game, container, false);
            return rootView;
        }
    }

}
