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
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

        Task task = GlobalVariables.getTask();

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
                        //System.out.println("!!! N");
                    } else if (22 < bearingRelative  && bearingRelative <= 68) {
                        arrowIV.setImageBitmap(arrowImgNE);
                        arrowIV.setVisibility(View.VISIBLE);
                        //System.out.println("!!! NE");
                    } else if (68 < bearingRelative && bearingRelative <= 135) {
                        arrowIV.setImageBitmap(arrowImgE);
                        arrowIV.setVisibility(View.VISIBLE);
                        //System.out.println("!!! E");
                    } else if (138 < bearingRelative && bearingRelative <= 225) {
                        arrowIV.setImageBitmap(arrowImgS);
                        arrowIV.setVisibility(View.VISIBLE);
                       // System.out.println("!!! S");
                    } else if (225 < bearingRelative && bearingRelative <= 295) {
                        arrowIV.setImageBitmap(arrowImgW);
                        arrowIV.setVisibility(View.VISIBLE);
                        //System.out.println("!!! W");
                    } else if (295 < bearingRelative && bearingRelative <= 338) {
                        arrowIV.setImageBitmap(arrowImgNW);
                        arrowIV.setVisibility(View.VISIBLE);
                        //System.out.println("!!! NW");
                    } else if (338 < bearingRelative) {
                        arrowIV.setImageBitmap(arrowImgN);
                        arrowIV.setVisibility(View.VISIBLE);
                        //System.out.println("!!! N");
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


        @Override
        public void onLocationChanged(Location location) {

            this.currentLocation = location;
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
